<%@page import="servlet.membership.ListAllMembers"%>
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>All Members</title>
    </head>
    <body>
        <h1>All Members</h1>

        <%
            if (request.getAttribute(ListAllMembers.USER_LIST) != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<User> usersList = (List<User>) request.getAttribute(ListAllMembers.USER_LIST);
                for (User user : usersList) {
                    out.println("<br>");
                    out.println("<b>Members username/ID: </b>" + user.getId() + "<br>");
                    out.println("<b>Members name: </b>" + user.getName() + "<br>");
                    out.println("<b>Members address: </b>" + user.getAddress() + "<br>");
                    out.println("<b>Members birthday: </b>" + formatter.format(user.getDob()) + "<br>");
                    out.println("<b>Members registration date: </b>" + formatter.format(user.getDor()) + "<br>");
                    out.println("<b>Members current balance: </b>£" + String.valueOf(user.getBalance()) + "<br>");
                    out.println("<b>Membership status: </b>" + user.getStatus() + "<br>");
                }
            }
        %>

        <br>

        <font color="red">
        <%
            if (request.getAttribute(ListAllMembers.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(ListAllMembers.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>


</html>
