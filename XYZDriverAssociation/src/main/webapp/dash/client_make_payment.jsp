
<%@page import="servlet.Payment.MakePayment"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Make Payment</title>
    </head>
    <body>
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

        <font color="green">
        <%
            if (request.getAttribute(MakePayment.CREATED_PAYMENT) != null) {
                out.println("Payment successfully made");
            }
        %>
        </font>
        <font color="red">
        <%
            if (request.getAttribute(MakePayment.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(MakePayment.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>
