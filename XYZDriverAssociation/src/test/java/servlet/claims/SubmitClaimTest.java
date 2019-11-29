package servlet.claims;

import db.DatabaseFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Claim;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import servlet.dash.ClientDashboard;

public class SubmitClaimTest extends Mockito {

    private static final String JSP = "claims/submit_claim.jsp";
    private static final String CLIENT_DASH_JSP = "dash/client_dash.jsp";

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
    public void shouldBlockUserWhenAccountIsLessThan6MonthsOld() throws Exception {
        // Given
        User user = new User(
                "Mike123",
                "Michael",
                "50 The Meadows",
                new Date(),
                new Date(),
                100.0f,
                User.STATUS_PENDING
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(CLIENT_DASH_JSP)).thenReturn(requestDispatcher);

        // When
        new SubmitClaim().doGet(request, response);

        // Then
        verify(request).setAttribute(eq(ClientDashboard.ERROR_MESSAGE), anyString());
        verify(request).getRequestDispatcher(CLIENT_DASH_JSP);
    }

    @Test
    public void shouldBlockUserWhenHasMadeMaxClaims() throws Exception {
        // Given
        User user = new User(
                "Mike123",
                "Michael",
                "50 The Meadows",
                new Date(),
                new Date(),
                100.0f,
                User.STATUS_PENDING
        );
        List<Claim> claims = getListOfClaims();
        DatabaseFactory dbf = new DatabaseFactory();
        for (Claim claim : claims) {
            dbf.insert(claim);
        }
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(CLIENT_DASH_JSP)).thenReturn(requestDispatcher);

        // When
        new SubmitClaim().doGet(request, response);

        // Then
        verify(request).setAttribute(eq(ClientDashboard.ERROR_MESSAGE), anyString());
        verify(request).getRequestDispatcher(CLIENT_DASH_JSP);
    }
    
    @Test
    public void shouldBlockUserWhenMembershipPending() throws Exception {
        // Given
        User user = new User(
                "Mike123",
                "Michael",
                "50 The Meadows",
                new Date(),
                new GregorianCalendar(2017, 6, 11).getTime(),
                100.0f,
                User.STATUS_PENDING
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(CLIENT_DASH_JSP)).thenReturn(requestDispatcher);

        // When
        new SubmitClaim().doGet(request, response);

        // Then
        verify(request).setAttribute(eq(ClientDashboard.ERROR_MESSAGE), anyString());
        verify(request).getRequestDispatcher(CLIENT_DASH_JSP);
    }

    @Test
    public void shouldAllowUserWhenValid() throws Exception {
        // Given
        User user = new User(
                "Mike123",
                "Michael",
                "50 The Meadows",
                new Date(),
                new GregorianCalendar(2017, 6, 11).getTime(),
                100.0f,
                User.STATUS_APPROVED
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);

        // When
        new SubmitClaim().doGet(request, response);

        // Then
        verify(request).getRequestDispatcher(JSP);
    }

    @Test
    public void shouldInsertClaimObjectWhenValidPost() throws Exception {
        // Given
        User user = new User(
                "Mike123",
                "Michael",
                "50 The Meadows",
                new Date(),
                new Date(),
                100.0f,
                User.STATUS_PENDING
        );
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);
        when(request.getParameter("description")).thenReturn("I crashed my car.");
        when(request.getParameter("amount")).thenReturn("99.99");

        // When
        new SubmitClaim().doPost(request, response);

        // Then
        verify(request).getParameter("description");
        verify(request).getParameter("amount");
        verify(request).setAttribute(eq("createdClaim"), any(Claim.class));
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }

    private List<Claim> getListOfClaims() {
        List<Claim> claims = new ArrayList<>();
        claims.add(new Claim(1, "Mike123", new Date(), "My car exploded", "APPROVED", 250.55f));
        claims.add(new Claim(5, "Mike123", new Date(), "My car exploded again", "APPROVED", 12.99f));
        return claims;
    }
}
