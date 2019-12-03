<%@page import="model.User"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.membership.ListMembershipApplications"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Membership applications</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='AdminDashboard' type="submit" method='get'>Home</a></li>
                <li><a class="active" href='ListMembershipApplications' type="submit" method='get' value='List all Membership Applications'>Applications</a></li>
                <li><a href='ListAllMembers' type="submit" method='get' value='List all Members'>Members</a></li>
                <li><a href='ListClaims' type="submit" method='get' value='List all Claims'>Claims</a></li>
                <li><a href='ListPayments' type="submit" method='get' value='List all Payments'>Payments</a></li>
                <li><a href="SuspendResumeMembership" type="submit" method='get' value='Suspend/Resume Membership'>Suspend/Resume Membership</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Membership applications</h1>
        <%
            if (request.getAttribute(ListMembershipApplications.MEMBERSHIP_APPLICATION_LIST) != null) {
        %>

        <table align="center" width="80%">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Address</th>
                <th>Date of Birth</th>
                <th>Date of Registration</th>
                <th>Balance</th>
                <th>Status</th>
            </tr>

            <%
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<User> usersList = (List<User>) request.getAttribute(ListMembershipApplications.MEMBERSHIP_APPLICATION_LIST);
                for (User user : usersList) {
                    out.println("<tr>");
                    out.println("<td>" + user.getId() + "</td>");
                    out.println("<td>" + user.getName() + "</td>");
                    out.println("<td>" + user.getAddress() + "</td>");
                    out.println("<td>" + formatter.format(user.getDob()) + "</td>");
                    out.println("<td>" + formatter.format(user.getDor()) + "</td>");
                    out.println("<td>" + String.valueOf(user.getBalance()) + "</td>");
                    out.println("<td>" + user.getStatus() + "</td>");
                    out.println("</tr>");
                }
            %> 
        </table>

        <%
            }
        %>

        <p class="failure">
            <%
                if (request.getAttribute(ListMembershipApplications.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(ListMembershipApplications.ERROR_MESSAGE));
                }
            %>
        <p/>
    </body>
</html>
