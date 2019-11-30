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
                <li><a href="SuspendResumeMembership" type="submit" method='get' value='Suspend/Resume Membership'>Suspend/Resume Membership</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Turnover</h1>
        <h4>Turnover:</h4>
        <%
            if (request.getAttribute(Turnover.TURNOVER) != null && request.getAttribute(Turnover.RECOUPED) != null) {
                Float turnover = (Float) request.getAttribute(Turnover.TURNOVER);
                Float recouped = (Float) request.getAttribute(Turnover.RECOUPED);
                out.println("£" + (turnover + recouped));
            } else if (request.getAttribute(Turnover.TURNOVER) != null) {
                out.println("£" + request.getAttribute(Turnover.TURNOVER));
            }
        %>
        <br>
        <h4>Outgoing Claims:</h4>
        <%
            if (request.getAttribute(Turnover.OUTGOING) != null && request.getAttribute(Turnover.RECOUPED) != null) {
                Float outgoing = (Float) request.getAttribute(Turnover.OUTGOING);
                Float recouped = (Float) request.getAttribute(Turnover.RECOUPED);
                out.println("£" + (outgoing - recouped));
            } else if (request.getAttribute(Turnover.OUTGOING) != null) {
                out.println("£" + request.getAttribute(Turnover.OUTGOING));
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
        <form action ='Turnover' method='post'> 
            <input name='annualCharge' type='submit'  value='Issue Annual Charge'/>
        </form>
    </body>
</html>