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
        <!--
        The user can:
            Check outstanding balances
            Make a payments
            Submit a claim
            List claims and payments
        
        extra:
            claims after 6 months of membership
            Maximum 2 claims per year,
        -->
        
        <div id="user-info">
            <fieldset>
                <legend>client-info</legend>
                <!-- some call to get this information from db -->
                <!--<label action ='Testme' method='get'>-->
                <!--<font color="red">-->
                
                <%
                    if (request.getAttribute("usersName") != null) {
                        out.println(request.getAttribute("usersName"));
                    }
                %>
                <!--<font>-->
                <!--</label>-->
<!--                 <label> members.name </label><br/>
                    <label> members.status </label><br/>
                    <label> members.balance </label><br/>-->
             </fieldset>
        </div>
        <br/>
        <div id="user-options">
            
            <form action ='' method=''>
                <input name='payments' type='submit' value='payments'/>
            </form>
            <form action ='' method=''>
                <input name='claims' type='submit' value='claims'/>
            </form>
            <form action ='' method=''>
                <input name='history' type='submit' value='history'/>
            </form>
        </div>
        
    </body>
</html>
