import Config.ServerConfig;
import DynamoDB.DAO;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to serve User's Stats
 *
 * @author Cody Cao
 */
@WebServlet("/stats/*")
public class Stats extends HttpServlet {

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

    //Retrieve from DynamoDB
    int userId = Integer.parseInt(urlParts[1]);
    DAO dao = new DAO(userId);
    int[] stats = dao.getStats();

    String responseMsg;

    //In case the given userId is not in dynamoDB
    if(stats == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      responseMsg = "User not found";
      response.getWriter().write(responseMsg);
    }
    else {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");

      //Build the response message
      responseMsg = new StringBuilder("{")
          .append("\"numLlikes\": ").append(stats[0])
          .append(", \"numDislikes\": ").append(stats[1])
          .append("}").toString();

    }
    response.getWriter().write(responseMsg);
  }


  /**
   * Verify that the parameter is correct
   * @param urlParts
   * @return
   */
  private boolean isUrlValid(String[] urlParts) {
    try{
      int userId = Integer.parseInt(urlParts[1]);

      //Check if the parameter is within range
      if(userId <= 0 || userId > ServerConfig.SWIPER_RANGE){
        return false;
      }
    }
    //In case the provided parameter is not an Integer, return false
    catch (NumberFormatException e){
      return false;
    }
    return true;
  }
}

