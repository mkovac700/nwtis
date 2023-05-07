<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Brisanje leta</title>
</head>
<body>
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	String url = (String) request.getAttribute("url");
	%>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}<%=url %>">Povratak</a><br />
		<h1>Brisanje leta</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
		<br>
		<%=odgovor%>
	</main>
</body>
</html>