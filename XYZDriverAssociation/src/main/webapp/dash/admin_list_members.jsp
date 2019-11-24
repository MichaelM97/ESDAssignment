
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.Members.ListAllMembers"%>
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
            if (request.getAttribute(ListAllMembers.USERS_LIST) != null) {
                List<User> usersList = (List<User>) request.getAttribute(ListAllMembers.USERS_LIST);
                for (User user : usersList) {
                    out.println("<br>");
                    out.println("<h4>User ID: " + user.getId() + "</h4>");
                    out.println("<h4>Account Status: " + user.getStatus() + "</h4>");
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
