<%@page import="servlet.dash.Dashboard"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Admin Dashboard</title>
        <!--
        The admin can:
            Handle members claims
            List all members
            List outstanding balances
            List all claims
            List all provisional member applications
            Process individual claims
            Process membership applications and upgrade if payment made.
            Suspend resume membership
            Report annual turnover
        -->
    </head>
    <body>
        <body>
            <div id="navbar">
                <ul>
                    <li><a class="active" href='Dashboard' type="submit" method='get'>Home</a></li>
                    <li><a href='ListMembershipApplications' type="submit" method='get' value='List all Membership Applications'>Applications</a></li>
                    <li><a href='ListAllMembers' type="submit" method='get' value='List all Members'>Members</a></li>
                    <li><a href='ListClaims' type="submit" method='get' value='List all Claims'>Claims</a></li>
                    <li><a href='ListPayments' type="submit" method='get' value='List all Payments'>Payments</a></li>
                    <li><a href="">Turnover</a></li>
                    <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
                </ul>
            </div>
        <h1>Admin Dashboard</h1>
    </body>
</html>