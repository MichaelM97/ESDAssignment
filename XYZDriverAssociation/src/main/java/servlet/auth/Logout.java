package servlet.auth;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.SessionHelper;

public class Logout extends HttpServlet {

    public static final String INFO_MESSAGE = "infoMessage";
    private static final String JSP = "home.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SessionHelper.setUser(request, null);
        request.setAttribute(INFO_MESSAGE, "You were successfully logged out");
        RequestDispatcher view = request.getRequestDispatcher(JSP);
        view.forward(request, response);
    }
}
