package servlet.payments;

import db.DatabaseFactory;
import java.io.IOException;
import java.util.Date;
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

        boolean insertSuccessful = new DatabaseFactory().insert(payment);

        if (insertSuccessful) {
            request.setAttribute(CREATED_PAYMENT, payment);
        } else {
            request.setAttribute(ERROR_MESSAGE, "Failed to process payment. Please try again.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
