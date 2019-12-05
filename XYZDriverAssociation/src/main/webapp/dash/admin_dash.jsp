<%@page import="servlet.dash.AdminDashboard"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <title>Admin Dashboard</title>
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
        <h1>Admin Dashboard</h1>
        <h3>Business metrics (year-to-date) -</h3>
        <table style="width:30%">
            <tr>
                <td>Turnover (payments-to-date)</td>
                <%
                    Float turnover = (Float) request.getAttribute(AdminDashboard.TURNOVER);
                    out.println("<td>£" + turnover + "</td>");
                %>
            </tr>
            <tr>
                <td>Pay-outs (claims-to-date)</td>
                <%
                    Float payouts = (Float) request.getAttribute(AdminDashboard.PAY_OUTS);
                    out.println("<td>£" + payouts + "</td>");
                %>
            </tr>
            <tr>
                <td>Profit (turnover - pay-outs)</td>
                <%
                    Float profit = (Float) request.getAttribute(AdminDashboard.PROFIT);
                    out.println("<td>£" + profit + "</td>");
                %>
            </tr>
        </table>
        <br>
        <form action ='AdminDashboard' method='post'>
            <input name='annualCharge' type='submit'  value='Issue annual charge to all members'/>
        </form>
        <p class="success">
            <%
                if (request.getAttribute(AdminDashboard.SUCCESS_MESSAGE) != null) {
                    out.println(request.getAttribute(AdminDashboard.SUCCESS_MESSAGE));
                }
            %>
        </p>
        <p class="failure">
            <%
                if (request.getAttribute(AdminDashboard.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(AdminDashboard.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>