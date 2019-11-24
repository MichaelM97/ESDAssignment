package servlet.auth;

import db.DatabaseFactory;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * Servlet for the Registration flow.
 */
public class Registration extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "auth/registration.jsp";

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
        RequestDispatcher view = request.getRequestDispatcher(JSP);
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
        boolean userWasCreated = false;

        // Get users entry from JSP
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        Date dob = null;
        try {
            dob = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("dob"));
        } catch (ParseException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (dob == null) {
            request.setAttribute(ERROR_MESSAGE, "There was an issue with your date of birth");
        } else {
            // Hash the password
            String hashedPassword = HashHelper.hashString(password);
            if (hashedPassword == null) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue with your password");
            } else {
                // Create user object
                User user = new User(
                        username,
                        hashedPassword,
                        name,
                        address,
                        dob,
                        new Date(),
                        0.0f,
                        User.STATUS_PENDING
                );

                // Save the user in the DB
                DatabaseFactory dbf = new DatabaseFactory();
                boolean insertSuccessful = dbf.insert(user);

                if (insertSuccessful) {
                    // Save the user in the current session
                    SessionHelper.setUser(request, user);

                    // Navigate to the client dashboard
                    response.sendRedirect("Dashboard");
                    userWasCreated = true;
                } else {
                    request.setAttribute(ERROR_MESSAGE, "Failed to create account");
                }
            }
        }

        if (!userWasCreated) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
            dispatcher.forward(request, response);
        }
    }
}
