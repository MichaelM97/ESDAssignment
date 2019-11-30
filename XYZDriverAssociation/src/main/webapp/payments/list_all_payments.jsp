<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.Payment"%>
<%@page import="servlet.payments.ListPayments"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>All payments</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='AdminDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='ListMembershipApplications' type="submit" method='get' value='List all Membership Applications'>Applications</a></li>
                <li><a href='ListAllMembers' type="submit" method='get' value='List all Members'>Members</a></li>
                <li><a href='ListClaims' type="submit" method='get' value='List all Claims'>Claims</a></li>
                <li><a class="active" href='ListPayments' type="submit" method='get' value='List all Payments'>Payments</a></li>
                <li><a href="Turnover" type="submit" method='get' value='Generate Turnover'>Turnover</a></li>
                <li><a href="SuspendResumeMembership" type="submit" method='get' value='Suspend/Resume Membership'>Suspend/Resume Membership</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <%
            if (request.getAttribute(ListPayments.PAYMENT_LIST) != null) {
                List<String> pendingUserIDs = new ArrayList<String>();
                if (request.getAttribute(ListPayments.PENDING_USERS_LIST) != null) {
                    pendingUserIDs = (List<String>) request.getAttribute(ListPayments.PENDING_USERS_LIST);
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<Payment> paymentsList = (List<Payment>) request.getAttribute(ListPayments.PAYMENT_LIST);
                for (Payment payment : paymentsList) {
                    out.println("<br>");
                    out.println("<h4>Payment ID: " + payment.getId() + "</h4>");
                    out.println("Payment made by: " + payment.getMem_id());
                    out.println("<br>Payment type: " + payment.getType());
                    out.println("<br>Amount: £" + String.valueOf(payment.getAmount()));
                    out.println("<br>Payment made on: " + formatter.format(payment.getDate()));
                    if (pendingUserIDs.contains(payment.getMem_id())) {
                        out.println("<br><b>This users membership has yet to be approved, would you like to approve it?</b>");
                        out.println("<form action ='ListPayments' method='post'> <input type='hidden' name='" + ListPayments.APPROVED_USER_ID + "' value='" + payment.getMem_id() + "'> <input name='payments' type='submit' value='Approve membership'/> </form>");
                    }
                }
            }
        %>
        <br>
        <p class="failure">
            <%
                if (request.getAttribute(ListPayments.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(ListPayments.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
