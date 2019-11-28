<%@page import="servlet.other.Turnover"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Turnover</title>
    </head>
    <body>
        <h1>Turnover</h1>           
        <%
           if (request.getAttribute(Turnover.TURNOVER) != null){
               out.println("£"+ request.getAttribute(Turnover.TURNOVER));
           } 
        %>
        <br>
        <font color="red">
        <%
            if (request.getAttribute(Turnover.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(Turnover.ERROR_MESSAGE));
            }
        %>
        </font>
    </body>
</html>
