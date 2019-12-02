package servlet.payments;

import db.DatabaseFactory;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
 * Servlet which allows user to make a payment.
 */
public class MakePayment extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String CREATED_PAYMENT = "createdPayment";

    private static final String JSP = "payments/client_make_payment.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get user object and field inputs from the JSP
        User user = SessionHelper.getUser(request);
        String reference = request.getParameter("reference");
        float amount = Float.parseFloat(request.getParameter("amount"));
        Payment payment = new Payment(
                user.getId(),
                reference,
                amount,
                new Date()
        );

        // Check if the user has enouch balance to make the payment
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet userResult = dbf.get_from_table("users", user.getId());
        try {
            if (userResult.getFloat("balance") < amount) {
                request.setAttribute(ERROR_MESSAGE, "You do not have enough balance to make this payment, please top up your account first.");
            } else {
                // Insert the payment into the DB
                boolean insertSuccessful = dbf.insert(payment);
                if (insertSuccessful) {
                    request.setAttribute(CREATED_PAYMENT, payment);
                } else {
                    request.setAttribute(ERROR_MESSAGE, "Failed to process payment. Please try again.");
                }

                // Subtract the payment amount from the users balance
                User updatedUser = new User(
                        userResult.getString("id"),
                        userResult.getString("password"),
                        userResult.getString("name"),
                        userResult.getString("address"),
                        userResult.getDate("dob"),
                        userResult.getDate("dor"),
                        userResult.getFloat("balance") - amount,
                        userResult.getString("status")
                );
                if (!dbf.update(updatedUser)) {
                    request.setAttribute(ERROR_MESSAGE, "User balance not updated in users table");
                }
                SessionHelper.setUser(request, updatedUser);
            }
        } catch (SQLException ex) {
            request.setAttribute(ERROR_MESSAGE, "Failed to make payment.");
            Logger.getLogger(MakePayment.class.getName()).log(Level.SEVERE, null, ex);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
