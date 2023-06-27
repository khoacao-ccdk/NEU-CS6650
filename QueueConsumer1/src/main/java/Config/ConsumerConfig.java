package Config;

public class ConsumerConfig {

  /**
   * Number of connections opened towards the message queue host - also used to allocate threads
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
   * Username for vhost authentication
   */
  public static final String USER_NAME = "Cody";

  /**
   * Password for vhost authentication
   */
  public static final String PASSWORD = "CS6650-Lab";

  /**
   * Queue Name
   */
  public static final String QUEUE_NAME = "SwipeData1";


  /**
   * Private constructor since no object should be constructed for this class
   */
  private ConsumerConfig(){}

}
