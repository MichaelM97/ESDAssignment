package servlet.claims;

import db.DatabaseFactory;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Claim;
import model.User;
import servlet.dash.ClientDashboard;
import utils.SessionHelper;

/**
 * The Servlet which allows clients to submit a new claim.
 */
public class SubmitClaim extends HttpServlet {

    public static final String CREATED_CLAIM = "createdClaim";
    public static final String ERROR_MESSAGE = "errorMessage";

    private static final String JSP = "claims/submit_claim.jsp";
    private static final String CLIENT_DASH_JSP = "dash/client_dash.jsp";

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
        String getJSP = JSP;

        // Check if user is eligible to make a claim
        User currentUser = SessionHelper.getUser(request);
        if (currentUser.getStatus().equals(User.STATUS_PENDING)) {
            getJSP = CLIENT_DASH_JSP;
            request.setAttribute(ClientDashboard.ERROR_MESSAGE, "You must be an approved member before submitting a claim.");
        } else if (userHasNotBeenRegisteredFor6Months(currentUser)) {
            getJSP = CLIENT_DASH_JSP;
            request.setAttribute(ClientDashboard.ERROR_MESSAGE, "You can only make a claim if you have been a member for 6 months.");
        } else if (userHasMadeMaxClaims(currentUser)) {
            getJSP = CLIENT_DASH_JSP;
            request.setAttribute(ClientDashboard.ERROR_MESSAGE, "You have already made the maximum number of claims for this year (2).");
        }
        RequestDispatcher view = request.getRequestDispatcher(getJSP);
        view.forward(request, response);
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
        // Build the claim
        User currentUser = SessionHelper.getUser(request);
        Claim claim = new Claim(
                currentUser.getId(),
                new Date(),
                request.getParameter("description"),
                Claim.STATUS_PENDING,
                Float.parseFloat(request.getParameter("amount"))
        );

        // Add the claim to the DB
        DatabaseFactory dbf = new DatabaseFactory();
        boolean insertSuccessful = dbf.insert(claim);

        if (insertSuccessful) {
            request.setAttribute(CREATED_CLAIM, claim);
        } else {
            request.setAttribute(ERROR_MESSAGE, "Failed to create claim. Please try again.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }

    private boolean userHasNotBeenRegisteredFor6Months(User user) {
        Calendar date6MonthsAgo = Calendar.getInstance();
        date6MonthsAgo.add(Calendar.MONTH, -6);
        return user.getDor().after(date6MonthsAgo.getTime());
    }

    private boolean userHasMadeMaxClaims(User user) {
        // Get the claims from the DB
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet claimsResult = dbf.get_from_table("claims", "*");
        int claimCount = 0;
        if (claimsResult == null) {
            return false;
        } else {
            Calendar date1YearAgo = Calendar.getInstance();
            date1YearAgo.add(Calendar.YEAR, -1);
            try {
                do {
                    try {
                        if (claimsResult.getString("mem_id").equals(user.getId())) {
                            // Check if claim is from the past year
                            Date claimDate = claimsResult.getDate("date");
                            if (claimDate.after(date1YearAgo.getTime())) {
                                claimCount++;
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(SubmitClaim.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while (claimsResult.next());
            } catch (SQLException ex) {
                Logger.getLogger(SubmitClaim.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return claimCount >= 2;
    }
}
