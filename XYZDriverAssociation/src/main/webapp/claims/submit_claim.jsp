<%@page import="model.Claim"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.claims.SubmitClaim"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Submit a claim</title>
    </head>
    <body>
        <h1>Submit a claim</h1>
        <form action='SubmitClaim' method="post">
            <h4>Amount (£):</h4>
            <input type="number" min="0.01" step="0.01" max="100000" name="amount" placeholder="Enter the amount you are claiming for" required/>
            <br>
            <h4>Description</h4>
            <textarea cols="40" rows="5" name="description" placeholder="Please describe what you are claiming for" required></textarea>
            <br>
            <br>
            <input name='submitClaimButton' type='submit' value='Submit claim'/>
        </form>

        <br>
        <font color="green">
        <%
            if (request.getAttribute(SubmitClaim.CREATED_CLAIM) != null) {
                out.println("Claim successfully created!");
            }
        %>
        </font>

        <h3>
            <%
                if (request.getAttribute(SubmitClaim.CREATED_CLAIM) != null) {
                    out.println("Your claim");
                }
            %>
        </h3>

        <%
            if (request.getAttribute(SubmitClaim.CREATED_CLAIM) != null) {
                Claim claim = (Claim) request.getAttribute(SubmitClaim.CREATED_CLAIM);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                out.println("Date created: " + formatter.format(claim.getDate()));
                out.println("<br>Status: " + claim.getStatus());
                out.println("<br>Amount: £" + String.valueOf(claim.getAmount()));
                out.println("<br>Description: " + claim.getDescription());
            }
        %>

        <font color="red">
        <%
            if (request.getAttribute(SubmitClaim.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(SubmitClaim.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>
