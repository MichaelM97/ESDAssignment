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
        <p class="failure">
            <%
                if (request.getAttribute(ListMembershipApplications.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(ListMembershipApplications.ERROR_MESSAGE));
                }
            %>
        <p/>
    </body>
</html>
