<%@page import="servlet.dash.ClientDashboard"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Client Dashboard</title>
    </head>
    <body>         
        <div id="navbar">
            <ul>
                <li><a class="active" href='ClientDashboard' type="submit" method='get'>Home</a></li>
                <li><a href='MakePayment' type="submit" method='get'>Payments</a></li>
                <li><a href='WithdrawFunds' type="submit" method='get'>Withdraw</a></li>
                <li><a href='SubmitClaim' type="submit" method='get'>Claims</a></li>
                <li><a href="">History</a></li>
                <li><a href="ChangePassword" type="submit" method='get'>Account</a></li>
                <li style="float:right"><a href="Logout" type="submit" method='get'>Logout</a></li>
            </ul>
        </div>

        <h1>Client Dashboard</h1>
        <br/>
        <div id="user-info">
            <fieldset> 
                <legend>Your information</legend>
                <%
                    if (request.getAttribute(ClientDashboard.USER_OBJECT_ATT) != null) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        User user = (User) request.getAttribute(ClientDashboard.USER_OBJECT_ATT);
                        out.println("<b>Username/ID: </b>" + user.getId() + "<br>");
                        out.println("<b>Name: </b>" + user.getName() + "<br>");
                        out.println("<b>Address: </b>" + user.getAddress() + "<br>");
                        out.println("<b>Birthday: </b>" + formatter.format(user.getDob()) + "<br>");
                        out.println("<b>Registration date: </b>" + formatter.format(user.getDor()) + "<br>");
                        out.println("<b>Current balance: </b>£" + String.valueOf(user.getBalance()) + "<br>");
                        out.println("<b>Membership status: </b>" + user.getStatus() + "<br>");
                    }
                %>
                <p class="info">
                    <%
                        if (request.getAttribute(ClientDashboard.INFO_MESSAGE) != null) {
                            out.println("<br>");
                            out.println(request.getAttribute(ClientDashboard.INFO_MESSAGE));
                        }
                    %>
                </p>
                <p class="failure">
                    <%
                        if (request.getAttribute(ClientDashboard.ERROR_MESSAGE) != null) {
                            out.println("<br>");
                            out.println(request.getAttribute(ClientDashboard.ERROR_MESSAGE));
                        }
                    %>
                </p>
            </fieldset>
        </div>
    </body>
</html>