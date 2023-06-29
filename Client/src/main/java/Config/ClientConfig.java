package Config;

/**
 * Config Class to store common variables - used by both single-threaded and multi-threaded client
 * classes.
 */
public final class ClientConfig {

  /**
   * The BaseURL to send request to server
   */
  public static final String REMOTE_BASE_URL = "http://TwinderServer-ELB-1396322766.us-west-2.elb.amazonaws.com:8080/TwinderServer";

  /**
   * The BaseURL to send request to server
   */
  public static final String LOCAL_BASE_URL = "http://localhost:8080/TwinderServer";

  /**
   * How many request the client should send to server
   */
  public static final int REQUEST_NUM = 50000;

  /**
   * Number of threads the client will use
   */
  public static final int NUM_THREADS = 50;

  /**
   * Swiper's id range
   */
  public static final int SWIPER_RANGE = 100;

  /**
   * Swipee's id range
   */
  public static final int SWIPEE_RANGE = 1000000;

  /**
   * Length of a randomly generated comment
   */
  public static final int COMMENT_LENGTH = 256;

  /**
   * Number of time the client thread will retry sending a thread until it deem the request as
   * failed
   */
  public static final int NUM_RETRY = 5;

  private ClientConfig() {
  }
}
