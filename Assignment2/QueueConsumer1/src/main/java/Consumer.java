import SwipeQueue.SwipeConsumer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer {
  public static void main(String[] args){
    Map<Integer, AtomicInteger>
        likeMap = new ConcurrentHashMap<>(),
        dislikeMap = new ConcurrentHashMap<>();

    SwipeConsumer consumer = new SwipeConsumer(likeMap, dislikeMap);
    consumer.start();
  }
}
