package servlet.other;

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
import model.Claim;
import model.Payment;
import utils.SessionHelper;

public class UserHistory extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String PAYMENT_LIST = "paymentList";
    public static final String CLAIMS_LIST = "claimList";

    private static final String JSP = "other/user_history.jsp";

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
        ResultSet paymentsResult = new DatabaseFactory().get_from_table("payments", "*");
        // Check if table has results
        if (paymentsResult != null) {
            List<Payment> paymentList = new ArrayList<>();
            try {
                do {
                    // Build the payment object
                    Payment payment = new Payment(
                            paymentsResult.getInt("id"),
                            paymentsResult.getString("mem_id"),
                            paymentsResult.getString("type"),
                            paymentsResult.getFloat("amount"),
                            paymentsResult.getDate("date")
                    );
                    // only add payments from current user 
                    if (payment.getMem_id().equals(SessionHelper.getUser(request).getId())) {
                        paymentList.add(payment);
                    }
                } while (paymentsResult.next());
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue retrieving the information");
                Logger.getLogger(UserHistory.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.setAttribute(PAYMENT_LIST, paymentList);
        }

        ResultSet claimsResult = new DatabaseFactory().get_from_table("claims", "*");
        if (claimsResult != null) {
            // Loop through and filter out the users claims
            List<Claim> claimList = new ArrayList<>();
            try {
                do {
                    // Build the claim object
                    Claim claim = new Claim(
                            claimsResult.getInt("id"),
                            claimsResult.getString("mem_id"),
                            claimsResult.getDate("date"),
                            claimsResult.getString("description"),
                            claimsResult.getString("status"),
                            claimsResult.getFloat("amount")
                    );
                    // only add claims from current user 
                    if (claim.getMem_id().equals(SessionHelper.getUser(request).getId())) {
                        claimList.add(claim);
                    }

                } while (claimsResult.next());
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue retrieving the information");
                Logger.getLogger(UserHistory.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Save the list of claims in the request
            request.setAttribute(CLAIMS_LIST, claimList);
        }
        // Show the JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
