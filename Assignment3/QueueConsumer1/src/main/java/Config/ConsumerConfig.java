package Config;

public class ConsumerConfig {

  /**
   * Number of connections opened towards the message queue host - also used to allocate threads
   */
  public static final int NUM_CONNECTIONS = 10;

  /**
   * RabbitMQ Host
   *
   * AWS Host: "50.112.70.179"
   */
  public static final String QUEUE_HOST = "localhost";

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
   * Queue Name
   */
  public static final String QUEUE_NAME = "SwipeData1";

  /**
   * Queue durability
   */
  public static final boolean QUEUE_DURABILITY = true;

  /**
   * Queue auto acknowledgement - currently not auto
   */
  public static final boolean QUEUE_AUTO_ACK = false;

  /**
   * DynamoDB table name
   */
  public static final String DYNAMO_TABLE_NAME = "TwinderUserSwipeCounter";

  /**
   * Primary Key field name
   */
  public static final String DYNAMO_PK = "SwiperId";

  /**
   * Private constructor since no object should be constructed for this class
   */
  private ConsumerConfig(){}

}
