import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.SwipeApi;

import java.io.File;
import java.util.*;

public class APITest {

  public static void main(String[] args) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath("http://localhost:8081/Twinder");
    SwipeApi apiInstance = new SwipeApi(apiClient);
    SwipeDetails body = new SwipeDetails(); // SwipeDetails | response details
    String leftorright = "left"; // String | Ilike or dislike user
    try {
      apiInstance.swipe(body, leftorright);
    } catch (ApiException e) {
      System.err.println("Exception when calling SwipeApi#swipe");
      e.printStackTrace();
    }
  }
}