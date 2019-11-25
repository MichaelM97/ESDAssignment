<%@page import="servlet.auth.ChangePassword"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Management</title>
    </head>
    <body>
        <h1>Account Management</h1>
        <form action="ChangePassword" method="post">
            <h4>Password:</h4>
            <input type="password" name="password" placeholder="Enter your password" required/>
            <h4>New password:</h4>
            <input type="password" name="newPassword" placeholder="Enter your new password" required/>
            <br><br>
            <input name='submitChangePassword' type='submit' value='Enter'/>
        </form>
        <br>
        <font color="red">
        <%
            if (request.getAttribute(ChangePassword.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(ChangePassword.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>