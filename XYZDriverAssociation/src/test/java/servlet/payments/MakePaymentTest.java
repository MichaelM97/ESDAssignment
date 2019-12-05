package servlet.payments;

import db.DatabaseFactory;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Payment;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

public class MakePaymentTest extends Mockito {

    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher requestDispatcher;
    private static HttpSession session;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        session = mock(HttpSession.class);
    }

    @Test
    public void shouldInsertPaymentObjectWhenValidPost() throws Exception {
        // Given
        User user = new User(
                "m-mccormick",
                "password",
                "Michael",
                "50 The Meadows",
                new Date(),
                new Date(),
                100.0f,
                User.STATUS_PENDING
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getParameter("reference")).thenReturn("FEE");
        when(request.getParameter("amount")).thenReturn("50.0");

        // When
        new MakePayment().doPost(request, response);

        // Then
        verify(request).getParameter("description");
        verify(request).getParameter("amount");
        verify(request).setAttribute(eq("createdPayment"), any(Payment.class));
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }
}
