package servlet.auth;

import db.DatabaseFactory;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import utils.HashHelper;
import utils.SessionHelper;

/**
 * Servlet for the Client login flow.
 */
public class Login extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "auth/login.jsp";

    /**
     * Displays the login JSP relevant to the selected user type.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher(JSP);
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
        // Get users entry
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Query the DB for users
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet userResult = dbf.get_from_table("users", username);
        boolean userLoggedIn = false;

        // Hash the entered password
        String hashedPassword = HashHelper.hashString(password);
        if (hashedPassword == null) {
            request.setAttribute(ERROR_MESSAGE, "There is an issue with your password.");
        } else {
            if (userResult != null) {
                try {
                    if (userResult.getString("id").equals(username)) {
                        // Check if the password hashes match
                        if (userResult.getString("password").equals(hashedPassword)) {
                            // Save the user in the current session
                            User user = new User(
                                    username,
                                    hashedPassword,
                                    userResult.getString("name"),
                                    userResult.getString("address"),
                                    userResult.getDate("dob"),
                                    userResult.getDate("dor"),
                                    userResult.getFloat("balance"),
                                    userResult.getString("status")
                            );
                            SessionHelper.setUser(request, user);

                            // Navigate to relevant dashboard
                            if (user.getStatus().equals(User.ADMIN)) {
                                response.sendRedirect("AdminDashboard");
                            } else {
                                response.sendRedirect("ClientDashboard");
                            }
                            userLoggedIn = true;
                        } else {
                            request.setAttribute(ERROR_MESSAGE, "Incorrect password.");
                        }
                    }
                } catch (SQLException ex) {
                    request.setAttribute(ERROR_MESSAGE, "There was an issue logging you in.");
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                request.setAttribute(ERROR_MESSAGE, "No user found with that username");
            }
        }
        if (userLoggedIn == false) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
            dispatcher.forward(request, response);
        }
    }
}
