package servlet.dash;

import db.DatabaseFactory;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Claim;
import model.Payment;
import model.User;

/**
 * The dashboard for an admin.
 */
public class AdminDashboard extends HttpServlet {

    public static final String TURNOVER = "turnover";
    public static final String PAY_OUTS = "pay-outs";
    public static final String PROFIT = "profit";
    public static final String SUCCESS_MESSAGE = "successMessage";
    public static final String ERROR_MESSAGE = "errorMessage";

    private static final String JSP = "dash/admin_dash.jsp";

    private int numOfClaims = 0;

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
        // Calculate business metrics
        DatabaseFactory dbf = new DatabaseFactory();
        float turnover = getTurnover(dbf);
        float payouts = getPayouts(dbf);
        float profit = turnover - payouts;

        // Set the business metric fields and show the JSP
        request.setAttribute(TURNOVER, turnover);
        request.setAttribute(PAY_OUTS, payouts);
        request.setAttribute(PROFIT, profit);
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
        // Get total number of users
        DatabaseFactory dbf = new DatabaseFactory();
        List<User> approvedUsers = getApprovedUsers(dbf);
        int numOfUsers = approvedUsers.size();

        if (numOfUsers > 0) {
            // Calculate the annual fee
            float annualFee = numOfClaims / numOfUsers;

            for (User user : approvedUsers) {
                // Create a payment for the annual fee amount
                Payment payment = new Payment(
                        user.getId(),
                        Payment.ANNUAL_FEE,
                        annualFee,
                        new Date()
                );
                // Update the users balance
                user.setBalance(user.getBalance() - annualFee);
                // Submit the records into the DB
                dbf.insert(payment);
                dbf.update(user);
            }
            request.setAttribute(SUCCESS_MESSAGE, "All members charged £" + annualFee);
        } else {
            request.setAttribute(ERROR_MESSAGE, "There are currently no members to charge");
        }

        // Re-show the JSP
        doGet(request, response);
    }

    private float getTurnover(DatabaseFactory dbf) {
        float turnover = 0;
        ResultSet payments = dbf.get_from_table("payments", "*");
        if (payments != null) {
            try {
                do {
                    // Get user associated with the payment
                    ResultSet user = dbf.get_from_table("users", payments.getString("mem_id"));
                    if (user != null) {
                        // Check if user is approved
                        if (user.getString("status").equals(User.STATUS_APPROVED)) {
                            // Check if the payment is for the current year
                            LocalDate paymentDate = LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(payments.getDate("date").getTime()),
                                    ZoneId.systemDefault()
                            ).toLocalDate();
                            if (paymentDate.getYear() == LocalDate.now().getYear()) {
                                turnover += payments.getFloat("amount");
                            }
                        }
                    }
                } while (payments.next());
            } catch (SQLException ex) {
                Logger.getLogger(
                        AdminDashboard.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return turnover;
    }

    private float getPayouts(DatabaseFactory dbf) {
        float totalOutgoing = 0;
        ResultSet claims = dbf.get_from_table("claims", "*");
        if (claims != null) {
            try {
                do {
                    // Ensure claim is approved
                    if (claims.getString("status").equals(Claim.STATUS_APPROVED)) {
                        // Check if the claim is for the current year
                        LocalDate claimDate = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(claims.getDate("date").getTime()),
                                ZoneId.systemDefault()
                        ).toLocalDate();
                        if (claimDate.getYear() == LocalDate.now().getYear()) {
                            totalOutgoing += claims.getFloat("amount");
                        }
                        // Increment the count of claims field
                        numOfClaims++;
                    }
                } while (claims.next());
            } catch (SQLException ex) {
                Logger.getLogger(
                        AdminDashboard.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return totalOutgoing;
    }

    private List<User> getApprovedUsers(DatabaseFactory dbf) {
        List<User> users = new ArrayList<>();
        ResultSet usersReults = dbf.get_from_table("users", "*");
        if (usersReults != null) {
            try {
                do {
                    // Ensure user is approved
                    if (usersReults.getString("status").equals(User.STATUS_APPROVED)) {
                        // Build the user object and add them to the list
                        User user = new User(
                                usersReults.getString("id"),
                                usersReults.getString("password"),
                                usersReults.getString("name"),
                                usersReults.getString("address"),
                                usersReults.getDate("dob"),
                                usersReults.getDate("dor"),
                                usersReults.getFloat("balance"),
                                usersReults.getString("status")
                        );
                        users.add(user);
                    }
                } while (usersReults.next());
            } catch (SQLException ex) {
                Logger.getLogger(
                        AdminDashboard.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return users;
    }
}
