package servlet.other;

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

/**
 * Searches for a user on the ID field using the admins entry.
 */
public class AdminSearch extends HttpServlet {

    public static final String SEARCH_RESULTS = "searchResults";
    public static final String ERROR_MESSAGE = "errorMessage";

    private static final String JSP = "other/admin_search.jsp";

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
        String searchEntry = request.getParameter("adminSearchField");
        searchEntry = searchEntry.toLowerCase();

        ResultSet searchResults = new DatabaseFactory().user_search(searchEntry);

        if (searchResults == null) {
            request.setAttribute(ERROR_MESSAGE, "There was an issue searching for users.");
        } else {
            List<User> userList = new ArrayList<>();
            try {
                do {
                    // Prevent adding the ADMIN to the search results
                    if (!searchResults.getString("status").equals(User.ADMIN)) {
                        User user = new User(
                                searchResults.getString("id"),
                                searchResults.getString("password"),
                                searchResults.getString("name"),
                                searchResults.getString("address"),
                                searchResults.getDate("dob"),
                                searchResults.getDate("dor"),
                                searchResults.getFloat("balance"),
                                searchResults.getString("status")
                        );
                        userList.add(user);
                    }
                } while (searchResults.next());
                if (userList.isEmpty()) {
                    request.setAttribute(ERROR_MESSAGE, "There were no results found for that query.");
                } else {
                    request.setAttribute(SEARCH_RESULTS, userList);
                }
            } catch (SQLException ex) {
                request.setAttribute(ERROR_MESSAGE, "There was an issue searching for users.");
                Logger.getLogger(AdminSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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
