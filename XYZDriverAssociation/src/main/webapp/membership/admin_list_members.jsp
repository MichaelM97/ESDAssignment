<%@page import="servlet.membership.ListAllMembers"%>
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>All Members</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='Dashboard' type="submit" method='get'>Home</a></li>
                <li><a href='ListMembershipApplications' type="submit" method='get' value='List all Membership Applications'>Applications</a></li>
                <li><a class="active" href='ListAllMembers' type="submit" method='get' value='List all Members'>Members</a></li>
                <li><a href='ListClaims' type="submit" method='get' value='List all Claims'>Claims</a></li>
                <li><a href='ListPayments' type="submit" method='get' value='List all Payments'>Payments</a></li>
                <li><a href="">Turnover</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
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
        <p class="failure">
        <%
            if (request.getAttribute(ListAllMembers.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(ListAllMembers.ERROR_MESSAGE));
            }
        %>
        </p>
    </body>


</html>
