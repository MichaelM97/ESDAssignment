<%@page import="servlet.auth.Registration"%>
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
            <h3>Login details</h3>
            <h4>Username:</h4>
            <input type="text" name="username" placeholder="Choose a username" required/>
            <br>
            <h4>Password:</h4>
            <input type="password" name="password" placeholder="Choose a password" required/>
            <br>
            <br>
            <h3>Your details</h3>
            <h4>Name:</h4>
            <input type="text" name="name" placeholder="Enter your name" required/>
            <br>
            <h4>Address:</h4>
            <textarea cols="40" rows="5" name="address" placeholder="Enter your address" required></textarea>
            <br>
            <h4>Birthday:</h4>
            <input type="date" name="dob" required/>
            <br>
            <br>
            <input name='submitRegistrationButton' type='submit' value='Create account'/>
        </form>
        <br>
        <font color="red">
        <%
            if (request.getAttribute(Registration.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(Registration.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>
