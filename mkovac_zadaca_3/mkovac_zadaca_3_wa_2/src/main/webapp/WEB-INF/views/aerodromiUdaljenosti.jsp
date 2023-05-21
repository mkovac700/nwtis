<%@page
	import="org.foi.nwtis.mkovac.zadaca_3.ws.WsAerodromi.endpoint.UdaljenostKlasa"%>
<%@page import="org.foi.nwtis.podaci.Udaljenost"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenost između dva aerodroma</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a><br>
		<h1>Udaljenost između dva aerodroma po državama</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>

		<%
		String icaoOd = "";
		String icaoDo = "";

		String greska = null;

		if (request.getParameter("icaoOd") != null)
		  icaoOd = request.getParameter("icaoOd");
		if (request.getParameter("icaoDo") != null)
		  icaoDo = request.getParameter("icaoDo");
		%>

		<div style="display: flex;">
			<form action="" method="get">
				<label for="icaoOd">ICAO od:</label><br> <input type="text"
					id="icaoOd" name="icaoOd" value=<%=icaoOd%>><br> <label
					for="icaoDo">ICAO do:</label><br> <input type="text"
					id="icaoDo" name="icaoDo" value=<%=icaoDo%>>
				<button type="submit">Pretraži</button>
			</form>
		</div>
		<br>
		<%
		float ukupno = 0;
		List<UdaljenostKlasa> udaljenost2Aerodroma =
		    (List<UdaljenostKlasa>) request.getAttribute("udaljenost2Aerodroma");
		if ((icaoOd != null && !icaoOd.isEmpty()) && (icaoDo != null && !icaoDo.isEmpty())) {
		  if (udaljenost2Aerodroma != null && !udaljenost2Aerodroma.isEmpty()) {
		%>
		<table border=1>
			<tr>
				<th>Država</th>
				<th>Udaljenost (km)</th>
			</tr>
			<%
			for (UdaljenostKlasa u : udaljenost2Aerodroma) {
			  ukupno += u.getKm();
			%>
			<tr>
				<td><%=u.getDrzava()%></td>
				<td><%=u.getKm()%></td>
			</tr>
			<%
			}
			%>
			<tr>
				<td><b>Ukupno (km)</b></td>
				<td><b><%=ukupno%></b></td>
			</tr>
		</table>
		<%
		} else
		greska = "Nema podataka za prikaz!";
		%>

		<%
		} else
		greska = "Nema podataka za prikaz!";
		if (greska != null) {
		%>
		<%=greska%>
		<%
		}
		%>
	</main>
</body>
</html>