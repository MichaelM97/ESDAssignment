package servlet.auth;

import db.DatabaseFactory;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import utils.HashHelper;
import utils.SessionHelper;

public class ChangePassword extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "auth/change_password.jsp";

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

        User user = SessionHelper.getUser(request);
        String uid = user.getId();
        String password = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet userResult = dbf.get_from_table("users", "*");
        String hashedPassword = HashHelper.hashString(password);
        if (hashedPassword != null) {
            try {
                do {
                    try {
                        if (userResult.getString("id").equals(uid)) {
                            if (userResult.getString("password").equals(hashedPassword)) {
                                hashedPassword = HashHelper.hashString(newPassword);
                                user.setPassword(hashedPassword);
                                boolean updateSucessful = dbf.update(user);
                                if (!updateSucessful) {
                                    request.setAttribute(ERROR_MESSAGE, "Password not updated");
                                }
                                SessionHelper.setUser(request, user);
                                request.setAttribute(ERROR_MESSAGE, "Password updated");
                                response.setHeader("Refresh", "1;url=Dashboard");
                            } else {
                                request.setAttribute(ERROR_MESSAGE, "Incorrect password");
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while (userResult.next());
            } catch (SQLException ex) {
                Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            request.setAttribute(ERROR_MESSAGE, "Error with your password");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
