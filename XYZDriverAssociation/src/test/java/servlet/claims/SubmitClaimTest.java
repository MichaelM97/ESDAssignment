package servlet.claims;

import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Claim;
import model.User;
import org.junit.Test;
import org.mockito.Mockito;

public class SubmitClaimTest extends Mockito {

    @Test
    public void shouldRedirectToHomeWhenNullUser() throws Exception {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getSession(false)).thenReturn(session);

        // When
        new SubmitClaim().doPost(request, response);

        // Then
        verify(session, times(1)).getAttribute("user");
        verify(request, times(1)).getSession(false);
        verify(response, times(1)).sendRedirect("./home.jsp");
    }

    @Test
    public void shouldInsertClaimObjectWhenValidPost() throws Exception {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        User user = new User(
                "MichaelM12",
                "password",
                "Michael McCormick",
                "50 The Meadows",
                new Date(),
                new Date(),
                0.0f,
                User.STATUS_APPROVED
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher("claims/submit_claim.jsp")).thenReturn(dispatcher);
        when(request.getParameter("description")).thenReturn("I crashed my car.");
        when(request.getParameter("amount")).thenReturn("99.99");

        // When
        new SubmitClaim().doPost(request, response);

        // Then
        verify(session, times(1)).getAttribute("user");
        verify(request, times(1)).getSession(false);
        verify(request, times(1)).getParameter("description");
        verify(request, times(1)).getParameter("amount");
        verify(request, times(1)).setAttribute(eq("createdClaim"), any(Claim.class));
    }
}
