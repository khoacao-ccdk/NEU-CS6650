package MultiThreadPart2;

import Config.ClientConfig;
import Config.SwipeDetails;
import Output.RequestOutput;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class RequestThread implements Runnable {

  /**
   * Poison pill to signal threads to stop
   */
  public static final RequestThread POISON_PILL = new RequestThread();

  /**
   * Processing pill to signal the threads to send a POST request
   */
  public static final RequestThread PROCESS_PILL = new RequestThread();

  private CloseableHttpClient httpclient;
  private AtomicInteger successCounter;
  private AtomicInteger failCounter;
  private ArrayBlockingQueue<RequestThread> processingQueue;
  private ConcurrentLinkedQueue<RequestOutput> writeQueue;

  /**
   * @param successCounter  AtomicInteger object to count the number of successful requests
   * @param failCounter     AtomicInteger object to count the number of fail requests
   * @param processingQueue an ArrayBlockingQueue to receive post requests
   * @oaram writeQueue a ConcurrentLinkedQueue to send results
   */
  public RequestThread(
      AtomicInteger successCounter,
      AtomicInteger failCounter,
      ArrayBlockingQueue processingQueue,
      ConcurrentLinkedQueue writeQueue
  ) {
    this.successCounter = successCounter;
    this.failCounter = failCounter;
    this.processingQueue = processingQueue;
    this.writeQueue = writeQueue;
    this.httpclient= HttpClients.createDefault();
  }

  /**
   * Empty constructor - used to create pills
   */
  private RequestThread() {
  }

  /**
   * Starts a thread
   */
  @Override
  public void run() {
    while (true) {
      RequestThread signal = null;
      try {
        signal = processingQueue.poll(500, TimeUnit.MILLISECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (signal == POISON_PILL || signal == null) {
        writeQueue.offer(RequestOutput.POISON_PILL);
        return;
      }
      post();
    }
  }

  /**
   * Generate a POST request and send to server.
   */
  private void post() {
    ThreadLocalRandom rand = ThreadLocalRandom.current();
    String leftorright = rand.nextInt(2) == 0
        ? "left"
        : "right";

    //Set up request
    HttpPost postReq = new HttpPost(
        String.format("%s/swipe/%s", ClientConfig.BASE_URL, leftorright));
    postReq.setEntity(new StringEntity(getRandomSwipeBody()));
    postReq.setHeader("Accept", "application/json");
    postReq.setHeader("Content-type", "application/json");
    long start, end;

    CloseableHttpResponse response = null;
    for (int i = 0; i <= ClientConfig.NUM_RETRY; i++) {
      try {
        start = System.currentTimeMillis();
        response = httpclient.execute(postReq);
        end = System.currentTimeMillis();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      try {
        HttpEntity entity1 = response.getEntity();
        //Currently not doing anything with the response. We are consuming the response only.
        //String bodyAsString = EntityUtils.toString(response.getEntity());
        EntityUtils.consume(entity1);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          response.close();

          //If the response is already successful, we don't need to try again
          if (response.getCode() == 201) {
            successCounter.getAndIncrement();
            writeQueue.offer(new RequestOutput(
                start,
                "POST",
                end - start,
                response.getCode()
            ));
            return;
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    //After 5 retries, if the request still fail, increment the fail counter
    failCounter.getAndIncrement();
  }

  /**
   * @return a Random request body for swipe POST request
   */
  private String getRandomSwipeBody() {
    ThreadLocalRandom rand = ThreadLocalRandom.current();
    int swiper = rand.nextInt(ClientConfig.SWIPER_RANGE) + 1;
    int swipee = rand.nextInt(ClientConfig.SWIPEE_RANGE) + 1;

    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'

    //Generate a random comment
    String comment = rand.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(ClientConfig.COMMENT_LENGTH)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    SwipeDetails swipe = new SwipeDetails();
    swipe.setSwiper(String.valueOf(swiper));
    swipe.setSwipee(String.valueOf(swipee));
    swipe.setComment(comment);
    return new Gson().toJson(swipe);
  }
}
