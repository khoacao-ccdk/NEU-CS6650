package SingleThread;

import Config.ClientConfig;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;
import java.util.Random;

/**
 * Main class used to test API
 */
public class APITest {

  public static void main(String[] args) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(ClientConfig.BASE_URL);
    SwipeApi apiInstance = new SwipeApi(apiClient);


    Random rand = new Random();
    String leftorright = rand.nextInt(2) == 0
        ? "left"
        : "right";

    try {
      //Send request to server
      for(int i = 0; i < ClientConfig.REQUEST_NUM; i++){
        SwipeDetails body = getRandomSwipeBody(); // SwipeDetails | response details
        apiInstance.swipe(body, leftorright);
      }
    } catch (ApiException e) {
      System.err.println("Exception when calling SwipeApi#swipe");
      e.printStackTrace();
    }
  }

  /**
   * @return a Random request body for swipe POST request
   */
  private static SwipeDetails getRandomSwipeBody() {
    Random rand = new Random();
    int swiper = rand.nextInt(ClientConfig.SWIPER_RANGE) + 1;
    int swipee = rand.nextInt(ClientConfig.SWIPEE_RANGE) + 1;

    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'

    //Generate a random comment
    String comment = rand.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(ClientConfig.COMMENT_LENGTH)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    SwipeDetails swipe = new SwipeDetails();
    swipe.setSwiper(String.valueOf(swiper));
    swipe.setSwipee(String.valueOf(swipee));
    swipe.setComment(comment);
    return swipe;
  }
}