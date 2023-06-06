import RequestBody.SwipeRequest;
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

    SwipeRequest swipeInfo;
    try {
      Gson gSon = new Gson();
      //Parse request body from JSON to object
      String s;
      StringBuilder sb = new StringBuilder();
      while ((s = request.getReader().readLine()) != null) {
        sb.append(s);
      }
      swipeInfo = (SwipeRequest) gSon.fromJson(sb.toString(), SwipeRequest.class);

    } catch (Exception e){
      System.err.println(e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Missing body");
      return;
    }

    //If there was no body - return a bad request response
    if(swipeInfo == null){
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Parameters missing");
      return;
    }

    response.setStatus(HttpServletResponse.SC_CREATED);
    //Return a dummy response
    response.getWriter().write("Written");
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
