<%@page import="java.text.SimpleDateFormat"%>
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
                <li><a href='Topup' type="submit" method='get'>Top-up</a></li>
                <li><a href='WithdrawFunds' type="submit" method='get'>Withdraw</a></li>
                <li><a href='SubmitClaim' type="submit" method='get'>Claims</a></li>
                <li><a href='UserHistory' type="submit" method='get'>History</a></li>
                <li><a href="ChangePassword" type="submit" method='get'>Account</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>Make A Payment</h1>
        <form action='MakePayment' method="post">
            <h4>Todays Date: </h4>
            <%
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                out.println(formatter.format(new java.util.Date()));
            %>
            <br>
            <h4>Reference:</h4>
            <select name='reference' id="selectRef">
                <option value='FEE'>Membership fee</option>
                <option value='OTHER'>Other</option>
            </select>
            <br>
            <h4>Amount (£):</h4>
            <input type="number" min="0.01" step="0.01" max="100000" name="amount" id='amountRef' placeholder="Enter the amount you are paying" required/>
            <br>
            <br>
            <br>
            <input name='submitPaymentButton' type='submit' value='Submit payment'/>
        </form>
        <p class="success">
            <%
                if (request.getAttribute(MakePayment.CREATED_PAYMENT) != null) {
                    out.println("Payment successfully made!<br>");
                }
            %>
        </p>
        <p class="failure">
            <%
                if (request.getAttribute(MakePayment.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(MakePayment.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js" ></script>
    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>‌
    <script>
        $(document).ready(function () {
            toggleFields();
            $("#selectRef").change(function () {
                toggleFields();
            });

        });
        function toggleFields() {
            if ($("#selectRef").val() === "FEE") {
                document.getElementById("amountRef").value = "10";
                document.getElementById("amountRef").readOnly = true;
            } else {
                document.getElementById("amountRef").value = "";
                document.getElementById("amountRef").readOnly = false;
            }
        }
    </script>
</html>
