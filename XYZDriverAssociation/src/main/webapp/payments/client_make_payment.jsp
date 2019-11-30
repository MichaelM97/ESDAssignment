<%@page import="servlet.payments.MakePayment"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Make Payment</title>
    </head>
    <body>       
        <div id="navbar">
            <ul>
                <li><a href='ClientDashboard' type="submit" method='get'>Home</a></li>
                <li><a class="active" href='MakePayment' type="submit" method='get'>Payments</a></li>
                <li><a href='SubmitClaim' type="submit" method='get'>Claims</a></li>
                <li><a href='UserHistory' type="submit" method='get'>History</a></li>
                <li><a href="ChangePassword" type="submit" method='get'>Account</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Make A Payment</h1>
        <form action='MakePayment' method="post">
            <h4>Todays Date: </h4>
            <%= new java.util.Date()%>
            <br>
            <h4>Amount (£):</h4>
            <input type="number" min="0.01" step="0.01" max="100000" name="amount" placeholder="Enter the amount you are paying" required/>
            <br>
            <br>
            <br>
            <input name='submitPaymentButton' type='submit' value='Submit payment'/>
        </form>
        <p class="success">
            <%
                if (request.getAttribute(MakePayment.CREATED_PAYMENT) != null) {
                    out.println("Payment successfully made");
                }
            %>
        </p>
        <p color="failure">
            <%
                if (request.getAttribute(MakePayment.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(MakePayment.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
