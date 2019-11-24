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
import utils.SessionHelper;

/**
 * Lists all membership payments, and allows the ADMIN to approve membership
 * applications.
 */
public class ListPayments extends HttpServlet {

    public static final String PAYMENT_LIST = "paymentList";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "payments/list_all_payments.jsp";

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
        User currentUser = SessionHelper.getUser(request);
        if (currentUser == null || !currentUser.getStatus().equals("ADMIN")) {
            // There is no current user or the user is not an admin
            // TODO: Display an error on the home screen explaining the redirect
            response.sendRedirect("./home.jsp");
        } else {
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
