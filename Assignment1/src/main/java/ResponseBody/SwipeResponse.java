package ResponseBody;

import com.google.gson.Gson;

public class SwipeResponse {

  /**
   * Dummy response message for a left swipe
   */
  public static final String LEFT_SWIPE_RESPONSE = new Gson().toJson(new SwipeResponse(
      123,
      1234,
      "You are not my type, sorry."
  ));


  /**
   * Dummy response message for a right swipe
   */
  public static final String RIGHT_SWIPE_RESPONSE = new Gson().toJson(new SwipeResponse(
      123,
      1234,
      "You are my type!"
  ));

  private int SwiperID;

  private int SwipeeID;

  private String response;

  /**
   * Construct a new SwipeResponse object
   * @param swiperID an Integer represents the swiper's id
   * @param swipeeID an Integer represents the swipee's id
   * @param response a String represents the message
   */
  public SwipeResponse(int swiperID, int swipeeID, String response) {
    SwiperID = swiperID;
    SwipeeID = swipeeID;
    this.response = response;
  }

  /**
   *
   * @return an Integer represents the swiper's id
   */
  public int getSwiperID() {
    return SwiperID;
  }

  /**
   * Set swiper's ID
   * @param swiperID an Integer represents the swiper's id
   */
  public void setSwiperID(int swiperID) {
    SwiperID = swiperID;
  }

  /**
   *
   * @return an Integer represents the swipee's id
   */
  public int getSwipeeID() {
    return SwipeeID;
  }

  /**
   * Set swipee's id
   * @param swipeeID an Integer represents the swipee's id
   */
  public void setSwipeeID(int swipeeID) {
    SwipeeID = swipeeID;
  }

  /**
   *
   * @return a String represents the message
   */
  public String getResponse() {
    return response;
  }

  /**
   * Set the message
   * @param response a String represents the message
   */
  public void setResponse(String response) {
    this.response = response;
  }
}
