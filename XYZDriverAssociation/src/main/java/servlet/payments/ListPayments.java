package servlet.payments;

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
import model.Payment;
import model.User;

/**
 * Lists all membership payments, and allows the ADMIN to approve membership
 * applications.
 */
public class ListPayments extends HttpServlet {

    public static final String PAYMENT_LIST = "paymentList";
    public static final String PENDING_USERS_LIST = "pendingUsersList";
    public static final String APPROVED_USER_ID = "approvedUserID";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final int MEMBERSHIP_FEE = 50;
    private static final String JSP = "payments/list_all_payments.jsp";

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
        // Get all payments from the DB
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet paymentsResult = dbf.get_from_table("payments", "*");

        // Check if table has results
        if (paymentsResult == null) {
            request.setAttribute(ERROR_MESSAGE, "No payments have been made yet");
        } else {
            List<Payment> paymentList = new ArrayList<>();
            try {
                do {
                    // Build the user object
                    Payment payment = new Payment(
                            paymentsResult.getInt("id"),
                            paymentsResult.getString("mem_id"),
                            paymentsResult.getString("type"),
                            paymentsResult.getFloat("amount"),
                            paymentsResult.getDate("date")
                    );
                    // Add the user to the list
                    paymentList.add(payment);
                } while (paymentsResult.next());
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue retrieving the payments");
                Logger.getLogger(ListPayments.class.getName()).log(Level.SEVERE, null, ex);
            }

            request.setAttribute(PAYMENT_LIST, paymentList);
            List<String> pendingUserIDs = getListOfPendingUsersIDs(dbf);
            if (!pendingUserIDs.isEmpty()) {
                request.setAttribute(PENDING_USERS_LIST, pendingUserIDs);
            }
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
        String userID = request.getParameter(APPROVED_USER_ID);

        // Update the users status in the DB
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet userResult = dbf.get_from_table("users", "*");
        try {
            do {
                if (userResult.getString("id").equals(userID)) {
                    User user = new User(
                            userResult.getString("id"),
                            userResult.getString("password"),
                            userResult.getString("name"),
                            userResult.getString("address"),
                            userResult.getDate("dob"),
                            userResult.getDate("dor"),
                            userResult.getFloat("balance") - MEMBERSHIP_FEE,
                            User.STATUS_APPROVED
                    );
                    boolean updateSucessful = dbf.update(user);
                    if (!updateSucessful) {
                        request.setAttribute(ERROR_MESSAGE, "There was an issue approving the users membership. Please try again.");
                    }
                    break;
                }
            } while (userResult.next());
        } catch (SQLException ex) {
            request.setAttribute(ERROR_MESSAGE, "There was an issue approving the users membership. Please try again.");
            Logger.getLogger(ListPayments.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Re-show the JSP (and re-fetch the updated data)
        doGet(request, response);
    }

    /**
     * Fetches a list of all Users in the DB that have a status of PENDING.
     *
     * @param dbf Instance of DatabaseFactory
     * @return The list of users, empty if no users were found/an error occurred
     */
    List<String> getListOfPendingUsersIDs(DatabaseFactory dbf) {
        List<String> userIDList = new ArrayList<>();

        // Get all users from the DB
        ResultSet usersResult = dbf.get_from_table("users", "*");

        // Loop through the results and pull out any PENDING users
        if (usersResult != null) {
            try {
                do {
                    // Check if the members status is PENDING
                    if (usersResult.getString("status").equals(User.STATUS_PENDING)) {
                        // Add the users ID to the list
                        userIDList.add(usersResult.getString("id"));
                    }
                } while (usersResult.next());
            } catch (SQLException ex) {
                Logger.getLogger(ListPayments.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<>();
            }
        }

        return userIDList;
    }
}
