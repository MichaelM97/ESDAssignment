package servlet.claims;

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
import model.User;

/**
 * Lists all claims that the current user has filed.
 */
public class ListClaims extends HttpServlet {

    public static final String CLAIMS_LIST = "claimsList";
    public static final String ERROR_MESSAGE = "errorMessage";

    private static final String JSP = "claims/list_all_claims.jsp";

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
        // Get the claims from the DB
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet claimsResult = dbf.get_from_table("claims", "*");
        // Check if table has results
        if (claimsResult == null) {
            request.setAttribute(ERROR_MESSAGE, "No claims have been filed yet");
        } else {

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
                    // Add the claim to the list
                    claimList.add(claim);
                } while (claimsResult.next());
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue retrieving the claims");
                Logger.getLogger(ListClaims.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Save the list of claims in the request
            request.setAttribute(CLAIMS_LIST, claimList);
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
        // Get the updated claims ID
        String claimID = request.getParameter("claimID");

        // Update the claims status in the DB
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet claimResult = dbf.get_from_table("claims", claimID);
        try {
            String newStatus;
            if (request.getParameter("submitStatus").equals("Approve")) {
                newStatus = Claim.STATUS_APPROVED;
            } else {
                newStatus = Claim.STATUS_REJECTED;
            }
            Claim claim = new Claim(
                    claimResult.getInt("id"),
                    claimResult.getString("mem_id"),
                    claimResult.getDate("date"),
                    claimResult.getString("description"),
                    newStatus,
                    claimResult.getFloat("amount")
            );
            boolean updateSucessful = dbf.update(claim);
            if (!updateSucessful) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue approving the claim. Please try again.");
            } else {
                // Add the claim amount to the users balance (if approved)
                if (newStatus.equals(Claim.STATUS_APPROVED)) {
                    ResultSet userResult = dbf.get_from_table("users", claim.getMem_id());
                    User user = new User(
                            userResult.getString("id"),
                            userResult.getString("password"),
                            userResult.getString("name"),
                            userResult.getString("address"),
                            userResult.getDate("dob"),
                            userResult.getDate("dor"),
                            userResult.getFloat("balance") + claim.getAmount(),
                            userResult.getString("status")
                    );
                    if (!dbf.update(user)) {
                        request.setAttribute(ERROR_MESSAGE, "Funds not allocated to user");
                    }
                }
            }
        } catch (SQLException ex) {
            request.setAttribute(ERROR_MESSAGE, "There was an issue approving the claim. Please try again.");
            Logger.getLogger(ListClaims.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Re-show the JSP (and re-fetch the updated data)
        doGet(request, response);
    }
}
