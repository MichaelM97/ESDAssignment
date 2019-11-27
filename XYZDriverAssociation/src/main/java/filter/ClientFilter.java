package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import model.User;
import utils.SessionHelper;

/**
 * Filters out any requests that are not made by an authenticated client.
 */
public class ClientFilter implements Filter {

    public static final String CLIENT_FILTER_ERROR = "clientFilterError";
    private static final String HOME_JSP = "home.jsp";

    private FilterConfig filterConfig = null;

    public ClientFilter() {
    }

    /**
     * Checks if the request is from an authenticated user.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        // Get the current user from the session
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        User currentUser = SessionHelper.getUser(httpRequest);

        // Check if there is a user and that the user is not an admin
        if (currentUser == null || currentUser.getStatus().equals(User.ADMIN)) {
            request.setAttribute(CLIENT_FILTER_ERROR, "Unauthorised attempt to access XYZDriversAssociation.");
            RequestDispatcher view = request.getRequestDispatcher(HOME_JSP);
            view.forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Initialises this filter.
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter.
     */
    @Override
    public void destroy() {
    }
}
