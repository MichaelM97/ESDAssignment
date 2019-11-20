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
    public static final String USERS_NAME = "usersName";
    public static final String USERS_STATUS = "usersStatus";
    private static String JSP = "dash/client_dash.jsp";
                       
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get user session information
        User user = SessionHelper.getUser(request);
        if (user == null) {
            // TODO: Home servlet to appropriately handle error message/log.  
            response.sendRedirect("./home.jsp");  
        } else {
            String uid = user.getId();
            String uStat = user.getStatus();
            if (uStat.equals("ADMIN")) {
                JSP = "dash/admin_dash.jsp";
            }
            request.setAttribute(USERS_NAME, uid); 
            request.setAttribute(USERS_STATUS, uStat); 
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
