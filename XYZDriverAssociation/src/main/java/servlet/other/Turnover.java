package servlet.other;

import java.io.IOException;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import model.User;
import db.DatabaseFactory;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Turnover extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String TURNOVER = "turnover";
    public static final String OUTGOING = "outgoing";
    public static final String JSP = "other/turnover.jsp";

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
        float turnover = 0;
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet payments = dbf.get_from_table("payments", "*");
        if (payments != null) {
            try {
                do {
                    //Check user is approved
                    ResultSet user = dbf.get_from_table("users", payments.getString("mem_id"));
                    if (user != null) {
                        if (user.getString("status").equals(User.STATUS_APPROVED)) {
                            LocalDate paymentDate = LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(payments.getDate("date").getTime()),
                                    ZoneId.systemDefault()
                            ).toLocalDate();
                            // Ensure it's for this year.
                            if (paymentDate.getYear() == LocalDate.now().getYear()) {
                                turnover += payments.getFloat("amount");
                            }
                        }
                    }
                } while (payments.next());
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE,
                        "Error retrieving payments.");
                Logger.getLogger(
                        Turnover.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }

        float outgoing = 0;
        ResultSet claims = dbf.get_from_table("claims", "*");
        if (claims != null) {
            try {
                do {
                    //Check user is approved
                    ResultSet user = dbf.get_from_table("users", claims.getString("mem_id"));
                    if (user != null) {
                        if (user.getString("status").equals(User.STATUS_APPROVED)) {
                            LocalDate claimDate = LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(claims.getDate("date").getTime()),
                                    ZoneId.systemDefault()
                            ).toLocalDate();
                            // Ensure it's for this year.
                            if (claimDate.getYear() == LocalDate.now().getYear()) {
                                outgoing += claims.getFloat("amount");
                            }
                        }
                    }
                } while (claims.next());
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE,
                        "Error retrieving claims.");
                Logger.getLogger(
                        Turnover.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }

        request.setAttribute(TURNOVER, turnover);
        request.setAttribute(OUTGOING, outgoing);
        RequestDispatcher view = request.getRequestDispatcher(JSP);
        view.forward(request, response);
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
