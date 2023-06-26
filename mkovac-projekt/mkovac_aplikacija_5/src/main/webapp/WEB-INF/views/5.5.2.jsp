
<%@page import="org.foi.nwtis.mkovac_aplikacija_4.ws.WsMeteo.endpoint.MeteoPodaci"%>
<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
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
	
	<br>
	<%
	MeteoPodaci mp = (MeteoPodaci) request.getAttribute("meteo");
	if (mp != null) {
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
	<%=mp.getSunRise()%><br> Zalazak sunca:
	<%=mp.getSunSet()%><br>
	<br> Ažurirano:
	<%=mp.getLastUpdate()%><br>

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
	</main>
</body>
</html>