package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.User;

/**
 * Helper class for interacting with the Session.
 */
public class SessionHelper {

    private static final String USER_ATT = "user";

    /**
     * Sets the user attribute using the passed User object. Creates a session
     * if one doesn't exist.
     *
     * @param request: The current HttpServletRequest, used to get the session
     * @param user: The User object to set in the session
     */
    public static void setUser(HttpServletRequest request, User user) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(USER_ATT, user);
    }

    /**
     * Gets the user attribute from the current session.
     *
     * @param request: The current HttpServletRequest, used to get the session
     * @return User, or null if the session has timed out.
     */
    public static User getUser(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return null;
        }
        return (User) httpSession.getAttribute(USER_ATT);
    }

}
