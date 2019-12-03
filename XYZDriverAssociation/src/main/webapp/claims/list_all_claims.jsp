
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
                <li><a href="SuspendResumeMembership" type="submit" method='get' value='Suspend/Resume Membership'>Suspend/Resume Membership</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>
        <h1>All Claims</h1>
        <%
            if (request.getAttribute(ListClaims.CLAIMS_LIST) != null) {
        %>

        <table align="center" width="80%">
            <tr>
                <th>ID</th>
                <th>Associated Member</th>
                <th>Date Made</th>
                <th>Amount (£)</th>
                <th>Description</th>
                <th>Status</th>
            </tr>

            <%
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<Claim> claimsList = (List<Claim>) request.getAttribute(ListClaims.CLAIMS_LIST);
                for (Claim claim : claimsList) {
                    out.println("<tr>");
                    out.println("<td>" + claim.getId() + "</td>");
                    out.println("<td>" + claim.getMem_id() + "</td>");
                    out.println("<td>" + formatter.format(claim.getDate()) + "</td>");
                    out.println("<td>" + String.valueOf(claim.getAmount()) + "</td>");
                    out.println("<td>" + claim.getDescription() + "</td>");
                    if (claim.getStatus().equals(Claim.STATUS_PENDING)) {
                        out.println("<td>" +
                                "<form action ='ListClaims' method='post'> <input type='hidden' name='claimID' value='" + claim.getId() + "'> <input name='submitStatus' type='submit' value='Approve'/> </form>" +
                                        "<form action ='ListClaims' method='post'> <input type='hidden' name='claimID' value='" + claim.getId() + "'> <input name='submitStatus' type='submit' value='Reject'/> </form>" +
                                        "</td>");
                    } else {
                        out.println("<td>" + claim.getStatus() + "</td>");
                    }
                    out.println("</tr>");
                }
            %> 
        </table>

        <%
            }
        %>

        <p class="failure">
            <%
                if (request.getAttribute(ListClaims.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(ListClaims.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
