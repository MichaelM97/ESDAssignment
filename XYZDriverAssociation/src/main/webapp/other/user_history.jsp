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
        <table  border="0" align="left" width="30%">
            <tr>
                <td>
                    <h2>Payments:</h2>
                    <%
                        if (request.getAttribute(UserHistory.PAYMENT_LIST) != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            List<Payment> paymentsList = (List<Payment>) request.getAttribute(UserHistory.PAYMENT_LIST);
                            for (Payment payment : paymentsList) {
                                out.println("<br>");
                                out.println("<h4>Payment ID: " + payment.getId() + "</h4>");
                                out.println("<br>Payment type: " + payment.getType());
                                out.println("<br>Amount: £" + String.valueOf(payment.getAmount()));
                                out.println("<br>Payment made on: " + formatter.format(payment.getDate()));
                            }
                        } else {
                            out.println("<p class=\"failure\">");
                            out.println("You are yet to make any payments.");
                            out.println("</p>");
                        }
                    %> 
                </td>
                <td>
                    <h2>Claims:</h2>
                    <%
                        if (request.getAttribute(UserHistory.CLAIMS_LIST) != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            List<Claim> claimsList = (List<Claim>) request.getAttribute(UserHistory.CLAIMS_LIST);
                            for (Claim claim : claimsList) {
                                out.println("<br>");
                                out.println("<h4>Claim ID: " + claim.getId() + "</h4>");
                                out.println("Claim created by: " + claim.getMem_id());
                                out.println("<br>Date created: " + formatter.format(claim.getDate()));
                                out.println("<br>Status: " + claim.getStatus());
                                out.println("<br>Amount: £" + String.valueOf(claim.getAmount()));
                                out.println("<br>Description: " + claim.getDescription());
                            }
                        } else {
                            out.println("<p class=\"failure\">");
                            out.println("You are yet to make any claims.");
                            out.println("</p>");
                        }
                    %>
                </td>
            </tr>
        </table>
        <p class="failure">
            <%
                if (request.getAttribute(UserHistory.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(UserHistory.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
