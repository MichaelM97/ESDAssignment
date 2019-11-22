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
        <div id="user-info">
            <fieldset> 
                <h4>Username:</h4>
                <%
                    if (request.getAttribute(Dashboard.USERS_NAME) != null) {
                        out.println(request.getAttribute(Dashboard.USERS_NAME));
                    }
                %>
                <br>
                <h4>Membership status:</h4>
                <%
                    if (request.getAttribute(Dashboard.USERS_STATUS) != null) {
                        out.println(request.getAttribute(Dashboard.USERS_STATUS));
                    }
                %>

                <font color="blue">
                <%
                    if (request.getAttribute(Dashboard.INFO_MESSAGE) != null) {
                        out.println("<br>");
                        out.println(request.getAttribute(Dashboard.INFO_MESSAGE));
                    }
                %>
                </font>
            </fieldset>
        </div>
        <br/>
        <h4>Options:</h4>
        <div id="user-options">
            <form action ='' method=''>
                <input name='payments' type='submit' value='payments'/>
            </form>
            <form action ='' method=''>
                <input name='claims' type='submit' value='claims'/>
            </form>
            <form action ='' method=''>
                <input name='history' type='submit' value='history'/>
            </form>
        </div>

    </body>
</html>
