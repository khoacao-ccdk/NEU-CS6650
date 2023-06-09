package Config;

/**
 * Config Class to store common variables - used by both single-threaded and multi-threaded client
 * classes.
 */
public final class ClientConfig {

  /**
   * The BaseURL to send request to server
   */
  public static final String BASE_URL = "http://localhost:8080/TwinderServer";

  /**
   * How many request the client should send to server
   */
  public static final int REQUEST_NUM = 500000;

  /**
   * Number of threads the client will use
   */
  public static final int NUM_THREADS = 6;

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
   * Number of time the client thread will retry sending a thread until it deem the request as
   * failed
   */
  public static final int NUM_RETRY = 5;

  private ClientConfig() {
  }
}
