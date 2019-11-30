package servlet.withdraw;

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
import servlet.dash.ClientDashboard;
import utils.SessionHelper;

public class WithdrawFunds extends HttpServlet {

    private static final String JSP = "withdraw/client_withdraw_funds.jsp";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String MADE_WITHDRAWAL = "madeWithdrawal";
    private static final String CLIENT_DASH_JSP = "dash/client_dash.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String getJSP = JSP;
        // Check if user is eligible to make a claim
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

        User user = SessionHelper.getUser(request);
        float amount = Float.parseFloat(request.getParameter("amount"));
        if (amount > user.getBalance()) {
            request.setAttribute(ERROR_MESSAGE, "Insufficient funds.");
        } else {
            DatabaseFactory dbf = new DatabaseFactory();
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
                            userResult.getFloat("balance") - amount,
                            userResult.getString("status")
                    );
                    if (!dbf.update(user)) {
                        request.setAttribute(ERROR_MESSAGE, "User balance not updated in users table");
                    } else {
                        request.setAttribute(MADE_WITHDRAWAL, "true");
                    }
                } catch (SQLException ex) {
                    request.setAttribute(ERROR_MESSAGE, "There was an issue approving the withdrawal. Please try again.");
                    Logger.getLogger(WithdrawFunds.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                request.setAttribute(ERROR_MESSAGE, "User not registered");
            }
        }
        SessionHelper.setUser(request, user);
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
