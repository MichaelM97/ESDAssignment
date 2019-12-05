package servlet.auth;

import db.DatabaseFactory;
import java.io.IOException;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import utils.HashHelper;

public class LoginTest {

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
    public void shouldLoginWhenValidUser() throws IOException, ServletException {
        // Given
        String username = "m-mccormick";
        String password = "password";
        String hashedPassword = HashHelper.hashString(password);
        User user = new User(
                username,
                hashedPassword,
                "Michael McCormick",
                "UWE",
                new Date(),
                new Date(),
                0.0f,
                User.STATUS_PENDING
        );
        new DatabaseFactory().insert(user);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // When
        new Login().doPost(request, response);

        // Then
        verify(response).sendRedirect("ClientDashboard");
    }
    
     @Test
    public void shouldLoginWhenValidAdmin() throws IOException, ServletException {
        // Given
        String username = "admin";
        String password = "password";
        String hashedPassword = HashHelper.hashString(password);
        User user = new User(
                username,
                hashedPassword,
                "",
                "",
                new Date(),
                new Date(),
                0.0f,
                User.ADMIN
        );
        new DatabaseFactory().insert(user);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // When
        new Login().doPost(request, response);

        // Then
        verify(response).sendRedirect("AdminDashboard");
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }
}
