package servlet.dash;

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

public class Dashboard extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";
    private static String JSP = "dash/client_dash.jsp";
                       
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get user session information
        User user = SessionHelper.getUser(request);
        if (user == null) {
            // error in home jsp, no user found.
            response.sendRedirect("./home.jsp");  
           
        } else {
            
            String uid = user.getId();
            String ustat = user.getStatus();
            if (ustat == "ADMIN") {
                JSP = "dash/admin_dash.jsp";
            }
            request.setAttribute("usersName", uid); 
            RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
            dispatcher.forward(request, response);     
        }    
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                
        RequestDispatcher dispatcher = request.getRequestDispatcher(JSP);
        dispatcher.forward(request, response);
    }
}
