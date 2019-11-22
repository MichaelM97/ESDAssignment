<%@page import="model.Member"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="servlet.membership.ListMembershipApplications"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Membership applications</title>
    </head>
    <body>
        <h1>Membership applications</h1>

        <%
            if (request.getAttribute(ListMembershipApplications.MEMBERSHIP_APPLICATION_LIST) != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                List<Member> membersList = (List<Member>) request.getAttribute(ListMembershipApplications.MEMBERSHIP_APPLICATION_LIST);
                for (Member member : membersList) {
                    out.println("<br>");
                    out.println("<h4>Member ID: " + member.getId() + "</h4>");
                    out.println("Member name: " + member.getName());
                    out.println("<br>Member address: " + member.getAddress());
                    out.println("<br>Member DoB: " + formatter.format(member.getDob()));
                    out.println("<br>Member DoR: " + formatter.format(member.getDor()));
                    out.println("<br>Membership status " + member.getStatus());
                    out.println("<br>Member balance: £" + String.valueOf(member.getBalance()));
                }
            }
        %>

        <br>

        <font color="red">
        <%
            if (request.getAttribute(ListMembershipApplications.ERROR_MESSAGE) != null) {
                out.println(request.getAttribute(ListMembershipApplications.ERROR_MESSAGE));
            }
        %>
    </body>
</html>
