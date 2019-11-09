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
        <form action ='Login' method='get'>
            <input name='clientLoginButton' type='submit' value='Client'/>
        </form>
        <form action ='Login' method='get'>
            <input name='adminLoginButton' type='submit' value='Admin'/>
        </form>
        <form action ='Registration' method='get'>
            <input name='registrationButton' type='submit' value='New user'/>
        </form>
    </body>
</html>
