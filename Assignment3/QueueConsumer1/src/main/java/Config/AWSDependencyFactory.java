package Config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

/**
 * Singleton class that holds clients connection to AWS services
 *
 * @author Cody Cao
 */
public class AWSDependencyFactory {
  private static AWSDependencyFactory instance;
  private static BlockingQueue<DynamoDbAsyncClient> dynamoDbConnectionPool;

  /**
   *
   * @return a factory instance to retrieve a connection
   */
  public static AWSDependencyFactory getInstance() {
    if(instance == null) {
      instance = new AWSDependencyFactory();
    }
    return instance;
  }

  private AWSDependencyFactory() {
    this.dynamoDbConnectionPool = new LinkedBlockingQueue<>(ConsumerConfig.NUM_CONNECTIONS);
    EnvironmentVariableCredentialsProvider credentials = EnvironmentVariableCredentialsProvider.create();

    for(int i = 0; i < ConsumerConfig.NUM_CONNECTIONS; i++) {
      dynamoDbConnectionPool.offer(
          DynamoDbAsyncClient.builder()
            .credentialsProvider(credentials)
            .region(Region.US_WEST_2)
            .build()
      );
    }
  }

  /**
   * @return an instance of DynamoDbClient
   */
  public DynamoDbAsyncClient getDynamoDbClient() {
    try {
      return dynamoDbConnectionPool.poll(300, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * put a client back to the connection pool
   */
  public void putDynamoDBClient (DynamoDbAsyncClient client){
    dynamoDbConnectionPool.offer(client);
  }
}
