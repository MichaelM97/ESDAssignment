<%-- 
    Document   : client_login
    Created on : 07-Nov-2019, 19:48:40
    Author     : michaelmccormick
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Client login</title>
    </head>
    <body>
        <h1>Client login portal</h1>
        <form action='ClientLogin' method="post">
            <h4>Username:</h4>
            <input type="text" name="username" placeholder="Enter your username"/>
            <br>
            <h4>Password:</h4>
            <input type="password" name="password" placeholder="Enter your password"/>
            <br>
            <br>
            <input name='submitClientLoginButton' type='submit' value='Log in'/>
        </form>
    </body>
</html>
