package servlet.membership;

import db.DatabaseFactory;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;

public class SuspendResumeMembership extends HttpServlet {

    public static final String USER_LIST = "userList";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String USER_ID = "userID";

    private static final String JSP = "membership/suspend_resume_membership.jsp";

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
        // Get all users from the DB
        ResultSet usersResult = new DatabaseFactory().get_from_table("users", "*");

        List<User> userList = new ArrayList<>();
        try {
            do {
                String usersStatus = usersResult.getString("status");
                if (usersStatus.equals(User.STATUS_APPROVED) || usersStatus.equals(User.STATUS_SUSPENDED)) {
                    User user = new User(
                            usersResult.getString("id"),
                            usersResult.getString("password"),
                            usersResult.getString("name"),
                            usersResult.getString("address"),
                            usersResult.getDate("dob"),
                            usersResult.getDate("dor"),
                            usersResult.getFloat("balance"),
                            usersStatus
                    );
                    userList.add(user);
                }
            } while (usersResult.next());
        } catch (SQLException ex) {
            request.setAttribute(ERROR_MESSAGE, "There was an issue retrieving the members.");
            Logger.getLogger(ListMembershipApplications.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Check if any appropriate members were found
        if (userList.isEmpty()) {
            request.setAttribute(ERROR_MESSAGE, "There are no approved/suspended members.");
        } // Save the list of users in the request
        else {
            request.setAttribute(USER_LIST, userList);
        }
        // Show the JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
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
        // Get the approved users ID
        String userID = request.getParameter(USER_ID);

        // Update the users status in the DB
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet userResult = dbf.get_from_table("users", userID);
        try {
            String newStatus;
            if (userResult.getString("status").equals(User.STATUS_APPROVED)) {
                newStatus = User.STATUS_SUSPENDED;
            } else {
                newStatus = User.STATUS_APPROVED;
            }

            User user = new User(
                    userResult.getString("id"),
                    userResult.getString("password"),
                    userResult.getString("name"),
                    userResult.getString("address"),
                    userResult.getDate("dob"),
                    userResult.getDate("dor"),
                    userResult.getFloat("balance"),
                    newStatus
            );

            boolean updateSucessful = dbf.update(user);
            if (!updateSucessful) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue altering the users membership status.");
            }

        } catch (SQLException ex) {
            request.setAttribute(ERROR_MESSAGE, "There was an issue altering the users membership status.");
            Logger.getLogger(SuspendResumeMembership.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Re-show the JSP (and re-fetch the updated data)
        doGet(request, response);
    }

}
