package servlet.claims;

import db.DatabaseFactory;
import java.io.IOException;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Claim;
import model.User;
import utils.SessionHelper;

/**
 * The Servlet which allows clients to submit a new claim.
 */
public class SubmitClaim extends HttpServlet {

    public static final String CREATED_CLAIM = "createdClaim";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "claims/submit_claim.jsp";

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
        RequestDispatcher view = request.getRequestDispatcher(JSP);
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
        // Get the current user from the session
        User user = SessionHelper.getUser(request);
        if (user == null) {
            // TODO: Display an error on the home screen explaining the redirect
            response.sendRedirect("./home.jsp");
        } else {
            // Fetch all required fields for the claim
            String userID = user.getId();
            Date date = new Date();
            String enteredDescription = (String) request.getParameter("description");
            String status = "PENDING";
            float enteredAmount = Float.parseFloat(request.getParameter("amount"));

            // Build the Claim object            
            Claim claim = new Claim(userID, date, enteredDescription, status, enteredAmount);

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
    }

}
