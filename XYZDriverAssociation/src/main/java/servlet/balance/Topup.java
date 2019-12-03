package servlet.balance;

import db.DatabaseFactory;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import utils.SessionHelper;

/**
 * Allows the user to top-up their balance.
 */
public class Topup extends HttpServlet {

    public static final String SUCCESS_MESSAGE = "successMessage";
    public static final String ERROR_MESSAGE = "errorMessage";

    private static final String JSP = "balance/topup.jsp";

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
        // Get the current user and their entered params
        User user = SessionHelper.getUser(request);
        float amount = Float.parseFloat(request.getParameter("amount"));

        // Update the users balance in the DB
        user.setBalance(user.getBalance() + amount);
        if (!new DatabaseFactory().update(user)) {
            request.setAttribute(ERROR_MESSAGE, "There was an issue updating your balance.");
        } else {
            request.setAttribute(SUCCESS_MESSAGE, "Balance successfully topped up by £" + amount);
            SessionHelper.setUser(request, user);
        }

        // Re-show the JSP
        doGet(request, response);
    }
}
