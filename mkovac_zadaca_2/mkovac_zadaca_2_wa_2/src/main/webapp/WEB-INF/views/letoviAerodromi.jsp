<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.time.ZonedDateTime"%>
<%@page import="java.time.Instant"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="org.foi.nwtis.rest.podaci.LetAviona"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Letovi između aerodroma na dan</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a><br>
		<h1>Letovi između aerodroma na dan</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>

		<%
		String icaoOd = "";
		String icaoDo = "";
		String dan = "";
		String greska = null;

		if (request.getParameter("icaoOd") != null)
		  icaoOd = request.getParameter("icaoOd");
		if (request.getParameter("icaoDo") != null)
		  icaoDo = request.getParameter("icaoDo");
		if (request.getParameter("dan") != null)
		  dan = request.getParameter("dan");

		int brStranice = 1;

		if (request.getParameter("stranica") != null) { //odBroja
		  brStranice = Integer.parseInt(request.getParameter("stranica"));//odBroja
		  if (brStranice < 1)
		    response.sendRedirect(request.getContextPath() + "/mvc/letovi/aerodromi?icaoOd=" + icaoOd
		    + "&icaoDo=" + icaoDo + "&dan=" + dan + "&stranica=1");
		}
		%>

		<div style="display: flex;">
			<form action="" method="get">
				<label for="icaoOd">ICAO od:</label><br> <input type="text"
					id="icaoOd" name="icaoOd" value=<%=icaoOd%>><br> <label
					for="icaoOd">ICAO do:</label><br> <input type="text"
					id="icaoDo" name="icaoDo" value=<%=icaoDo%>><br> <label
					for="dan">Dan:</label><br> <input type="text" id="dan"
					name="dan" placeholder="dd.MM.yyyy" value=<%=dan%>>
				<button type="submit">Pretraži</button>
			</form>
		</div>
		<br>
		<%
		Gson gson = new Gson();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		List<LetAviona> letoviAviona = (List<LetAviona>) request.getAttribute("letoviAviona");

		if ((icaoOd != null && !icaoOd.isEmpty()) && (icaoDo != null && !icaoDo.isEmpty())
		    && (dan != null && !dan.isEmpty())) {

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
				<th>Opcije</th>
			</tr>
			<%
			for (LetAviona la : letoviAviona) {
			  String jsonLetAviona = gson.toJson(la);
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
				<td>
					<form action="${pageContext.servletContext.contextPath}/mvc/letovi"
						method="post">
						<input type="hidden" name="url"
							value='/mvc/letovi/aerodromi?icaoOd=<%=icaoOd%>&icaoDo=<%=icaoDo%>&dan=<%=dan%>&stranica=<%=brStranice%>' />
						<input type="hidden" name="jsonLetAviona"
							value='<%=jsonLetAviona%>' />
						<button type="submit">Spremi</button>
					</form>
				</td>
			</tr>
			<%
			}
			%>
		</table>
		<br> Stranica:
		<%=brStranice%><br> <br>
		<div style="display: flex;">
			<!-- justify-content: center;" -->
			<form action="" method="get">
				<input type="hidden" name="icaoOd" value="<%=icaoOd%>" /> <input
					type="hidden" name="icaoDo" value="<%=icaoDo%>" /> <input
					type="hidden" name="dan" value="<%=dan%>" /> <input type="hidden"
					name="stranica" value="1" />
				<button type="submit">Početak</button>
			</form>
			<form action="" method="get">
				<input type="hidden" name="icaoOd" value="<%=icaoOd%>" /> <input
					type="hidden" name="icaoDo" value="<%=icaoDo%>" /> <input
					type="hidden" name="dan" value="<%=dan%>" /> <input type="hidden"
					name="stranica" value="<%=brStranice - 1%>" />
				<button type="submit">Prethodna stranica</button>
			</form>
			<form action="" method="get">
				<input type="hidden" name="icaoOd" value="<%=icaoOd%>" /> <input
					type="hidden" name="icaoDo" value="<%=icaoDo%>" /> <input
					type="hidden" name="dan" value="<%=dan%>" /> <input type="hidden"
					name="stranica" value="<%=brStranice + 1%>" />
				<button type="submit">Sljedeća stranica</button>
			</form>
		</div>
		<%
		} else {
		if (brStranice != 1)
		  response.sendRedirect(request.getContextPath() + "/mvc/letovi/aerodromi?icaoOd=" + icaoOd
		  + "&icaoDo=" + icaoDo + "&dan=" + dan + "&stranica=1");
		else
		  greska = "Nema podataka za prikaz!";
		}
		%>

		<%
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