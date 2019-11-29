package filter;

import static filter.AdminFilter.ADMIN_FILTER_ERROR;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminFilterTest {

    private static final String ADMIN_JSP = "dash/admin_dash.jsp";

    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static FilterChain chain;
    private static HttpSession session;
    private static RequestDispatcher requestDispatcher;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void shouldAllowWhenUserIsAdmin() throws IOException, ServletException {
        // Given
        User adminUser = new User(
                "",
                "",
                "",
                new Date(),
                new Date(),
                0.0f,
                User.ADMIN
        );
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestURI()).thenReturn(ADMIN_JSP);

        // When
        AdminFilter adminFilter = new AdminFilter();
        adminFilter.doFilter(request, response,
                chain);

        // Then
        verify(chain).doFilter(request, response);
    }

    @Test
    public void shouldBlockWhenUserIsPendingClient() throws IOException, ServletException {
        // Given
        User user = new User(
                "",
                "",
                "",
                new Date(),
                new Date(),
                0.0f,
                User.STATUS_PENDING
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestURI()).thenReturn(ADMIN_JSP);
        when(request.getRequestDispatcher("home.jsp")).thenReturn(requestDispatcher);

        // When
        AdminFilter adminFilter = new AdminFilter();
        adminFilter.doFilter(request, response,
                chain);

        // Then
        verify(request).setAttribute(eq(ADMIN_FILTER_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void shouldBlockWhenUserIsApprovedClient() throws IOException, ServletException {
        // Given
        User user = new User(
                "",
                "",
                "",
                new Date(),
                new Date(),
                0.0f,
                User.STATUS_APPROVED
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestURI()).thenReturn(ADMIN_JSP);
        when(request.getRequestDispatcher("home.jsp")).thenReturn(requestDispatcher);

        // When
        AdminFilter adminFilter = new AdminFilter();
        adminFilter.doFilter(request, response,
                chain);

        // Then
        verify(request).setAttribute(eq(ADMIN_FILTER_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void shouldBlockWhenUserIsNull() throws IOException, ServletException {
        // Given
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestURI()).thenReturn(ADMIN_JSP);
        when(request.getRequestDispatcher("home.jsp")).thenReturn(requestDispatcher);

        // When
        AdminFilter adminFilter = new AdminFilter();
        adminFilter.doFilter(request, response,
                chain);

        // Then
        verify(request).setAttribute(eq(ADMIN_FILTER_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}
