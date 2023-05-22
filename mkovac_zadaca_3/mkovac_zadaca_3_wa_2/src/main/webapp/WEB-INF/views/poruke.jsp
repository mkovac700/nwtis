<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Poruke</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a><br>
		<h1>Pregled primljenih poruka</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
		<br>
		<%
		List<String> poruke = (List<String>) request.getAttribute("poruke");
		String greska = (String) request.getAttribute("greska");
		if (poruke != null && !poruke.isEmpty()) {
		  for (String poruka : poruke) {
		%>
		<%=poruka%><br>

		<%
		}
		} else
		greska = "Nema podataka za prikaz!";
		%>

		<%
		if (greska != null) {
		%>
		<%=greska%>
		<%
		}
		%>
	</main>
</body>
</html>