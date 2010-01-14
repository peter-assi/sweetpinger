<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="appchk.Stuff, com.google.appengine.api.users.*, java.security.Principal" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>- sweetpinger - start</title>
  </head>
  <body>
  <h1>Welcome to Sweetpinger</h1>
  <div>Sweetping is a service the helps you check if your web apps are responding to requests. </div>
  <div>We will ping (send a HEAD to) your URL every 10 minutes and if the response is anything else than 200 OK we will notify you by email. 
  </div>
    <h2>Input URL</h2>
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
	<form action="<%=Stuff.PINGROUTE%>" method="get">
	   <input type="text" name="<%=Stuff.URL%>"></input>
	   <input type="submit"></input>
	</form>
	<p>
	If the URL doesn't begin with http it will be added. That means that https:// should be added for such urls.
	</p>
<%
Principal user = request.getUserPrincipal();
if (user!=null){
%>
<a href="<%=Stuff.PINGROUTE%>">View URLs</a>
<%} else {
    UserService userService = UserServiceFactory.getUserService();
    String loginUrl = userService.createLoginURL(Stuff.PINGROUTE);
%>
    <a href="<%=loginUrl%>">Log in</a>
    <%
    }%>
  </body>
</html>
