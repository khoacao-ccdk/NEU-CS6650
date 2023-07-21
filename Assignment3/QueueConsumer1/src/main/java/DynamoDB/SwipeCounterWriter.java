package DynamoDB;

import Config.AWSDependencyFactory;
import Config.ConsumerConfig;
import java.util.concurrent.CompletableFuture;
import software.amazon.awssdk.enhanced.dynamodb.NestedAttributeName;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

/**
 * SwipeCounterWriter class - used to update number of left/right swipes in DynamoDB
 *
 * @author Cody Cao
 */
public class SwipeCounterWriter {

  private DynamoDbAsyncClient dynamoDbClient;
  private int swiperId;
  String swipeType;

  /**
   * Constructs a new writer
   *
   * @param swiperId
   * @param swipeType
   */
  public SwipeCounterWriter(int swiperId, String swipeType) {
    this.swiperId = swiperId;
    this.swipeType = swipeType;
  }

  /**
   * Update the counter based on the given value
   */
  public void updateCounter() {
    this.dynamoDbClient = AWSDependencyFactory.getInstance().getDynamoDbClient();
    try{
      // Table name and key attribute
      String tableName = ConsumerConfig.DYNAMO_TABLE_NAME;
      // Primary key attribute name and value
      String primaryKeyAttributeName = ConsumerConfig.DYNAMO_PK;
      // Define the update expression
      String attrName = swipeType + "swipe";
      String updateExpression = String.format("SET #attr = if_not_exists(#attr, :startValue) + :incrValue");
      // Set the attribute value to increment by 1
      int incrementByValue = 1;

      // Create the update request with the expression attribute names and values
      UpdateItemRequest updateRequest = UpdateItemRequest.builder()
          .tableName(tableName)
          .key(java.util.Map.of(
              primaryKeyAttributeName,
              AttributeValue.builder().n(Integer.toString(swiperId)).build()
          ))
          .updateExpression(updateExpression)
          .expressionAttributeValues(java.util.Map.of(
              ":incrValue", AttributeValue.builder().n(Integer.toString(incrementByValue)).build(),
              ":startValue", AttributeValue.builder().n("0").build() // Set the start value to 0 in case it's not in the db yet
          ))
          .expressionAttributeNames(java.util.Map.of(
              "#attr", attrName // Use an attribute name placeholder to avoid conflicts with reserved words
          ))
          .build();

      // Perform the update asynchronously
      CompletableFuture<UpdateItemResponse> updateFuture = dynamoDbClient.updateItem(updateRequest);

      // Handle the response
      updateFuture.whenComplete((updateResponse, ex) -> {
        if (ex != null) {
          System.err.println("Error updating the item: " + ex.getMessage());
        }
      });

      // Wait for the operation to complete
      updateFuture.join();
      // Return the DynamoDB async client back to the connection pool
      AWSDependencyFactory.getInstance().putDynamoDBClient(this.dynamoDbClient);
    } catch (Exception e){
      e.printStackTrace();
    }
  }
}
