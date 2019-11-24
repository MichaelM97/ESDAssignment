<%@page import="java.util.List"%>
<%@page import="servlet.claims.ListClaims"%>
<%@page import="model.Claim"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>All claims</title>
    </head>
    <body>

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

        <font color="red">
        <%
            if (request.getAttribute(ListClaims.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(ListClaims.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>
