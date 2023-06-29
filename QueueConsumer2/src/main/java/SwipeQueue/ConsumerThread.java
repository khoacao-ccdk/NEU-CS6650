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

public class ConsumerThread implements Runnable {

  private Channel chan;
  Map<Integer, ArrayBlockingQueue<Integer>> rightSwipeMap;

  /**
   * Constructs a new consumer thread to handle messages
   *
   * @param conn          a Connection object represents the connection to the queue
   * @param rightSwipeMap a ConcurrentHashMap represents user's right swipes
   */
  public ConsumerThread(Connection conn, Map<Integer, ArrayBlockingQueue<Integer>> rightSwipeMap) {
    this.rightSwipeMap = rightSwipeMap;
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

      Swipe swipeInfo = new Gson().fromJson(message, Swipe.class);
      String swipeType = swipeInfo.getSwipeType();
      if (swipeType.equals("right")) {
        int swiperId = swipeInfo.getSwiper();
        int swipeeId = swipeInfo.getSwipee();

        //Make sure the array list is in the map
        if (!rightSwipeMap.containsKey(swiperId)) {
          rightSwipeMap.putIfAbsent(swiperId,
              new ArrayBlockingQueue<>(ConsumerConfig.RIGHT_SWIPED_LIMIT));
        }

        ArrayBlockingQueue<Integer> swipeeList = rightSwipeMap.getOrDefault(swiperId, null);

        //Remove the first in queue if the list is full
        if (swipeeList.size() == ConsumerConfig.RIGHT_SWIPED_LIMIT) {
          swipeeList.poll();
        }
        swipeeList.offer(swipeeId);

        //Acknowledge the message after performing computation
      }
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
