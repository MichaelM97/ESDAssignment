<%@page import="servlet.dash.Dashboard"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Dashboard</title>
    </head>
    <body>
        <h1>Admin Dashboard</h1>
        <!--
        The admin can:
            Handle members claims
            List all members
            List outstanding balances
            List all claims
            List all provisional member applications
            Process individual claims
            Process membership applications and upgrade if payment made.
            Suspend resume membership
            Report annual turnover
        -->
        <h4>Options:</h4>
        <div id="user-options">
            <form action ='ListMembershipApplications' method='get'>
                <input name='list_pending_members' type='submit' value='List membership applications'/>
            </form>
            <form action ='' method=''>
                <input name='claims' type='submit' value='claims'/>
            </form>
            <form action ='' method=''>
                <input name='turnover' type='submit' value='turnover'/>
            </form>
        </div>
    </body>
</html>
