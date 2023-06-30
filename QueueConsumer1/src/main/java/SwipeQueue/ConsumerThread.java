package SwipeQueue;

import Body.Swipe;
import Config.ConsumerConfig;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerThread implements Runnable {

  private Channel chan;
  private Map<Integer, AtomicInteger> likeMap, dislikeMap;

  /**
   * Constructs a new consumer thread to handle messages
   *
   * @param conn          a Connection object represents the connection to the queue
   * @param likeMap a ConcurrentHashMap represents user's likes
   * @param dislikeMap a ConcurrentHashMap represents user's dislikes
   */
  public ConsumerThread(Connection conn,
      Map<Integer, AtomicInteger> likeMap,
      Map<Integer, AtomicInteger> dislikeMap
      ) {
    this.likeMap = likeMap;
    this.dislikeMap = dislikeMap;
    try {
      this.chan = conn.createChannel();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Callback function to receive message
   */
  private DeliverCallback callback = (consumerTag, delivery) -> {
    try {
      String message = new String(delivery.getBody(), "UTF-8");

      //Deserialize message to object
      Swipe swipeInfo = new Gson().fromJson(message, Swipe.class);
      String swipeType = swipeInfo.getSwipeType();
      Map<Integer, AtomicInteger> countMap;

      //Get map based on swipe type
      if (swipeType.equals("left")) {
        countMap = dislikeMap;
      }
      else {
        countMap = likeMap;
      }

      //Update like/dislike number
      int swiperId = swipeInfo.getSwiper();
      if(!countMap.containsKey(swiperId)){
        countMap.put(swiperId, new AtomicInteger(0));
      }
      AtomicInteger count = countMap.getOrDefault(swiperId, new AtomicInteger(0));

      //Acknowledge the message after performing computation
      chan.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    } catch (Exception e) {
      //In case an exception was thrown, reject the message and let RabbitMQ knows that it's failed to be consumed
      chan.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
    }
  };

  @Override
  public void run() {
    try {
      //Bind the queue to exchange
      chan.queueDeclare(ConsumerConfig.QUEUE_NAME, false, false, false, null);

      //Start listening for messages
      boolean basicAck = false;
      chan.basicConsume(ConsumerConfig.QUEUE_NAME, basicAck, callback, (consumerTag -> {
      }));

      System.out.println(String.format("Threads num %d running on channel %d",
          Thread.currentThread().getId(),
          chan.getChannelNumber()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
