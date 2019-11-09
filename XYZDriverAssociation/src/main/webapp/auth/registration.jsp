<%-- 
    Document   : registration
    Created on : 07-Nov-2019, 20:41:18
    Author     : michaelmccormick
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
    </head>
    <body>
        <h1>Registration portal</h1>
        <form action='Registration' method="post">
            <h4>Username:</h4>
            <input type="text" name="username" placeholder="Choose a username" required/>
            <br>
            <h4>Password:</h4>
            <input type="password" name="password" placeholder="Choose a password" required/>
            <br>
            <br>
            <input name='submitRegistrationButton' type='submit' value='Create account'/>
        </form>
    </body>
</html>
