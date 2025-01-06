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
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Letovi s aerodroma u intervalu</title>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
		<div class="container-fluid">
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/letovi">Povratak</a><br> 
		</div>
	</header>
	<main class="flex-grow-1">
		
		<div class="container mt-5">
	
		<h1>Letovi s aerodroma u intervalu</h1>

		<%
		String icao = "";
		String danOd = "";
		String danDo = "";

		String greska = null;

		if (request.getAttribute("icao") != null)
		  icao = (String) request.getAttribute("icao");
		if (request.getAttribute("danOd") != null)
		  danOd = (String) request.getAttribute("danOd");
		if (request.getAttribute("danDo") != null)
		  danDo = (String) request.getAttribute("danDo");

		int brStranice = 1;

		if (request.getAttribute("stranica") != null) { //odBroja
		  brStranice = Integer.parseInt((String) request.getAttribute("stranica"));//odBroja
		  if (brStranice < 1)
		    response.sendRedirect(request.getContextPath() + "/mvc/letovi/aerodrom?icao=" + icao + "&dan="
		    + danOd + "&stranica=1");
		}
		%>

		<div style="display: flex;">
			<form action="" method="post">
				<label for="icao">ICAO:</label><br> <input type="text"
					id="icao" name="icao" value=<%=icao%>><br> <label
					for="danOd">Dan od:</label><br> <input type="text" id="danOd"
					name="danOd" placeholder="dd.MM.yyyy" value=<%=danOd%>><br> <label
					for="danOd">Dan do:</label><br> <input type="text" id="danDo"
					name="danDo" placeholder="dd.MM.yyyy" value=<%=danDo%>><br><br>
				<button type="submit" class="btn btn-primary">Pretraži</button>
			</form>
		</div>
		<br>
		<%
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		List<LetAviona> letoviAviona = (List<LetAviona>) request.getAttribute("letoviAviona");

		if ((icao != null && !icao.isEmpty()) && (danOd != null && !danOd.isEmpty()) && (danDo != null && !danDo.isEmpty())) {
		  if (letoviAviona != null && !letoviAviona.isEmpty()) {
		%>
		<table class="table table-hover">
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
		<br> Stranica:
		<%=brStranice%><br> <br>
		<div style="display: flex;">
			<form action="" method="post">
				<input type="hidden" name="icao" value="<%=icao%>" /> <input
					type="hidden" name="danOd" value="<%=danOd%>" /> <input
					type="hidden" name="danDo" value="<%=danDo%>" />
					<input type="hidden" name="stranica" value="1" />
				<button type="submit" class="btn btn-outline-primary">Početak</button>
			</form> &nbsp;
			<form action="" method="post">
				<input type="hidden" name="icao" value="<%=icao%>" /> <input
					type="hidden" name="dan" value="<%=danOd%>" /> <input
					type="hidden" name="danDo" value="<%=danDo%>" />
					<input type="hidden" name="stranica" value="<%=brStranice - 1%>" />
				<button type="submit" class="btn btn-outline-primary">Prethodna stranica</button>
			</form> &nbsp;
			<form action="" method="post">
				<input type="hidden" name="icao" value="<%=icao%>" /> <input
					type="hidden" name="dan" value="<%=danOd%>" /> <input
					type="hidden" name="danDo" value="<%=danDo%>" />
					<input type="hidden" name="stranica" value="<%=brStranice + 1%>" />
				<button type="submit" class="btn btn-outline-primary">Sljedeća stranica</button>
			</form>
		</div>
		<%
		} else {
		if (brStranice != 1)
		  response.sendRedirect(request.getContextPath() + "/mvc/letovi/aerodrom?icao=" + icao + "&dan="
		  + danOd + "&stranica=1");
		else
		  greska = "Nema podataka za prikaz!";
		}
		%>

		<%
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
		</div>
	</main>
</body>
	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>
</html>