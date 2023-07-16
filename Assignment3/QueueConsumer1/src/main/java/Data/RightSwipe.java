package Data;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class RightSwipe {

  private int userId;
  private int count;

  /**
   * @return an Integer represents the userId of the item in DynamoDb
   */
  @DynamoDbPartitionKey
  public int getUserId() {
    return this.userId;
  }

  /**
   *
   * @param userId an Integer represents the userId of the item in DynamoDb
   */
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * @return an Integer represents the number of left swipes of the user
   */
  public int getCount() {
    return count;
  }

  /**
   * @param count an Integer represents the number of left swipes of the user
   */
  public void setCount(int count) {
    this.count = count;
  }

  /**
   *
   * @return a String represents object's information
   */
  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("RightSwipe{");
    sb.append("userId=").append(userId);
    sb.append(", count=").append(count);
    sb.append('}');
    return sb.toString();
  }
}
