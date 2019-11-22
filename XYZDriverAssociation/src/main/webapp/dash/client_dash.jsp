<%@page import="servlet.dash.Dashboard"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Client Dashboard</title>
    </head>
    <body>
        <h1>Client Dashboard</h1>
        <br/>
        <!--
        The user can:
            Check outstanding balances
            Make a payments
            Submit a claim
            List claims and payments
        
        extra:
            claims after 6 months of membership
            Maximum 2 claims per year,
        -->
        <div id="user-info">
            <fieldset>
                <legend>client-info</legend>          
                <%
                    if (request.getAttribute(Dashboard.USERS_NAME) != null) {
                        out.println(request.getAttribute(Dashboard.USERS_NAME));
                    }
                    
                    if (request.getAttribute(Dashboard.USERS_STATUS) != null) {
                        out.println(request.getAttribute(Dashboard.USERS_STATUS));
                    }
                %>
             </fieldset>
        </div>
        <br/>
        <div id="user-options">
            <form action ='' method=''>
                <input name='payments' type='submit' value='payments'/>
            </form>
            <form action ='SubmitClaim' method='get'>
                <input name='claims' type='submit' value='Submit a Claim'/>
            </form>
            <form action ='' method=''>
                <input name='history' type='submit' value='Payment & Claim History'/>
            </form>
        </div>
        
    </body>
</html>
