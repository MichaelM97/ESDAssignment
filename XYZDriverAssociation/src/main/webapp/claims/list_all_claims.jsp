<%@page import="java.util.List"%>
<%@page import="servlet.claims.ListClaims"%>
<%@page import="model.Claim"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>All claims</title>
    </head>
    <body>
        <div id="navbar">
            <ul>
                <li><a href='AdminDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='ListMembershipApplications' type="submit" method='get' value='List all Membership Applications'>Applications</a></li>
                <li><a href='ListAllMembers' type="submit" method='get' value='List all Members'>Members</a></li>
                <li><a class="active" href='ListClaims' type="submit" method='get' value='List all Claims'>Claims</a></li>
                <li><a href='ListPayments' type="submit" method='get' value='List all Payments'>Payments</a></li>
                <li><a href="Turnover" type="submit" method='get' value='Generate Turnover'>Turnover</a></li>
                <li><a href="SuspendResumeMembership" type="submit" method='get' value='Suspend/Resume Membership'>Suspend/Resume Membership</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <%
            if (request.getAttribute(ListClaims.CLAIMS_LIST) != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<Claim> claimsList = (List<Claim>) request.getAttribute(ListClaims.CLAIMS_LIST);
                for (Claim claim : claimsList) {
                    out.println("<br>");
                    out.println("<h4>Claim ID: " + claim.getId() + "</h4>");
                    out.println("Claim created by: " + claim.getMem_id());
                    out.println("<br>Date created: " + formatter.format(claim.getDate()));
                    out.println("<br>Status: " + claim.getStatus());
                    out.println("<br>Amount: £" + String.valueOf(claim.getAmount()));
                    out.println("<br>Description: " + claim.getDescription());
                }
            }
        %>
        <br>
        <p class="failure">
            <%
                if (request.getAttribute(ListClaims.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(ListClaims.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
