
import SwipeQueue.SwipeConsumer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer {
  public static void main(String[] args){
    Map<Integer, ArrayBlockingQueue<Integer>> rightSwipeMap = new ConcurrentHashMap<>();

    SwipeConsumer consumer = new SwipeConsumer(rightSwipeMap);
    consumer.start();
  }
}
