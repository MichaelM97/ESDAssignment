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
 * Filters out any requests that are not made by an ADMIN.
 */
public class AdminFilter implements Filter {

    public static final String ADMIN_FILTER_ERROR = "adminFilterError";
    private static final String HOME_JSP = "home.jsp";

    private FilterConfig filterConfig = null;

    public AdminFilter() {
    }

    /**
     * Checks if the request is from an authenticated admin user.
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

        // Check if the user is an admin/if there is a user at all
        if (currentUser == null || !currentUser.getStatus().equals(User.ADMIN)) {
            request.setAttribute(ADMIN_FILTER_ERROR, "Unauthorised attempt to access admin page.");
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
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }
}
