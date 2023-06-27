
<%@page import="org.foi.nwtis.podaci.UdaljenostAerodrom"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Izračun udaljenosti #2</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp;<a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Povratak</a><br>
		<h1>Izračun udaljenosti do aerodroma unutar države #2</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>

		<%
		String icaoOd = "";
		String drzava = "";
		String km = "";

		String greska = null;

		if (request.getAttribute("icaoOd") != null)
		  icaoOd = (String) request.getAttribute("icaoOd");
		if (request.getAttribute("drzava") != null)
		  drzava = (String) request.getAttribute("drzava");
		if (request.getAttribute("km") != null)
		  km = (String) request.getAttribute("km");
		%>

		<div style="display: flex;">
			<form action="" method="post">
				<label for="icaoOd">ICAO od:</label><br> <input type="text"
					id="icaoOd" name="icaoOd" value=<%=icaoOd%>><br> <label
					for="drzava">Drzava:</label><br> <input type="text"
					id="drzava" name="drzava" value=<%=drzava%>><br> <label
					for="km">Km:</label><br> <input type="text" id="km" name="km"
					value=<%=km%>>
				<button type="submit">Pretraži</button>
			</form>
		</div>
		<br>
		<%
		List<UdaljenostAerodrom> udaljenosti2 =
		    (List<UdaljenostAerodrom>) request.getAttribute("udaljenosti2");
		if ((icaoOd != null && !icaoOd.isEmpty()) && (drzava != null && !drzava.isEmpty())
		    && (km != null && !km.isEmpty())) {
		  if (udaljenosti2 != null && !udaljenosti2.isEmpty()) {
		%>
		<table border=1>
			<tr>
				<th>ICAO</th>
				<th>Udaljenost (km)</th>
			</tr>
			<%
			for (UdaljenostAerodrom ua : udaljenosti2) {
			%>
			<tr>
				<td><%=ua.getIcao()%></td>
				<td><%=ua.getKm()%></td>
			</tr>
			<%
			}
			%>
		</table>
		<%
		} else
		greska = "Nema podataka za prikaz!";
		%>

		<%
		}
		if (greska != null) {
		%>
		<%=greska%>
		<%
		}
		%>
	</main>
</body>
</html>