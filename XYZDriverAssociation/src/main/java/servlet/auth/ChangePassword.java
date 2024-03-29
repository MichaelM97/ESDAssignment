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
    public static final String INFO_MESSAGE = "infoMessage";
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
        // Get user and the new password
        User user = SessionHelper.getUser(request);
        String password = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");

        // Update the users password in the DB
        DatabaseFactory dbf = new DatabaseFactory();
        ResultSet userResult = dbf.get_from_table("users", user.getId());
        String hashedPassword = HashHelper.hashString(password);
        if (hashedPassword != null) {
            try {
                if (userResult.getString("password").equals(hashedPassword)) {
                    hashedPassword = HashHelper.hashString(newPassword);
                    user.setPassword(hashedPassword);
                    if (!dbf.update(user)) {
                        request.setAttribute(ERROR_MESSAGE, "There was an error updating your password.");
                    }
                    SessionHelper.setUser(request, user);
                    request.setAttribute(INFO_MESSAGE, "Password changed!");
                } else {
                    request.setAttribute(ERROR_MESSAGE, "Incorrect password.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute(ERROR_MESSAGE, "There was an error updating your password.");
            }
        } else {
            request.setAttribute(ERROR_MESSAGE, "There was an error with your password.");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
