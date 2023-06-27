
<%@page import="org.foi.nwtis.podaci.UdaljenostAerodrom"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Izračun udaljenosti #1</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp;<a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Povratak</a><br>
		<h1>Izračun udaljenosti do aerodroma unutar države #1</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>

		<%
		String icaoOd = "";
		String icaoDo = "";

		String greska = null;

		if (request.getAttribute("icaoOd") != null)
		  icaoOd = (String) request.getAttribute("icaoOd");
		if (request.getAttribute("icaoDo") != null)
		  icaoDo = (String) request.getAttribute("icaoDo");
		%>

		<div style="display: flex;">
			<form action="" method="post">
				<label for="icaoOd">ICAO od:</label><br> <input type="text"
					id="icaoOd" name="icaoOd" value=<%=icaoOd%>><br> <label
					for="icaoDo">ICAO do:</label><br> <input type="text"
					id="icaoDo" name="icaoDo" value=<%=icaoDo%>>
				<button type="submit">Pretraži</button>
			</form>
		</div>
		<br>
		<%
		List<UdaljenostAerodrom> udaljenosti1 =
		    (List<UdaljenostAerodrom>) request.getAttribute("udaljenosti1");
		if ((icaoOd != null && !icaoOd.isEmpty()) && (icaoDo != null && !icaoDo.isEmpty())) {
		  if (udaljenosti1 != null && !udaljenosti1.isEmpty()) {
		%>
		<table border=1>
			<tr>
				<th>ICAO</th>
				<th>Udaljenost (km)</th>
			</tr>
			<%
			for (UdaljenostAerodrom ua : udaljenosti1) {
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