<%@page import="servlet.auth.Registration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <script src="https://cdn.jsdelivr.net/npm/places.js@1.17.1"></script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/style.css">
        <title>Registration</title>
    </head>
    <body>
        <h1>Registration portal</h1>
        <form action='Registration' method="post">
            <h3>Login details</h3>
            <%
                if (request.getAttribute(Registration.GEN_PASSWORD) == null) {
                    //Private service is not being run so behave as standard
                    out.println("<h4>Username:</h4>");
                    out.println(
                            "<input type=\"text\" name=\"username\" placeholder=\"Choose a username\" required/>");
                    out.println("<br>");
                }
            %>
            <h4>Password:</h4>
            <%
                String password;
                if (request.getAttribute(Registration.GEN_PASSWORD) != null) {
                    password = "<input type=\"text\" name=\"password\" value=\"";
                    password += request.getAttribute(Registration.GEN_PASSWORD);
                    password += "\" ";
                } else {
                    password = "<input type=\"password\" name=\"password\" placeholder=\"Choose a password\" ";
                }
                password += "required/>";
                out.println(password);
            %>
            <br>
            <br>
            <h3>Your details</h3>
            <h4>First Name:</h4>
            <input type="text" name="first_name" placeholder="First Name" required/>
            <br>
            <h4>Last Name:</h4>
            <input type="text" name="last_name" placeholder="Last Name" required/>
            <br>
            <h4>Birthday:</h4>
            <input type="date" name="dob" required/>
            <br>
            <h4>Your address</h4>
            <input type="text" name="address" id="address-input" placeholder="Enter your address" maxlength="100" required/>
            <script>
                var placesAutocomplete = places({
                    appId: 'plZ2WUAD6E6X',
                    apiKey: 'ff2559081223d2f36887f4a981192c27',
                    container: document.querySelector('#address-input')
                });
            </script>
            <br>
            <br>
            <input name='submitRegistrationButton' type='submit' value='Create account'/>
        </form>
        <br>
        <p class="failure">
            <%
                if (request.getAttribute(Registration.ERROR_MESSAGE) != null) {
                    out.println(request.getAttribute(Registration.ERROR_MESSAGE));
                }
            %>
        </p>
    </body>
</html>
