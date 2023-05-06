<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Letovi</title>
</head>
<body>
<%
	String odgovor = (String)request.getAttribute("odgovor");
	String url = (String)request.getAttribute("url");
%>
<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br/> 
<a href="${pageContext.servletContext.contextPath}<%=url %>">Povratak</a><br/> 
<%= odgovor %>

</body>
</html>