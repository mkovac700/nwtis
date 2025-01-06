
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.foi.nwtis.mkovac_aplikacija_4.ws.WsMeteo.endpoint.MeteoPodaci"%>
<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Pregled aerodroma</title>
</head>
<body class="d-flex flex-column min-vh-100">
<%
	String icao = (String) request.getAttribute("icao");
	String greska = (String) request.getAttribute("greska");
	Aerodrom aerodrom = (Aerodrom) request.getAttribute("aerodrom");
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
	
	String sunrise = "";
	String sunset = "";
	String lastUpdate = "";
	
	%>
	<header>
		<div class="container-fluid">
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Povratak</a><br>
		
		</div>
	</header>
	<main class="flex-grow-1">
	<div class="container mt-5">
	
	<h1>Pregled aerodroma <%=icao %></h1><br>
	
	<%if(aerodrom!=null){ %>
		ICAO: <%=aerodrom.getIcao() %> <br> Naziv: <%=aerodrom.getNaziv() %><br>
		Država: <%=aerodrom.getDrzava() %> <br> Lokacija:
		<%=aerodrom.getLokacija().getLatitude() %>, <%=aerodrom.getLokacija().getLongitude() %>
	<%}else greska = "Nema podataka za prikaz"; %>
	
	<br>
	<%
	MeteoPodaci mp = (MeteoPodaci) request.getAttribute("meteo");
	if (mp != null) {
	  
	sunrise = formatter.format(mp.getSunRise().toGregorianCalendar().getTime());
	sunset = formatter.format(mp.getSunSet().toGregorianCalendar().getTime());
	lastUpdate = formatter.format(mp.getLastUpdate().toGregorianCalendar().getTime());
	%>
	
	
	<img
		src="https://openweathermap.org/img/wn/<%=mp.getWeatherIcon()%>@2x.png"
		alt="weatherIcon"> <br> Trenutno vrijeme:
	<%=mp.getCloudsName()%>
	<br>
	<br> Temperatura zraka:
	<%=mp.getTemperatureValue()%>
	<%=mp.getTemperatureUnit()%>
	<br> Najviša temperatura zraka:
	<%=mp.getTemperatureMax()%>
	<%=mp.getTemperatureUnit()%>
	<br> Najniža temperatura zraka:
	<%=mp.getTemperatureMin()%>
	<%=mp.getTemperatureUnit()%>
	<br>
	<br> Relativna vlažnost:
	<%=mp.getHumidityValue()%>
	<%=mp.getHumidityUnit()%><br>
	<br> Tlak zraka:
	<%=mp.getPressureValue()%>
	<%=mp.getPressureUnit()%><br>
	<br> Izlazak sunca:
	<%=sunrise %><br> Zalazak sunca:
	<%=sunset%><br>
	<br> Ažurirano:
	<%=lastUpdate%><br>

	<%
	}else greska = "Nema meteo podataka za prikaz!";
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
	
	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>
</body>
</html>