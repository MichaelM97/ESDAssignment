<%@page import="model.User"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.membership.SuspendResumeMembership"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Suspend/Resume Membership</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='AdminDashboard' type="submit" method='get'>Home</a></li>
                <li><a class="active" href='ListMembershipApplications' type="submit" method='get' value='List all Membership Applications'>Applications</a></li>
                <li><a href='ListAllMembers' type="submit" method='get' value='List all Members'>Members</a></li>
                <li><a href='ListClaims' type="submit" method='get' value='List all Claims'>Claims</a></li>
                <li><a href='ListPayments' type="submit" method='get' value='List all Payments'>Payments</a></li>
                <li><a href="Turnover" type="submit" method='get' value='Generate Turnover'>Turnover</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Suspend/Resume Membership</h1>

        <%
            if (request.getAttribute(SuspendResumeMembership.USER_LIST) != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<User> usersList = (List<User>) request.getAttribute(SuspendResumeMembership.USER_LIST);
                for (User user : usersList) {
                    out.println("<br>");
                    out.println("<b>Members username/ID: </b>" + user.getId() + "<br>");
                    out.println("<b>Members name: </b>" + user.getName() + "<br>");
                    out.println("<b>Members registration date: </b>" + formatter.format(user.getDor()) + "<br>");
                    out.println("<b>Members current balance: </b>£" + String.valueOf(user.getBalance()) + "<br>");
                    out.println("<b>Membership status: </b>" + user.getStatus() + "<br>");
                    if (user.getStatus().equals(User.STATUS_APPROVED)) {
                        out.println("<form action ='SuspendResumeMembership' method='post'> <input type='hidden' name='" + SuspendResumeMembership.USER_ID + "' value='" + user.getId() + "'> <input name='suspend' type='submit' value='Suspend'/> </form>");
                    } else if (user.getStatus().equals(User.STATUS_SUSPENDED)) {
                        out.println("<form action ='SuspendResumeMembership' method='post'> <input type='hidden' name='" + SuspendResumeMembership.USER_ID + "' value='" + user.getId() + "'> <input name='resume' type='submit' value='Resume'/> </form>");
                    }

                }
            }
        %>


        <br>
        <font color="red">
        <%
            if (request.getAttribute(SuspendResumeMembership.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(SuspendResumeMembership.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>
