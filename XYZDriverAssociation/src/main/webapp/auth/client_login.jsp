<%@page import="servlet.auth.Login"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Client login</title>
    </head>
    <body>
        <h1>Client login portal</h1>
        <form action='Login' method="post">
            <h4>Username:</h4>
            <input type="text" name="username" placeholder="Enter your username" required/>
            <br>
            <h4>Password:</h4>
            <input type="password" name="password" placeholder="Enter your password" required/>
            <br>
            <br>
            <input name='submitClientLoginButton' type='submit' value='Log in'/>
        </form>
        <br>
        <font color="red">
        <%
            if (request.getAttribute(Login.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(Login.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>
