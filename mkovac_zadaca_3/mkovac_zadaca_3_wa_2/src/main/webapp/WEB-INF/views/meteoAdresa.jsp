<%@page import="java.net.URLDecoder"%>
<%@page
	import="org.foi.nwtis.mkovac.zadaca_3.ws.WsMeteo.endpoint.MeteoPodaci"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Meteo - adresa</title>
</head>
<body>
	<%
	String adresa = "";
	if (request.getParameter("adresa") != null) {
	  adresa = request.getQueryString();
	  System.out.println("Adresa: " + adresa);
	  adresa = adresa.substring("adresa=".length());
	  System.out.println("Adresa: " + adresa);
	  adresa = URLDecoder.decode(adresa, "UTF-8");
	  System.out.println("Adresa: " + adresa);

	  // 	  adresa = request.getParameter("adresa").replaceAll("\\+", " ");
	  // 	  adresa = URLDecoder.decode(adresa,"UTF-8");

	  // 	  adresa = request.getParameter("adresa");
	  // 	  adresa = URLDecoder.decode(adresa,"UTF-8");
	}
	%>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a><br>
		<h1>Pregled meteo podataka</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
		<div style="display: flex;">
			<form action="" method="get">
				<label for="adresa">Adresa:</label><br> <input type="text"
					id="adresa" name="adresa" size="40" value='<%=adresa%>'>&nbsp;
				<button type="submit">Dohvati</button>
			</form>
		</div>
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
		<br> <br> Temperatura zraka:
		<%=mp.getTemperatureValue()%>
		<%=mp.getTemperatureUnit()%>
		<br> Najviša temperatura zraka:
		<%=mp.getTemperatureMax()%>
		<%=mp.getTemperatureUnit()%>
		<br> Najniža temperatura zraka:
		<%=mp.getTemperatureMin()%>
		<%=mp.getTemperatureUnit()%>
		<br> <br> Relativna vlažnost:
		<%=mp.getHumidityValue()%>
		<%=mp.getHumidityUnit()%><br> <br> Tlak zraka:
		<%=mp.getPressureValue()%>
		<%=mp.getPressureUnit()%><br> <br> Izlazak sunca:
		<%=mp.getSunRise()%><br> Zalazak sunca:
		<%=mp.getSunSet()%><br> <br> Ažurirano:
		<%=mp.getLastUpdate()%><br>

		<%
		} else
		greska = "Nema podataka za prikaz!";
		%>

		<%
		if (greska != null && request.getParameter("adresa") != null) {
		%>
		<%=greska%>
		<%
		}
		%>
	</main>
</body>
</html>