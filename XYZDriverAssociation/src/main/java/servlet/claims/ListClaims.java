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
import utils.SessionHelper;

/**
 * Lists all claims that the current user has filed.
 */
public class ListClaims extends HttpServlet {

    public static final String CLAIMS_LIST = "claimsList";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "claims/list_all_claims.jsp";

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
        // Get the current user from the session
        User user = SessionHelper.getUser(request);
        if (user == null || !user.getStatus().equals("ADMIN")) {
            // There is no current user or the user is not an admin
            // TODO: Display an error on the home screen explaining the redirect
            response.sendRedirect("./home.jsp");
        } else {
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
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
