<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="appchk.Stuff, appchk.PingUrl, java.util.List, com.google.appengine.api.users.*" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>- sweetpinger - url list</title>
</head>
<body>
<% if (request.getAttribute(Stuff.USER)==null) {
    request.setAttribute(Stuff.FLASH, "Not logged in");
    request.getRequestDispatcher("/").forward(request, response);
}
%>
<h1>Your pinged URLs</h1>
<%  
    String flash = (String)request.getAttribute(Stuff.FLASH);
    if (flash != null) {
    %>
    <p>
    <%= flash %>
    </p>
    <%  
    }
    %>
<%
  List<PingUrl> urls = (List<PingUrl>)request.getAttribute(Stuff.LIST);
  for(PingUrl url : urls) {
      %>
      <p>
      <%=url.getUrl() %>&nbsp;<%=url.getLastStatus()%>&nbsp;<a href="<%=Stuff.DELETEROUTE+"?"+Stuff.ID+"="+url.getId()%>">Remove</a>
      </p>
      <%
  }

%>
<%
UserService userService = UserServiceFactory.getUserService();
String logoutUrl = userService.createLogoutURL("/");
String user = userService.getCurrentUser().getEmail();
%>
<a href="/">Add more</a>
<a href="<%=logoutUrl%>">Log out <%=user%></a>
</body>
</html>