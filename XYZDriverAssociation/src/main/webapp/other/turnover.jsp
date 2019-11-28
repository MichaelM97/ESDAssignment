<%@page import="servlet.other.Turnover"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Turnover</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='AdminDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='ListMembershipApplications' type="submit" method='get' value='List all Membership Applications'>Applications</a></li>
                <li><a href='ListAllMembers' type="submit" method='get' value='List all Members'>Members</a></li>
                <li><a href='ListClaims' type="submit" method='get' value='List all Claims'>Claims</a></li>
                <li><a href='ListPayments' type="submit" method='get' value='List all Payments'>Payments</a></li>
                <li><a class="active" href="Turnover" type="submit" method='get' value='Generate Turnover'>Turnover</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Turnover</h1>           
        <%
            if (request.getAttribute(Turnover.TURNOVER) != null) {
                out.println("£" + request.getAttribute(Turnover.TURNOVER));
            }
        %>
        <br>
        <p class="failure">
            <%
                if (request.getAttribute(Turnover.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(Turnover.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>

