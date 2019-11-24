<%@page import="java.text.SimpleDateFormat"%>
<%@page import="model.User"%>
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
                <legend>Your information</legend>
                <%
                    if (request.getAttribute(Dashboard.USER_OBJECT_ATT) != null) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        User user = (User) request.getAttribute(Dashboard.USER_OBJECT_ATT);
                        out.println("<b>Username/ID: </b>" + user.getId() + "<br>");
                        out.println("<b>Name: </b>" + user.getName() + "<br>");
                        out.println("<b>Address: </b>" + user.getAddress() + "<br>");
                        out.println("<b>Birthday: </b>" + formatter.format(user.getDob()) + "<br>");
                        out.println("<b>Registration date: </b>" + formatter.format(user.getDor()) + "<br>");
                        out.println("<b>Current balance: </b>£" + String.valueOf(user.getBalance()) + "<br>");
                        out.println("<b>Membership status: </b>" + user.getStatus() + "<br>");
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
            <form action ='MakePayment' method='get'>
                <input name='payments' type='submit' value='Make a payment'/>
            </form>
            <form action ='SubmitClaim' method='get'>
                <input name='claims' type='submit' value='Submit a Claim'/>
            </form>
            <form action ='' method=''>
                <input name='history' type='submit' value='history'/>
            </form>
        </div>

    </body>
</html>