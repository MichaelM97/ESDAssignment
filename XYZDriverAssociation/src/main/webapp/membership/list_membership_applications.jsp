<%@page import="model.User"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.membership.ListMembershipApplications"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Membership applications</title>
    </head>
    <body>
        <h1>Membership applications</h1>

        <%
            if (request.getAttribute(ListMembershipApplications.MEMBERSHIP_APPLICATION_LIST) != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<User> usersList = (List<User>) request.getAttribute(ListMembershipApplications.MEMBERSHIP_APPLICATION_LIST);
                for (User user : usersList) {
                    out.println("<br>");
                    out.println("<h4>Member ID: " + user.getId() + "</h4>");
                    out.println("Member name: " + user.getName());
                    out.println("<br>Member address: " + user.getAddress());
                    out.println("<br>Member birthday: " + formatter.format(user.getDob()));
                    out.println("<br>Member date of Registration: " + formatter.format(user.getDor()));
                    out.println("<br>Member balance: £" + String.valueOf(user.getBalance()));
                    out.println("<br>Membership status " + user.getStatus());
                }
            }
        %>

        <br>

        <font color="red">
        <%
            if (request.getAttribute(ListMembershipApplications.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(ListMembershipApplications.ERROR_MESSAGE));
            }
        %>
    </body>
</html>
