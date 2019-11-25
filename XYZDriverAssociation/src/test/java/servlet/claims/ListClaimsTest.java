package servlet.claims;

import db.DatabaseFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Claim;
import model.User;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

public class ListClaimsTest {

    @Test
    public void shouldRedirectToHomeWhenNullUser() throws Exception {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getSession(false)).thenReturn(session);

        // When
        new ListClaims().processRequest(request, response);

        // Then
        verify(session, times(1)).getAttribute("user");
        verify(request, times(1)).getSession(false);
        verify(response, times(1)).sendRedirect("./home.jsp");
    }

    @Test
    public void shouldRedirectToHomeWhenUserNotAdmin() throws Exception {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User(
                "MichaelM12",
                "password",
                "Michael McCormick",
                "50 The Meadows",
                new Date(),
                new Date(),
                0.0f,
                User.STATUS_PENDING
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);

        // When
        new ListClaims().processRequest(request, response);

        // Then
        verify(session, times(1)).getAttribute("user");
        verify(request, times(1)).getSession(false);
        verify(response, times(1)).sendRedirect("./home.jsp");
    }

    @Test
    public void shouldSetErrorWhenNoClaims() throws Exception {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        DatabaseFactory dbf = new DatabaseFactory();
        dbf.reset_db();
        User user = new User(
                "admin",
                "password",
                "",
                "",
                new Date(),
                new Date(),
                0.0f,
                User.ADMIN
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher("claims/list_all_claims.jsp")).thenReturn(dispatcher);

        // When
        new ListClaims().processRequest(request, response);

        // Then
        verify(session, times(1)).getAttribute("user");
        verify(request, times(1)).getSession(false);
        verify(request, times(1)).setAttribute("errorMessage", "No claims have been filed yet");
    }

    @Test
    public void shouldListClaimsWhenValidRequest() throws Exception {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        User user = new User(
                "admin",
                "password",
                "",
                "",
                new Date(),
                new Date(),
                0.0f,
                User.ADMIN
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher("claims/list_all_claims.jsp")).thenReturn(dispatcher);
        List<Claim> claims = new ArrayList<>();
        claims.add(new Claim(1, "Michael12", new Date(), "My car exploded", "Approved", 250.55f));
        claims.add(new Claim(5, "Dom99", new Date(), "Crashed my Clio", "PENDING", 12.99f));
        claims.add(new Claim(7, "Jake69", new Date(), "I dont even have a car", "PENDING", 10000f));
        claims.add(new Claim(8, "Alex22", new Date(), "I skrr skrrrd too hard", "PENDING", 199.99f));
        claims.add(new Claim(9, "TinWahCaseyCheung", new Date(), "Crashed air force tin", "PENDING", 100000f));
        DatabaseFactory dbf = new DatabaseFactory();
        dbf.reset_db();
        for (Claim claim : claims) {
            dbf.insert(claim);
        }

        // When
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        new ListClaims().processRequest(request, response);

        // Then
        verify(session, times(1)).getAttribute("user");
        verify(request, times(1)).getSession(false);
        verify(request, times(1)).setAttribute(eq("claimsList"), captor.capture());
        assertTrue(new ReflectionEquals(claims).matches(captor.getValue()));
    }
}
