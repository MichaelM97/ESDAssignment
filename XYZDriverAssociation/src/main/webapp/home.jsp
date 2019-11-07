<%-- 
    Document   : home
    Created on : 07-Nov-2019, 19:34:22
    Author     : michaelmccormick
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>

    <body>
        <h1>Welcome to XYZ Driver Association</h1>
        <h2>Please select your user type:</h2>
        <form action ='ClientLogin' method='get'>
            <input name='clientLoginButton' type='submit' value='Client'/>
        </form>
        <form method='get' action ='AdminLogin'>
            <input name='adminLoginButton' type='submit' value='Admin'/>
        </form>
        <form method='get' action ='Registration'>
            <input name='registrationButton' type='submit' value='New user'/>
        </form>
    </body>
</html>
