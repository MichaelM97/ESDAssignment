package servlet.dash;

import db.DatabaseFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Payment;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AdminDashTest {

    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher requestDispatcher;
    private static final String JSP = "dash/admin_dash.jsp";

    @Before
    public void setUp() {
        new DatabaseFactory().reset_db();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }

    @Test
    public void setNoPayments() throws Exception {
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);
        new AdminDashboard().doGet(request, response);
        verify(request).setAttribute(AdminDashboard.PROFIT, (float) 0.0);
        verify(request).setAttribute(AdminDashboard.PAY_OUTS, (float) 0.0);
        verify(request).setAttribute(AdminDashboard.TURNOVER, (float) 0.0);
    }

    @Test
    public void setPayments() throws Exception {
        float payment_amount = 0;
        DatabaseFactory dbf = new DatabaseFactory();
        for (User user : getListOfUsers()) {
            dbf.insert(user);
        }
        for (Payment payment : getListOfPayments("test")) {
            dbf.insert(payment);
            payment_amount += payment.getAmount();
        }
        when(request.getRequestDispatcher(JSP)).thenReturn(requestDispatcher);
        new AdminDashboard().doGet(request, response);

        verify(request).setAttribute(
                AdminDashboard.PAY_OUTS, (float) 0.0);
        verify(request).setAttribute(
                AdminDashboard.TURNOVER, (float) payment_amount);
        verify(request).setAttribute(
                AdminDashboard.PROFIT, (float) payment_amount);
    }

    private List<Payment> getListOfPayments(String type) {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("Michael12", type, 100, new Date()));
        payments.add(new Payment("Dom99", type, 100, new Date()));
        payments.add(new Payment("Jake69", type, 100, new Date()));
        payments.add(new Payment("Alex22", type, 100, new Date()));
        payments.add(new Payment("TinWahCaseyCheung", type, 100, new Date()));
        return payments;
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
