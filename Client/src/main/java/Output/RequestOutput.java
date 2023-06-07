package Output;

public class RequestOutput {

  public static final RequestOutput POISON_PILL = new RequestOutput();
  private long startTime;
  private String requestType;
  private long latency;
  private int responseCode;

  /**
   * Constructs a new output object
   *
   * @param startTime    a Long represents the start time in milliseconds
   * @param requestType  a String represents the request type
   * @param latency      a long represents the request's latency
   * @param responseCode an Integer represents the response code
   */
  public RequestOutput(long startTime, String requestType, long latency, int responseCode) {
    this.startTime = startTime;
    this.requestType = requestType;
    this.latency = latency;
    this.responseCode = responseCode;
  }

  /**
   * Private constructor - used to create a poison pill
   */
  private RequestOutput() {
  }

  /**
   * @return a Long represents the start time of request
   */
  public long getStartTime() {
    return startTime;
  }

  /**
   * @return a String represents the request type
   */
  public String getRequestType() {
    return requestType;
  }

  /**
   * @return a long represents the request's latency in millisecond
   */
  public long getLatency() {
    return latency;
  }

  /**
   * @return an Integer represents the response code
   */
  public int getResponseCode() {
    return responseCode;
  }

  /**
   * @return a String array to write to csv file
   */
  public String[] toCSVRow() {
    return new String[]{
        String.valueOf(startTime),
        requestType,
        String.valueOf(latency),
        String.valueOf(responseCode)
    };
  }
}
