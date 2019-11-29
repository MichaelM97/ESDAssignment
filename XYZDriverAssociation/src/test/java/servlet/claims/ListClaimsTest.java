package servlet.claims;

import db.DatabaseFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Claim;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
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
        DatabaseFactory dbf = new DatabaseFactory();
        dbf.reset_db();
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);

        // When
        new ListClaims().processRequest(request, response);

        // Then
        verify(request).setAttribute("errorMessage", "No claims have been filed yet");
    }

    @Test
    public void shouldListClaimsWhenValidRequest() throws Exception {
        // Given
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);
        List<Claim> claims = getListOfClaims();
        DatabaseFactory dbf = new DatabaseFactory();
        dbf.reset_db();
        for (Claim claim : claims) {
            dbf.insert(claim);
        }

        // When
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        new ListClaims().processRequest(request, response);

        // Then
        verify(request).setAttribute(eq("claimsList"), captor.capture());
        assertTrue(new ReflectionEquals(claims).matches(captor.getValue()));
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }

    private List<Claim> getListOfClaims() {
        List<Claim> claims = new ArrayList<>();
        claims.add(new Claim(1, "Michael12", new Date(), "My car exploded", "Approved", 250.55f));
        claims.add(new Claim(5, "Dom99", new Date(), "Crashed my Clio", "PENDING", 12.99f));
        claims.add(new Claim(7, "Jake69", new Date(), "I dont even have a car", "PENDING", 10000f));
        claims.add(new Claim(8, "Alex22", new Date(), "I skrr skrrrd too hard", "PENDING", 199.99f));
        claims.add(new Claim(9, "TinWahCaseyCheung", new Date(), "Crashed air force tin", "PENDING", 100000f));
        return claims;
    }
}
