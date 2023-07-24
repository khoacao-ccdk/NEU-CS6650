package Config;

public class ServerConfig {
  /**
   * Swiper's id range
   */
  public static final int SWIPER_RANGE = 50000;

  /**
   * DynamoDB table name
   */
  public static final String DYNAMO_TABLE_NAME = "SwipeData";

  /**
   * Primary Key field name
   */
  public static final String DYNAMO_PK = "SwiperId";

  /**
   * a String represents the count left swipe column
   */
  public static final String LEFT_SWIPE_COL_NAME = "LeftSwipe";

  /**
   * a String represents the count right swipe column
   */
  public static final String RIGHT_SWIPE_COL_NAME = "RightSwipe";

  /**
   * a String represents the potential matches
   */
  public static final String MATCHES_COL_NAME = "Matches";

  /**
   * Swipee's id range
   */
  public static final int SWIPEE_RANGE = SWIPER_RANGE;

  private ServerConfig(){}
}
