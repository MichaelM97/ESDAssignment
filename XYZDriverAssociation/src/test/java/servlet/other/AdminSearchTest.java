package servlet.other;

import db.DatabaseFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

public class AdminSearchTest {

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
    public void shouldSetErrorIfNoResults() throws Exception {
        // Given
        when(request.getParameter("adminSearchField")).thenReturn("MCCORMICK");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // When
        new AdminSearch().doGet(request, response);

        // Then
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }

    @Test
    public void shouldFindCorrectUser() throws Exception {
        // Given
        when(request.getParameter("adminSearchField")).thenReturn("MCCORMICK");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        List<User> users = getListOfUsers();
        DatabaseFactory dbf = new DatabaseFactory();
        for (User user : users) {
            dbf.insert(user);
        }

        // When
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        new AdminSearch().doGet(request, response);

        // Then
        verify(request).setAttribute(eq("searchResults"), captor.capture());
        List<User> captorUsers = captor.getValue();
        assertTrue(captorUsers.get(0).getId().equals("m-mccormick"));
        assertTrue(captorUsers.size() == 1);
    }

    @After
    public void after() throws Exception {
        new DatabaseFactory().reset_db();
    }

    private List<User> getListOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(
                "m-mccormick",
                "password",
                "Michael McCormick",
                "UWE",
                new Date(),
                new Date(),
                100.0f,
                User.STATUS_APPROVED
        ));
        users.add(new User(
                "d-lewis",
                "password",
                "Dominic Lewis",
                "UWE",
                new Date(),
                new Date(),
                100.0f,
                User.STATUS_PENDING
        ));
        users.add(new User(
                "c-cheung",
                "password",
                "Tin Cheung",
                "UWE",
                new Date(),
                new Date(),
                100.0f,
                User.STATUS_APPROVED
        ));
        return users;
    }
}
