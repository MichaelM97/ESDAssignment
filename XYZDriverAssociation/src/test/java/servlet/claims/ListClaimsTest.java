package servlet.claims;

import db.DatabaseFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Claim;
import model.User;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

public class ListClaimsTest {

    private static final String JSP = "claims/list_all_claims.jsp";

    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher requestDispatcher;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void shouldSetErrorWhenNoClaims() throws Exception {
        // Given
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);

        // When
        new ListClaims().doGet(request, response);

        // Then
        verify(request).setAttribute("errorMessage", "No claims have been filed yet");
    }

    @Test
    public void shouldListClaimsWhenValidRequest() throws Exception {
        // Given
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);
        List<Claim> claims = getListOfClaims();
        DatabaseFactory dbf = new DatabaseFactory();
        for (Claim claim : claims) {
            dbf.insert(claim);
        }

        // When
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        new ListClaims().doGet(request, response);

        // Then
        verify(request).setAttribute(eq("claimsList"), captor.capture());
        assertTrue(new ReflectionEquals(claims).matches(captor.getValue()));
    }

    @Test
    public void shouldApproveCorrectClaim() throws Exception {
        // Given
        List<Claim> claims = getListOfClaims();
        List<User> users = getListOfUsers();

        DatabaseFactory dbf = new DatabaseFactory();
        for (Claim claim : claims) {
            dbf.insert(claim);
        }
        for (User user : users) {
            dbf.insert(user);
        }
        when(request.getParameter("claimID")).thenReturn("3");
        when(request.getParameter("submitStatus")).thenReturn("Approve");
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);

        // When
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        new ListClaims().doPost(request, response);

        // Then
        verify(request, times(1)).setAttribute(eq("claimsList"), captor.capture());
        List<Claim> captorClaims = captor.getValue();
        assertTrue(captorClaims.get(2).getStatus().equals(Claim.STATUS_APPROVED));
    }

    @Test
    public void shouldRejctCorrectClaim() throws Exception {
        // Given
        List<Claim> claims = getListOfClaims();
        List<User> users = getListOfUsers();

        DatabaseFactory dbf = new DatabaseFactory();
        for (Claim claim : claims) {
            dbf.insert(claim);
        }
        for (User user : users) {
            dbf.insert(user);
        }
        when(request.getParameter("claimID")).thenReturn("5");
        when(request.getParameter("submitStatus")).thenReturn("Reject");
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);

        // When
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        new ListClaims().doPost(request, response);

        // Then
        verify(request, times(1)).setAttribute(eq("claimsList"), captor.capture());
        List<Claim> captorClaims = captor.getValue();
        assertTrue(captorClaims.get(4).getStatus().equals(Claim.STATUS_REJECTED));
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }

    private List<Claim> getListOfClaims() {
        List<Claim> claims = new ArrayList<>();
        claims.add(new Claim("Michael12", new Date(), "My car exploded", "APPROVED", 250.55f));
        claims.add(new Claim("Dom99", new Date(), "Crashed my Clio", "PENDING", 12.99f));
        claims.add(new Claim("Jake69", new Date(), "I dont even have a car", "PENDING", 10000f));
        claims.add(new Claim("Alex22", new Date(), "I skrr skrrrd too hard", "PENDING", 199.99f));
        claims.add(new Claim("TinWahCaseyCheung", new Date(), "Crashed air force tin", "PENDING", 100000f));
        return claims;
    }

    private List<User> getListOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("Michael12", "password", "Michael", "50 The Meadows", new Date(), new Date(), 100.0f, "APPROVED"));
        users.add(new User("Dom99", "password", "Dom", "11 The Street", new Date(), new Date(), 12.99f, "APPROVED"));
        users.add(new User("Jake69", "password", "Jake", "23 The Road", new Date(), new Date(), 10000f, "APPROVED"));
        users.add(new User("Alex22", "password", "Alex", "54 The Lane", new Date(), new Date(), 199.99f, "APPROVED"));
        users.add(new User("TinWahCaseyCheung", "password", "Tin", "276 The Plaza", new Date(), new Date(), 100000f, "APPROVED"));
        return users;
    }
}
