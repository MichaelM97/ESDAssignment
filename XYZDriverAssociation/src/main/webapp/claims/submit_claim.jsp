<%@page import="model.Claim"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.claims.SubmitClaim"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Submit a claim</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='ClientDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='MakePayment' type="submit" method='get'>Payments</a></li>
                <li><a href='WithdrawFunds' type="submit" method='get'>Withdraw</a></li>
                <li><a class="active" href='SubmitClaim' type="submit" method='get'>Claims</a></li>
                <li><a href="">Activity</a></li>
                <li><a href="ChangePassword" type="submit" method='get'>Account</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Submit a claim</h1>
        <form action='SubmitClaim' method="post">
            <h4>Amount (£):</h4>
            <input type="number" min="5" step="0.01" max="100000" name="amount" placeholder="Enter the amount you are claiming for" required/>
            <br>
            <h4>Description</h4>
            <textarea cols="40" rows="5" name="description" placeholder="Please describe what you are claiming for" required></textarea>
            <br>
            <br>
            <input name='submitClaimButton' type='submit' value='Submit claim'/>
        </form>
        <br>
        <p class="success">
            <%
                if (request.getAttribute(SubmitClaim.CREATED_CLAIM) != null) {
                    out.println("Claim successfully created!");
                }
            %>
        </p>
        <h3>
            <%
                if (request.getAttribute(SubmitClaim.CREATED_CLAIM) != null) {
                    out.println("Your claim");
                }
            %>
        </h3>
        <%
            if (request.getAttribute(SubmitClaim.CREATED_CLAIM) != null) {
                Claim claim = (Claim) request.getAttribute(SubmitClaim.CREATED_CLAIM);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                out.println("Date created: " + formatter.format(claim.getDate()));
                out.println("<br>Status: " + claim.getStatus());
                out.println("<br>Amount: £" + String.valueOf(claim.getAmount()));
                out.println("<br>Description: " + claim.getDescription());
            }
        %>
        <p class="failure">
            <%
                if (request.getAttribute(SubmitClaim.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(SubmitClaim.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
