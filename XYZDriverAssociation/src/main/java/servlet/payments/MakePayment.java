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

    private static final String JSP = "payments/client_make_payment.jsp";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String CREATED_PAYMENT = "createdPayment";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = SessionHelper.getUser(request);
        float amount = Float.parseFloat(request.getParameter("amount"));
        Payment payment = new Payment(
                user.getId(),
                "FEE",
                amount,
                new Date()
        );
        DatabaseFactory dbf = new DatabaseFactory();
        boolean insertSuccessful = dbf.insert(payment);
        if (insertSuccessful) {
            request.setAttribute(CREATED_PAYMENT, payment);
        } else {
            request.setAttribute(ERROR_MESSAGE, "Failed to process payment. Please try again.");
        }

        ResultSet userResult = dbf.get_from_table("users", user.getId());
        if (userResult != null) {
            try {
                user = new User(
                        userResult.getString("id"),
                        userResult.getString("password"),
                        userResult.getString("name"),
                        userResult.getString("address"),
                        userResult.getDate("dob"),
                        userResult.getDate("dor"),
                        userResult.getFloat("balance") + amount,
                        userResult.getString("status")
                );
                if (!dbf.update(user)) {
                    request.setAttribute(ERROR_MESSAGE, "User balance not updated in users table");
                }
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue approving the claim. Please try again.");
                Logger.getLogger(MakePayment.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            request.setAttribute(ERROR_MESSAGE, "User not registered");
        }
        SessionHelper.setUser(request, user);
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
