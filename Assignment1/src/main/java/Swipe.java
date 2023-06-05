import ResponseBody.SwipeResponse;
import com.google.gson.Gson;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Swipe class to handle Swipe requests.
 *
 * @author Cody Cao
 */
public class Swipe extends HttpServlet {

  //Swipe types supported by the server
  private static final List<String> SWIPE_TYPE = List.of("left", "right");

  /**
   * Handle POST request for Swipe
   *
   * @param request  an HttpServletRequest object represents the client's request
   * @param response an HttpServletResponse object represents the server's response
   * @throws ServletException when there's an exception with ServLet
   * @throws IOException      when there's an IOException
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
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

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_CREATED);

    //Return a dummy response based on type of swipe
    String swipe = urlParts[1];
    response.getWriter().write(
        swipe.equals("left")
            ? SwipeResponse.LEFT_SWIPE_RESPONSE
            : SwipeResponse.RIGHT_SWIPE_RESPONSE
    );

    response.flushBuffer();
  }

  /**
   * @param urlPath an Array with parameters of the path
   * @return whether the argument provided is valid for the request
   */
  private boolean isUrlValid(String[] urlPath) {
    if (SWIPE_TYPE.contains(urlPath[1])) {
      return true;
    }
    return false;
  }
}
