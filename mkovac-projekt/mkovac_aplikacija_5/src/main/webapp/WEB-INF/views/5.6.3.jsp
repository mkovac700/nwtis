<%@page import="org.foi.nwtis.mkovac_aplikacija_4.ws.WsLetovi.endpoint.LetAviona"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.time.ZonedDateTime"%>
<%@page import="java.time.Instant"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.ZoneOffset"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Letovi s aerodroma na dan - OS</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/letovi">Povratak</a><br> 
		<h1>Letovi s aerodroma na dan - OS</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>

		<%
		String icao = "";
		String dan = "";

		String greska = null;

		if (request.getAttribute("icao") != null)
		  icao = (String) request.getAttribute("icao");
		if (request.getAttribute("dan") != null)
		  dan = (String) request.getAttribute("dan");
		
		%>

		<div style="display: flex;">
			<form action="" method="post">
				<label for="icao">ICAO:</label><br> <input type="text"
					id="icao" name="icao" value=<%=icao%>><br> <label
					for="dan">Dan:</label><br> <input type="text" id="dan"
					name="dan" placeholder="dd.MM.yyyy" value=<%=dan%>>
				<button type="submit">Pretraži</button>
			</form>
		</div>
		<br>
		<%
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		List<LetAviona> letoviAviona = (List<LetAviona>) request.getAttribute("letoviAviona");

		if ((icao != null && !icao.isEmpty()) && (dan != null && !dan.isEmpty())) {
		  if (letoviAviona != null && !letoviAviona.isEmpty()) {
		%>
		<table border=1>
			<tr>
				<th>ICAO24</th>
				<th>Polazni aerodrom</th>
				<th>Dolazni aerodrom</th>
				<th>Vrijeme polaska (<%=ZoneId.systemDefault().getId()%>)
				</th>
				<th>Vrijeme dolaska (<%=ZoneId.systemDefault().getId()%>)
				</th>
				<th>Oznaka</th>
			</tr>
			<%
			for (LetAviona la : letoviAviona) {
			  long firstSeen = la.getFirstSeen();
			  long lastSeen = la.getLastSeen();
			  Instant instant1 = Instant.ofEpochSecond(firstSeen);
			  ZonedDateTime zonedDateTime1 = ZonedDateTime.ofInstant(instant1, ZoneId.systemDefault());
			  Instant instant2 = Instant.ofEpochSecond(lastSeen);
			  ZonedDateTime zonedDateTime2 = ZonedDateTime.ofInstant(instant2, ZoneId.systemDefault());
			%>
			<tr>
				<td><%=la.getIcao24()%></td>
				<td><%=la.getEstDepartureAirport()%></td>
				<td><%=la.getEstArrivalAirport()%></td>
				<td><%=zonedDateTime1.format(formatter)%></td>
				<td><%=zonedDateTime2.format(formatter)%></td>
				<td><%=la.getCallsign()%></td>
			</tr>
			<%
			}
			%>
		</table>
		<br> 
		<%
		} 
		else
		  greska = "Nema podataka za prikaz!";
		}
		%>
		<%

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