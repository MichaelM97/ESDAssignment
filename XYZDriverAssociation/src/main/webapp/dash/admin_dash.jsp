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
                <input name='list_pending_members' type='submit' value='List all Membership Applications'/>
            </form>
            <form action ='ListAllMembers' method='get'>
                <input name='membership' type='submit' value='List all Members'/>
            </form>
            <form action ='ListClaims' method='get'>
                <input name='claims' type='submit' value='List all Claims'/>
            </form>
            <form action ='ListPayments' method='get'>
                <input name='payments' type='submit' value='List all Payments'/>
            </form>
            <form action ='' method=''>
                <input name='turnover' type='submit' value='Turnover'/>
            </form>
        </div>
    </body>
</html>