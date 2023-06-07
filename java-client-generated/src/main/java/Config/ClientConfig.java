package Config;

/**
 * Config Class to store common variables - used by both single-threaded and multi-threaded client
 * classes.
 */
public final class ClientConfig {

  /**
   * The BaseURL to send request to server
   */
  public static final String BASE_URL = "http://34.220.222.4:8080/TwinderServer";

  /**
   * How many request the client should send to server
   */
  public static final int REQUEST_NUM = 10000;

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

  private ClientConfig() {
  }
}
