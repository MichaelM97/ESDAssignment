package servlet.Members;

import db.DatabaseFactory;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import utils.SessionHelper;

public class ListAllMembers extends HttpServlet {

    public static final String USERS_LIST = "usersList";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String JSP = "dash/admin_list_members.jsp";

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

        User currentUser = SessionHelper.getUser(request);
        if (currentUser == null || !currentUser.getStatus().equals("ADMIN")) {
            response.sendRedirect("./home.jsp");
        } else {
            ResultSet usersResult = new DatabaseFactory().get_from_table("users", "*");

            if (usersResult == null) {
                request.setAttribute(ERROR_MESSAGE, "No users have been registered yet");
            } else {
                // Collect all users
                List<User> userList = new ArrayList<>();
                try {
                    do {
                        User user = new User(
                                usersResult.getString("id"),
                                usersResult.getString("password"),
                                usersResult.getString("status")
                        );
                        userList.add(user);
                    } while (usersResult.next());
                } catch (SQLException ex) {
                    request.setAttribute(ERROR_MESSAGE, "There was an issue retrieving the members");
                    Logger.getLogger(ListAllMembers.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Save the list of users in the request
                request.setAttribute(USERS_LIST, userList);
            }
            // Show the JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
            dispatcher.forward(request, response);

        }
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for allowing admins to list all members";
    }

}
