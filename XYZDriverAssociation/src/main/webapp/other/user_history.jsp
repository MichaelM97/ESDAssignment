<%@page import="model.Claim"%>
<%@page import="java.util.List"%>
<%@page import="model.Payment"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.other.UserHistory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>User History</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='ClientDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='MakePayment' type="submit" method='get'>Payments</a></li>
                <li><a href='Topup' type="submit" method='get'>Top-up</a></li>
                <li><a href='WithdrawFunds' type="submit" method='get'>Withdraw</a></li>
                <li><a href='SubmitClaim' type="submit" method='get'>Claims</a></li>
                <li><a class="active" href='UserHistory' type="submit" method='get'>History</a></li>
                <li><a href="ChangePassword" type="submit" method='get'>Account</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>

        <h1>History</h1>
        <h2>Payments:</h2>
        <%
            if (request.getAttribute(UserHistory.PAYMENT_LIST) == null) {
                out.println("<p class=\"failure\">");
                out.println("You are yet to make any payments.");
                out.println("</p>");
            } else {
        %>

        <table align="center" width="50%">
            <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Amount (£)</th>
                <th>Date made</th>
            </tr>

            <%
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<Payment> paymentsList = (List<Payment>) request.getAttribute(UserHistory.PAYMENT_LIST);
                for (Payment payment : paymentsList) {
                    out.println("<tr>");
                    out.println("<td>" + payment.getId() + "</td>");
                    out.println("<td>" + payment.getType() + "</td>");
                    out.println("<td>" + String.valueOf(payment.getAmount()) + "</td>");
                    out.println("<td>" + formatter.format(payment.getDate()) + "</td>");
                    out.println("</tr>");
                }
            %> 
        </table>

        <%
            }
        %> 

        <h2>Claims:</h2>
        <%
            if (request.getAttribute(UserHistory.CLAIMS_LIST) == null) {
                out.println("<p class=\"failure\">");
                out.println("You are yet to make any claims.");
                out.println("</p>");
            } else {
        %> 

        <table  border="0" align="center" width="50%">
            <tr>
                <th>ID</th>
                <th>Date made</th>
                <th>Status</th>
                <th>Amount (£)</th>
                <th>Description</th>
            </tr>

            <%
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<Claim> claimsList = (List<Claim>) request.getAttribute(UserHistory.CLAIMS_LIST);
                for (Claim claim : claimsList) {
                    out.println("<tr>");
                    out.println("<td>" + claim.getId() + "</td>");
                    out.println("<td>" + formatter.format(claim.getDate()) + "</td>");
                    out.println("<td>" + claim.getStatus() + "</td>");
                    out.println("<td>" + String.valueOf(claim.getAmount()) + "</td>");
                    out.println("<td>" + claim.getDescription() + "</td>");
                    out.println("</tr>");
                }
            %> 
        </table>

        <%
            }
        %>

        <p class="failure">
            <%
                if (request.getAttribute(UserHistory.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(UserHistory.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>