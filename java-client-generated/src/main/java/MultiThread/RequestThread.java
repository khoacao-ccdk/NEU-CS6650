package MultiThread;

import Config.ClientConfig;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import io.swagger.client.model.SwipeDetails;
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

  /**
   * Number of time the client thread will retry sending a thread until it deem the request as
   * failed
   */
  public static final int NUM_RETRY = 5;
  CloseableHttpClient httpclient = HttpClients.createDefault();
  AtomicInteger successCounter;
  AtomicInteger failCounter;
  ArrayBlockingQueue<RequestThread> processingQueue;

  /**
   * @param successCounter  AtomicInteger object to count the number of successful requests
   * @param failCounter AtomicInteger object to count the number of fail requests
   * @param processingQueue a ConcurrentLinkedQueue to receive post requests
   */
  public RequestThread(AtomicInteger successCounter, AtomicInteger failCounter,
      ArrayBlockingQueue processingQueue) {
    this.successCounter = successCounter;
    this.failCounter = failCounter;
    this.processingQueue = processingQueue;
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

    CloseableHttpResponse response = null;
    for (int i = 0; i <= NUM_RETRY; i++) {
      try {
        response = httpclient.execute(postReq);
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
