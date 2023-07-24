import Config.ServerConfig;
import DynamoDB.DAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
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
  public void init() throws ServletException {
    super.init();

    //Set aws credentials
    System.setProperty("aws.accessKeyId", "ASIATYHSEPEYYWGLVHNR");
    System.setProperty("aws.secretAccessKey", "iXYTU2Jqa4h0vKzUoBG+J2aKWZgUP4+nNLfZ8M5I");
    System.setProperty("aws.sessionToken", "FwoGZXIvYXdzEJL//////////wEaDBSJQA9wFpJpZt+E+iLJAeixT75PcobOQEXyPFmOHcjECd+y8N+6YeB03wTT0onODgiqzcrnrHv+++uog/XGBP/nOTzUM0pZfh9eeCfL1zZZ5YcHevN05ZrOXgltJXuEy2isHATRNSb1sXb1H6l1fl9JCRQyrmoha772r9GuVuWmzX20y3P3S/KyHa8MpollZJ/hvkTGd8HIHt0CNDNzx1+NPAAMa+SPe5mhf277SjsPt6BEwV4kFOJOPh+BfJZqcWXLYUx82LMPXiQJ0ZC5AOR7eqy66hNH8Cjyz/qlBjItMNEFX8bEX3wdzJfMgYHRqnqXhnD8dt/wD/K6VhD/8L8sDgGzGtBNapTM0uD8");
  }


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
    List<Integer> matches = dao.getMatches();
    String responseMsg;
    if(matches == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      responseMsg = "User not found";
    }
    else {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");

      //Build the content of the response
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < matches.size(); i++) {
        sb.append(matches.get(i));
        if(i < matches.size() - 1){
          sb.append(", ");
        }
      }
      String matchList = sb.toString();

      responseMsg = new StringBuilder("{")
          .append("\"matchList\": [")
          .append(matchList)
          .append("]")
          .append("}").toString();
    }
    response.getWriter().write(responseMsg);

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
