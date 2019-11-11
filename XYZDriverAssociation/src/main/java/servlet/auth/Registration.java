package servlet.auth;

import db.DatabaseFactory;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import utils.HashHelper;

/**
 * Servlet for the Registration flow.
 */
public class Registration extends HttpServlet {

    private static String jsp = "auth/registration.jsp";
    private static String errorMessageAtt = "errorMessage";

    /**
     * Displays the registration JSP.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher(jsp);
        view.forward(request, response);
    }

    /**
     * Submits the users entered information to the DB and creates a new
     * account.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get users entry
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Hash the password
        String hashedPassword = HashHelper.hashString(password);
        if (hashedPassword != null) {
            // Create user object
            User user = new User(username, hashedPassword, "APPROVED");

            // Save the user in the DB
            DatabaseFactory dbf = new DatabaseFactory();
            boolean insertSuccessful = dbf.insert("users", user);

            if (insertSuccessful) {
                // TODO: Navigate to the client dashboard?
                request.setAttribute(errorMessageAtt, "Account created!");
            } else {
                request.setAttribute(errorMessageAtt, "Failed to create account");
            }
        } else {
            request.setAttribute(errorMessageAtt, "There was an issue with your password");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
        dispatcher.forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Registers a new users account.";
    }
}
