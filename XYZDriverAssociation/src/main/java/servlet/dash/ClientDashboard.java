package servlet.dash;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import utils.SessionHelper;

/**
 * The dashboard for a client.
 */
public class ClientDashboard extends HttpServlet {

    public static final String INFO_MESSAGE = "infoMessage";
    public static final String USER_OBJECT_ATT = "userObject";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "dash/client_dash.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get user info
        User user = SessionHelper.getUser(request);
        String userStatus = user.getStatus();

        // Check if users membership status is pending
        if (userStatus.equals(User.STATUS_PENDING)) {
            request.setAttribute(INFO_MESSAGE, "<br>Please submit your first payment so that we can approve your membership.<br>");
        }

        request.setAttribute(USER_OBJECT_ATT, user);
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}