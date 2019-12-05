package servlet.balance;

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
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WithdrawFundsTest {

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
    public void shouldSetErrorWhenUserPending() throws IOException, ServletException {
        // Given
        User user = new User(
                "m-mccormick",
                "password",
                "Michael McCormick",
                "UWE",
                new Date(),
                new Date(),
                0.0f,
                User.STATUS_PENDING
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // When
        new WithdrawFunds().doGet(request, response);

        // Then
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }

    @Test
    public void shouldSetErrorWhenUserBalanceIs0() throws IOException, ServletException {
        // Given
        User user = new User(
                "m-mccormick",
                "password",
                "Michael McCormick",
                "UWE",
                new Date(),
                new Date(),
                0.0f,
                User.STATUS_APPROVED
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // When
        new WithdrawFunds().doGet(request, response);

        // Then
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }

    @Test
    public void shouldSetErrorWhenInsufficientFunds() throws IOException, ServletException {
        // Given
        User user = new User(
                "m-mccormick",
                "password",
                "Michael McCormick",
                "UWE",
                new Date(),
                new Date(),
                50.0f,
                User.STATUS_PENDING
        );
        new DatabaseFactory().insert(user);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getParameter("amount")).thenReturn("100.00");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // When
        new WithdrawFunds().doPost(request, response);

        // Then
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }

    @Test
    public void shouldWithdrawFromValidUsersBalance() throws IOException, ServletException {
        // Given
        User user = new User(
                "m-mccormick",
                "password",
                "Michael McCormick",
                "UWE",
                new Date(),
                new Date(),
                150.0f,
                User.STATUS_PENDING
        );
        new DatabaseFactory().insert(user);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("amount")).thenReturn("100.00");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // When
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        new WithdrawFunds().doPost(request, response);

        // Then
        verify(session).setAttribute(eq("user"), captor.capture());
        User captorUser = captor.getValue();
        assertTrue(captorUser.getBalance() == 50.00f);
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }
}
