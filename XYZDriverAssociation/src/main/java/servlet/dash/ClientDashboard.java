

package servlet.dash;

import db.DatabaseFactory;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import utils.SessionHelper;

/**
 * The dashboard for a client.
 */
public class ClientDashboard extends HttpServlet {

    public static final String INFO_MESSAGE = "infoMessage";
    public static final String USER_OBJECT_ATT = "userObject";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "dash/client_dash.jsp";

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
        // Get user info
        User user = SessionHelper.getUser(request);
        String userStatus = user.getStatus();

        // Check if users membership status is pending
        if (userStatus.equals(User.STATUS_PENDING)) {
            request.setAttribute(INFO_MESSAGE, "<br>Please submit your first payment so that we can approve your membership.<br>");
        }

        request.setAttribute(USER_OBJECT_ATT, user);
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }

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

        User user = SessionHelper.getUser(request);
        String userID = user.getId();
        String userStatus = user.getStatus();
        boolean paymentFound = false;
        ResultSet paymentsResult = new DatabaseFactory().get_from_table("payments", "*");

        if (userStatus.equals(User.STATUS_APPROVED)) {
            try {
                do {
                    if (paymentsResult.getString("mem_id").equals(userID)) {
                    // This is a payment for the user

                        Calendar date12MonthsAgo = Calendar.getInstance();
                        date12MonthsAgo.add(Calendar.MONTH, -12);
                        if (paymentsResult.getDate("date").after(date12MonthsAgo.getTime())) {
                            paymentFound = true;
                            break;
                        }
                    }
                } while (paymentsResult.next());
            } catch (SQLException ex) {
                Logger.getLogger(ClientDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (!paymentFound) {
                user.setStatus(User.STATUS_SUSPENDED);
                boolean updateSucessful = new DatabaseFactory().update(user);
                if (!updateSucessful) {
                    Logger.getLogger(ClientDashboard.class.getName()).log(Level.SEVERE, null, "Error updating this users status");
                }
            }

        }
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
}
