package servlet.other;

import db.DatabaseFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Claim;
import model.Payment;
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

public class UserHistoryTest {

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
    public void shouldListUsersHistory() throws Exception {
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
        List<Claim> claims = getListOfClaims();
        List<Payment> payments = getListOfPayments();
        DatabaseFactory dbf = new DatabaseFactory();
        for (Claim claim : claims) {
            dbf.insert(claim);
        }
        for (Payment payment : payments) {
            dbf.insert(payment);
        }

        // When
        ArgumentCaptor<List> claimCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List> paymentCaptor = ArgumentCaptor.forClass(List.class);
        new UserHistory().doGet(request, response);

        // Then
        verify(request).setAttribute(eq("claimList"), claimCaptor.capture());
        verify(request).setAttribute(eq("paymentList"), paymentCaptor.capture());
        List<Claim> captorClaims = claimCaptor.getValue();
        List<Payment> captorPayments = paymentCaptor.getValue();
        assertTrue(captorClaims.size() == 2);
        assertTrue(captorPayments.size() == 1);
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }

    private List<Claim> getListOfClaims() {
        List<Claim> claims = new ArrayList<>();
        claims.add(new Claim("m-mccormick", new Date(), "My car exploded", "APPROVED", 250.55f));
        claims.add(new Claim("Dom99", new Date(), "Crashed my Clio", "PENDING", 12.99f));
        claims.add(new Claim("Jake69", new Date(), "I dont even have a car", "PENDING", 10000f));
        claims.add(new Claim("m-mccormick", new Date(), "I skrr skrrrd too hard", "PENDING", 199.99f));
        claims.add(new Claim("TinWahCaseyCheung", new Date(), "Crashed air force tin", "PENDING", 100000f));
        return claims;
    }

    private List<Payment> getListOfPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("m-mccormick", "FEE", 10.0f, new Date()));
        payments.add(new Payment("d-lewis", "FEE", 10.0f, new Date()));
        return payments;
    }
}
