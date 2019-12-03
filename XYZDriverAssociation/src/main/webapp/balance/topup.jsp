<%@page import="servlet.balance.Topup"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Top-up</title>
    </head>
    <body>       
        <div id="navbar">
            <ul>
                <li><a href='ClientDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='MakePayment' type="submit" method='get'>Payments</a></li>
                <li><a class="active" href='Topup' type="submit" method='get'>Top-up</a></li>
                <li><a href='WithdrawFunds' type="submit" method='get'>Withdraw</a></li>
                <li><a href='SubmitClaim' type="submit" method='get'>Claims</a></li>
                <li><a href='UserHistory' type="submit" method='get'>History</a></li>
                <li><a href="ChangePassword" type="submit" method='get'>Account</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Top-up your balance</h1>
        <form action='Topup' method="post">
            <h4>Todays Date: </h4>
            <%
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                out.println(formatter.format(new java.util.Date()));
            %>
            <br>
            <h4>Amount (£):</h4>
            <input type="number" min="5" step="0.01" max="100000" name="amount" placeholder="Enter the amount you want to top-up by" required/>
            <br>
            <br>
            <br>
            <input name='submitTopupButton' type='submit' value='Submit'/>
        </form>
        <br>
        <p class="success">
            <%
                if (request.getAttribute(Topup.SUCCESS_MESSAGE) != null) {
                    out.println(request.getAttribute(Topup.SUCCESS_MESSAGE));
                }
            %>
        </p>
        <p class="failure">
            <%
                if (request.getAttribute(Topup.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(Topup.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
