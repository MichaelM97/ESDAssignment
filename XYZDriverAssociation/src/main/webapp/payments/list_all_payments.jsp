<%@page import="java.util.List"%>
<%@page import="model.Payment"%>
<%@page import="servlet.payments.ListPayments"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>All payments</title>
    </head>
    <body>

        <%
            if (request.getAttribute(ListPayments.PAYMENT_LIST) != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<Payment> paymentsList = (List<Payment>) request.getAttribute(ListPayments.PAYMENT_LIST);
                for (Payment payment : paymentsList) {
                    out.println("<br>");
                    out.println("<h4>Payment ID: " + payment.getId() + "</h4>");
                    out.println("Payment made by: " + payment.getMem_id());
                    out.println("<br>Payment type: " + payment.getType());
                    out.println("<br>Amount: £" + String.valueOf(payment.getAmount()));
                    out.println("<br>Payment made on: " + formatter.format(payment.getDate()));
                }
            }
        %>

        <br>

        <font color="red">
        <%
            if (request.getAttribute(ListPayments.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(ListPayments.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>
