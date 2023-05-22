<%@page
	import="org.foi.nwtis.mkovac.zadaca_3.ws.WsMeteo.endpoint.MeteoPodaci"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Meteo - aerodrom</title>
</head>
<body>
	<%
	String icao = (String)request.getAttribute("icao");
	%>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a><br>
		<h1>Pregled meteo podataka za aerodrom <%=icao %></h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
		<br>
		<%
		MeteoPodaci mp = (MeteoPodaci) request.getAttribute("meteo");
		String greska = (String) request.getAttribute("greska");
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
		}else greska = "Nema podataka za prikaz!";
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