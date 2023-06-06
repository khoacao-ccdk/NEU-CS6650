package RequestBody;

public class SwipeRequest {

  private int swiper;

  private int swipee;

  private String body;

  /**
   * Construct a new SwipeResponse object
   * @param swiper an Integer represents the swiper's id
   * @param swipee an Integer represents the swipee's id
   * @param body a String represents the message
   */
  public SwipeRequest(int swiper, int swipee, String body) {
    this.swiper = swiper;
    this.swipee = swipee;
    this.body = body;
  }

  /**
   *
   * @return an Integer represents the swiper's id
   */
  public int getSwiper() {
    return swiper;
  }

  /**
   * Set swiper's ID
   * @param swiper an Integer represents the swiper's id
   */
  public void setSwiper(int swiper) {
    this.swiper = swiper;
  }

  /**
   *
   * @return an Integer represents the swipee's id
   */
  public int getSwipee() {
    return swipee;
  }

  /**
   * Set swipee's id
   * @param swipee an Integer represents the swipee's id
   */
  public void setSwipee(int swipee) {
    this.swipee = swipee;
  }

  /**
   *
   * @return a String represents the message
   */
  public String getBody() {
    return body;
  }

  /**
   * Set the message
   * @param body a String represents the message
   */
  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("SwipeRequest{");
    sb.append("Swiper=").append(swiper);
    sb.append(", Swipee=").append(swipee);
    sb.append(", body='").append(body).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
