<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.other.AdminSearch"%>
<%@page import="servlet.dash.AdminDashboard"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <title>User search</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a class="active" href='AdminDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='ListMembershipApplications' type="submit" method='get' value='List all Membership Applications'>Applications</a></li>
                <li><a href='ListAllMembers' type="submit" method='get' value='List all Members'>Members</a></li>
                <li><a href='ListClaims' type="submit" method='get' value='List all Claims'>Claims</a></li>
                <li><a href='ListPayments' type="submit" method='get' value='List all Payments'>Payments</a></li>
                <li><a href="SuspendResumeMembership" type="submit" method='get' value='Suspend/Resume Membership'>Suspend/Resume Membership</a></li>
                <form action="AdminSearch">
                    <li><input type="text" placeholder="Search users..." name="adminSearchField" required></li>
                    <li><button type="submit"><i class="fa fa-search"></i></button></li>
                </form>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>

            </ul>
        </div>
        <h1>Search results</h1>
        
        <%
            if (request.getAttribute(AdminSearch.SEARCH_RESULTS) != null) {
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
                List<User> usersList = (List<User>) request.getAttribute(AdminSearch.SEARCH_RESULTS);
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
                if (request.getAttribute(AdminDashboard.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(AdminDashboard.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>