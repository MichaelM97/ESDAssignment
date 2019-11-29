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

/**
 * Lists all APPROVED members.
 */
public class ListAllMembers extends HttpServlet {

    public static final String USER_LIST = "userList";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "membership/admin_list_members.jsp";

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
        // Get all users from the DB
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet usersResult = dbf.get_from_table("users", "*");

        // Check if table has results
        if (usersResult == null) {
            request.setAttribute(ERROR_MESSAGE, "There are no APPROVED members yet");
        } else {
            // Loop through the results and pull out any APPROVED users
            List<User> userList = new ArrayList<>();
            try {
                do {
                    // Check if the members status is APPROVED
                    if (usersResult.getString("status").equals(User.STATUS_APPROVED)) {
                        // Build the user object
                        User user = new User(
                                usersResult.getString("id"),
                                usersResult.getString("name"),
                                usersResult.getString("address"),
                                usersResult.getDate("dob"),
                                usersResult.getDate("dor"),
                                usersResult.getFloat("balance"),
                                usersResult.getString("status")
                        );
                        // Add the user to the list
                        userList.add(user);
                    }
                } while (usersResult.next());
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue retrieving the members");
                Logger.getLogger(ListMembershipApplications.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Check if any APPROVED members were found
            if (userList.isEmpty()) {
                request.setAttribute(ERROR_MESSAGE, "There are no APPROVED members");
            } // Save the list of users in the request
            else {
                request.setAttribute(USER_LIST, userList);
            }
        }

        // Show the JSP
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
