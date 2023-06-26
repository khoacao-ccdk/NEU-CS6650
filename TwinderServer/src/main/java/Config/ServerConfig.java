package Config;

public class ServerConfig {

  /**
   * Swiper's id range
   */
  public static final int SWIPER_RANGE = 5000;

  /**
   * Swipee's id range
   */
  public static final int SWIPEE_RANGE = 1000000;

  /**
   * Length of a randomly generated comment
   */
  public static final int COMMENT_LENGTH = 256;

  /**
   * Number of connections opened towards the message queue host
   */
  public static final int NUM_CONNECTIONS = 10;

  /**
   * RabbitMQ Host
   */
  public static final String QUEUE_HOST = "localhost";

  /**
   * Virtual Host Name
   */
  public static final String VHOST_NAME = "cs6650";

  /**
   * Queue Name
   */
  public static final String QUEUE_NAME = "Swipe";

  public static final String USER_NAME = "Cody";

  public static final String PASSWORD = "CS6650-Lab";

  /**
   * Number of producer threads being allocated for the thread pool
   */
  public static final int NUM_PRODUCER_THREAD = NUM_CONNECTIONS;

  /**
   * Private constructor since no instance of this class is needed
   */
  private ServerConfig() {
  }
}
