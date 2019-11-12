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
import javax.servlet.http.HttpSession;
import model.User;
import utils.HashHelper;
import utils.SessionHelper;

/**
 * Servlet for the Client login flow.
 */
public class Login extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";

    String jsp = "auth/client_login.jsp";

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
        // If the admin user type was selected, switch the JSP
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
        // Get users entry
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Query the DB for users
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet userResult = dbf.get_from_table("users", "*");
        boolean userFound = false;

        // Hash the entered password
        String hashedPassword = HashHelper.hashString(password);
        if (hashedPassword == null) {
            request.setAttribute(ERROR_MESSAGE, "Error with your password");
        } else {
            // Search for the user in the query results
            if (userResult != null) {
                try {
                    do {
                        try {
                            if (userResult.getString("id").equals(username)) {
                                userFound = true;
                                // Check if the password hashes match
                                if (userResult.getString("password").equals(hashedPassword)) {
                                    // Save the user in the current session
                                    User user = new User(username, password, userResult.getString("status"));
                                    SessionHelper.setUser(request, user);

                                    // TODO: Navigate to the relevant dashboard
                                    request.setAttribute(ERROR_MESSAGE, "User found!");
                                } else {
                                    request.setAttribute(ERROR_MESSAGE, "Incorrect password");
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } while (userResult.next());
                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (userResult == null || userFound == false) {
                request.setAttribute(ERROR_MESSAGE, "No user found with that username");
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
        dispatcher.forward(request, response);
    }
}
