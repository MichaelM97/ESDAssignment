package servlet.balance;

import db.DatabaseFactory;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import servlet.dash.ClientDashboard;
import utils.SessionHelper;

/**
 * Servlet which allows the user to withdraw from their balance.
 */
public class WithdrawFunds extends HttpServlet {

    public static final String SUCCESS_MESSAGE = "successMessage";
    public static final String ERROR_MESSAGE = "errorMessage";

    private static final String JSP = "balance/client_withdraw_funds.jsp";
    private static final String CLIENT_DASH_JSP = "dash/client_dash.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String getJSP = JSP;
        // Check if user is eligible to make a withdrawal
        User currentUser = SessionHelper.getUser(request);
        if (currentUser.getStatus().equals(User.STATUS_PENDING)) {
            getJSP = CLIENT_DASH_JSP;
            request.setAttribute(ClientDashboard.ERROR_MESSAGE, "You must be an approved member before making a withdrawal.");
        } else if (currentUser.getBalance() <= 0.0) {
            getJSP = CLIENT_DASH_JSP;
            request.setAttribute(ClientDashboard.ERROR_MESSAGE, "No funds to withdraw.");
        }
        RequestDispatcher view = request.getRequestDispatcher(getJSP);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the current user and their entered params
        User user = SessionHelper.getUser(request);
        float amount = Float.parseFloat(request.getParameter("amount"));

        if (amount > user.getBalance()) {
            request.setAttribute(ERROR_MESSAGE, "Insufficient funds.");
        } else {
            // Update the users balance in the DB
            DatabaseFactory dbf = new DatabaseFactory();
            user.setBalance(user.getBalance() - amount);
            if (!dbf.update(user)) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue placing your withdrawal.");
            } else {
                request.setAttribute(SUCCESS_MESSAGE, "£" + amount + " successfully withdrawn.");
                SessionHelper.setUser(request, user);
            }
        }

        // Re-show the JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
