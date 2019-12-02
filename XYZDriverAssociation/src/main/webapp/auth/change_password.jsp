<%@page import="servlet.auth.ChangePassword"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Account Management</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='ClientDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='MakePayment' type="submit" method='get'>Payments</a></li>
                <li><a href='WithdrawFunds' type="submit" method='get'>Withdraw</a></li>
                <li><a href='SubmitClaim' type="submit" method='get'>Claims</a></li>
                <li><a href='UserHistory' type="submit" method='get'>History</a></li>
                <li><a class="active" href="ChangePassword" type="submit" method='get'>Account</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Account Management</h1>
        <form action="ChangePassword" method="post">
            <h4>Password:</h4>
            <input type="password" name="password" placeholder="Enter your password" required/>
            <h4>New password:</h4>
            <input type="password" name="newPassword" placeholder="Enter your new password" required/>
            <br><br>
            <input name='submitChangePassword' type='submit' value='Enter'/>
        </form>
        <br>
        <p class="success">
            <%
                if (request.getAttribute(ChangePassword.INFO_MESSAGE) != null) {
                    out.println(request.getAttribute(ChangePassword.INFO_MESSAGE));
                }
            %>
        </p>
        <p class="failure">
            <%
                if (request.getAttribute(ChangePassword.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(ChangePassword.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>