import Config.ServerConfig;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to serve User's Matches - Up to 100 people
 *
 * @author Cody Cao
 */
@WebServlet("/matches/*")
public class Matches extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/plain");
    String path = request.getPathInfo();

    //Check if there's a URL
    if (path == null || path.isEmpty() || path.equals("/")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Parameters missing");
      return;
    }

    //Confirm that the URL is valid.
    //If not valid, generate a Bad Request response
    String[] urlParts = path.split("/");
    if (!isUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Invalid parameter");
      return;
    }

    /**
     * TODO:
     * 1. Perform connection to the storage
     * 2. Verify that the UserId is in the storage
     *    a. If not, return code 404 - User not found
     *    b. If yes, return in this format:
     *      {
     *        "matchList": [
     *          "string"
     *        ]
     *      }
     */

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");

    String tempResponse = "{"
        + "\"matchList\": ["
        + "\"string\""
        + "]"
        + "}";
    response.getWriter().write(tempResponse);
  }

  /**
   * Verify that the parameter is correct
   *
   * @param urlParts
   * @return
   */
  private boolean isUrlValid(String[] urlParts) {
    try {
      int userId = Integer.parseInt(urlParts[1]);

      //Check if the parameter is within range
      if (userId <= 0 || userId > ServerConfig.SWIPER_RANGE) {
        return false;
      }
    }
    //In case the provided parameter is not an Integer, return false
    catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
