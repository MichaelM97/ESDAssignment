package servlet.auth;

import db.DatabaseFactory;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegistrationTest {

    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static HttpSession session;
    private static RequestDispatcher requestDispatcher;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void shouldRegisterValidUser() throws IOException, ServletException {
        // Given
        when(request.getParameter("username")).thenReturn("m-mccormick");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("first_name")).thenReturn("Michael");
        when(request.getParameter("last_name")).thenReturn("McCormick");
        when(request.getParameter("address")).thenReturn("UWE");
        when(request.getParameter("dob")).thenReturn("1997-06-11");
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // When
        new Registration().doPost(request, response);

        // Then
        verify(session).setAttribute(eq("user"), any(User.class));
        verify(response).sendRedirect("ClientDashboard");
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }
}
