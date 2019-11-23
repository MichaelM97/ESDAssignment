<%@page import="servlet.dash.Dashboard"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Dashboard</title>
    </head>
    <body>
        <h1>Admin Dashboard</h1>
        <br/>
        <!--
        The admin can:
            Handle members claims
            List all members
            List outstanding balances
            List all claims
            List all provisional member applications
            Process individual claims
            Process membership applications and upgrade if payment made.
            Suspend resume membership
            Report annual turnover
        -->
        <div id="user-info">
            <fieldset>
                <legend>admin-info</legend>          
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
        <div id="user-options">
            <form action ='ListAllMembers' method='get'>
                <input name='membership' type='submit' value='List all members'/>
            </form>
            <form action ='ListClaims' method='get'>
                <input name='claims' type='submit' value='List all Claims'/>
            </form>
            <form action ='' method=''>
                <input name='turnover' type='submit' value='turnover'/>
            </form>
        </div>
    </body>
</html>
