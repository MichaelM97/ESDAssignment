package servlet.auth;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for the Client login flow.
 */
public class Login extends HttpServlet {

    /**
     * Displays the client_login JSP.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Select which JSP to show based on which user type was selected
        String jsp = "auth/client_login.jsp";
        if (request.getParameter("adminLoginButton") != null) {
            jsp = "auth/admin_login.jsp";
        }

        // Show the selected JSP
        RequestDispatcher view = request.getRequestDispatcher(jsp);
        view.forward(request, response);
    }

    /**
     * Submits the users entered information to the DB and authenticates login.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // TODO: Await DB setup in order to authenticate the user using the above parameters
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles a client login.";
    }

}