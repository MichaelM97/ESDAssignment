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
import model.Member;
import model.User;
import utils.SessionHelper;

/**
 * Lists all membership applications.
 */
public class ListMembershipApplications extends HttpServlet {

    public static final String MEMBERSHIP_APPLICATION_LIST = "membershipApplicationList";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "membership/list_membership_applications.jsp";

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
            ResultSet membersResult = dbf.get_from_table("members", "*");

            // Check if table has results
            if (membersResult == null) {
                request.setAttribute(ERROR_MESSAGE, "No membership applications have been filed yet");
            } else {
                // Loop through the results
                List<Member> memberList = new ArrayList<>();
                try {
                    do {
                        // Check if the members status is pending
                        if (membersResult.getString("status").equals(Member.STATUS_PENDING)) {
                            // Build the member object
                            Member member = new Member(
                                    membersResult.getString("id"),
                                    membersResult.getString("name"),
                                    membersResult.getString("address"),
                                    membersResult.getDate("dob"),
                                    membersResult.getDate("dor"),
                                    membersResult.getString("status"),
                                    membersResult.getFloat("balance")
                            );
                            // Add the claim to the list
                            memberList.add(member);
                        }
                    } while (membersResult.next());
                } catch (SQLException ex) {
                    request.setAttribute(ERROR_MESSAGE, "There was an issue retrieving the members");
                    Logger.getLogger(ListMembershipApplications.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Check if any pending members were found
                if (memberList.isEmpty()) {
                    request.setAttribute(ERROR_MESSAGE, "There are no new pending membership applications");
                } // Save the list of claims in the request
                else {
                    request.setAttribute(MEMBERSHIP_APPLICATION_LIST, memberList);
                }
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
