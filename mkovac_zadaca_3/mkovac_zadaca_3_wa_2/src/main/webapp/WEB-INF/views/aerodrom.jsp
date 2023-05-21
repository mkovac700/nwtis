<%@page import="org.foi.nwtis.mkovac.zadaca_3.ws.WsAerodromi.endpoint.Aerodrom"%>
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
	String greska = (String) request.getAttribute("greska");
	Aerodrom aerodrom = (Aerodrom) request.getAttribute("aerodrom");
	%>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Povratak</a><br>
		<h1>Pregled aerodroma <%=icao %></h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
	<%if(aerodrom!=null){ %>
		ICAO: <%=aerodrom.getIcao() %> <br> Naziv: <%=aerodrom.getNaziv() %><br>
		Država: <%=aerodrom.getDrzava() %> <br> Lokacija:
		<%=aerodrom.getLokacija().getLatitude() %>, <%=aerodrom.getLokacija().getLongitude() %>
	<%}else greska = "Nema podataka za prikaz"; %>
	
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