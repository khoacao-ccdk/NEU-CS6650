package Config;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Class that holds clients connection to AWS services
 *
 * @author Cody Cao
 */
public class AWSDependencyFactory {

  private AWSDependencyFactory() {
  }

  /**
   * @return an instance of DynamoDbClient
   */
  public static DynamoDbAsyncClient dynamoDbClient() {
    return DynamoDbAsyncClient.builder()
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .region(Region.US_WEST_2)
        .build();
  }
}
