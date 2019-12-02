<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="model.User"%>
<%@page import="utils.SessionHelper"%>
<%@page import="servlet.withdraw.WithdrawFunds"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Make Withdrawal</title>
    </head>
    <body>       
        <div id="navbar">
            <ul>
                <li><a href='ClientDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='MakePayment' type="submit" method='get'>Payments</a></li>
                <li><a class="active" href='WithdrawFunds' type="submit" method='get'>Withdraw</a></li>
                <li><a href='SubmitClaim' type="submit" method='get'>Claims</a></li>
                <li><a href='UserHistory' type="submit" method='get'>History</a></li>
                <li><a href="ChangePassword" type="submit" method='get'>Account</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Make A Withdrawal</h1>
        <form action='WithdrawFunds' method="post">
            <h4>Todays Date: </h4>
            <%
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                out.println(formatter.format(new java.util.Date()));
            %>
            <br>
            <h4>Amount (£):</h4>
            <input type="number" min="0.01" step="0.01" max="100000" name="amount" placeholder="Enter the amount you want to withdraw" required/>
            <br>
            <br>
            <br>
            <input name='submitWithdrawalButton' type='submit' value='Submit'/>
        </form>
        <br>
        <p class="success">
            <%
                if (request.getAttribute(WithdrawFunds.MADE_WITHDRAWAL) != null) {
                    out.println("Withdraw successfully made.");
                }
            %>
        </p>
        <p class="failure">
            <%
                if (request.getAttribute(WithdrawFunds.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(WithdrawFunds.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
