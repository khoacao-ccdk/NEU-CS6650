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
  public static final int NUM_CONNECTIONS = 100;

  /**
   * RabbitMQ Host
   */
  public static final String QUEUE_HOST = "50.112.70.179";

  /**
   * RabbitMQ Port
   */
  public static final int QUEUE_PORT = 5672;

  /**
   * Virtual Host Name
   */
  public static final String VHOST_NAME = "cs6650";

  /**
   * Username for vhost authentication
   */
  public static final String USER_NAME = "Cody";

  /**
   * Password for vhost authentication
   */
  public static final String PASSWORD = "CS6650-Lab";

  /**
   * Queue1 Name
   */
  public static final String QUEUE_1_NAME = "SwipeData1";

  /**
   * Queue2 Name
   */
  public static final String QUEUE_2_NAME = "SwipeData2";

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
