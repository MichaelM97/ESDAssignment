package servlet.payments;

import db.DatabaseFactory;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

public class ListPaymentsTest {

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
    public void shouldListPaymentsWhenValidRequest() throws Exception {
        // Given
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        List<Payment> payments = getListOfPayments();
        DatabaseFactory dbf = new DatabaseFactory();
        for (Payment payment : payments) {
            dbf.insert(payment);
        }

        // When
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        new ListPayments().doGet(request, response);

        // Then
        verify(request).setAttribute(eq("paymentList"), captor.capture());
        assertTrue(new ReflectionEquals(payments).matches(captor.getValue()));
    }

    @Test
    public void shouldApproveCorrectUser() throws Exception {
        // Given
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        List<Payment> payments = getListOfPayments();
        List<User> users = getListOfUsers();
        DatabaseFactory dbf = new DatabaseFactory();
        for (Payment payment : payments) {
            dbf.insert(payment);
        }
        for (User user : users) {
            dbf.insert(user);
        }
        String userID = "m-mccormick";
        when(request.getParameter("approvedUserID")).thenReturn(userID);

        // When
        new ListPayments().doPost(request, response);

        // Then
        ResultSet userResult = dbf.get_from_table("users", userID);
        assertTrue(userResult.getString("status").equals(User.STATUS_APPROVED));
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }

    private List<Payment> getListOfPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("m-mccormick", "FEE", 10.0f, new Date()));
        payments.add(new Payment("d-lewis", "FEE", 10.0f, new Date()));
        return payments;
    }

    private List<User> getListOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("m-mccormick", "password", "Michael", "50 The Meadows", new Date(), new Date(), 100.0f, User.STATUS_PENDING));
        users.add(new User("d-lewis", "password", "Dom", "11 The Street", new Date(), new Date(), 12.99f, User.STATUS_APPROVED));
        return users;
    }
}
