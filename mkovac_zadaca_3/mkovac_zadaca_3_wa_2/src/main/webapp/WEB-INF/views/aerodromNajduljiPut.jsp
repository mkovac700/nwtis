<%@page import="org.foi.nwtis.mkovac.zadaca_3.ws.WsAerodromi.endpoint.UdaljenostAerodromDrzavaKlasa"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Najdulji put države</title>
</head>
<body>
	<%
	String icaoOd = (String) request.getAttribute("icao");
	String greska = null;
	%>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Povratak</a><br>
		<h1>
			Pregled najduljeg puta države od aerodroma
			<%=icaoOd%></h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
		<%
		UdaljenostAerodromDrzavaKlasa uad =
		    (UdaljenostAerodromDrzavaKlasa) request.getAttribute("udaljenostAerodromDrzava");
		String icao = "";
		String drzava = "";
		float km = 0;
		if (uad != null) {
		  icao = uad.getIcao();
		  drzava = uad.getDrzava();
		  km = uad.getKm();
		%>
		ICAO:
		<%=icao%>
		<br> Država:
		<%=drzava%><br> Udaljenost (km):
		<%=km%>
		<%
		} else {
		greska = "Nema podataka za prikaz!";
		}
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