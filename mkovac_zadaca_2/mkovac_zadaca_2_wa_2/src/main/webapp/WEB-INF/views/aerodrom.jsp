<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled aerodroma</title>
</head>
<body>
<%
	String icao = (String) request.getAttribute("icao");
	%>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Povratak</a><br>
		<h1>Pregled aerodroma <%=icao %></h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
		ICAO: ${aerodrom.icao} <br> Naziv: ${aerodrom.naziv} <br>
		Država: ${aerodrom.drzava} <br> Lokacija:
		${aerodrom.lokacija.latitude}, ${aerodrom.lokacija.longitude}
	</main>
</body>
</html>