
<%@page import="org.foi.nwtis.mkovac_aplikacija_4.ws.WsAerodromi.endpoint.AerodromLetovi"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi za preuzimanje</title>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script type="text/javascript">
	var socket = new WebSocket("ws://localhost:8080/mkovac_aplikacija_4/info");
	
	socket.onopen = function() {
		console.log("WebSocket veza uspostavljena!");
		
		var message = "dajBrojAerodromaZaPreuzimanje";
	    socket.send(message);
	};
	
	socket.onmessage = function(event){
		var obavijest = event.data;
		document.getElementById("obavijest").innerText = obavijest;
	};
	
	socket.onclose = function(event){
		console.log("WebSocket veza zatvorena: " + event.code);
	};
	
	function preuzimanje(event) {
		
		event.preventDefault();
		
		var baseUrl = window.location.href;
		var url = baseUrl + "/status";
		
		var icao = event.target.dataset.icao;
		var akcija = event.target.dataset.akcija;
		
	    $.ajax({
	      type: "POST",
	      url: url, 
	      data: { 
	    	  icao: icao,
	    	  akcija: akcija
	      }, 
	      success: function (response) {
	    	  console.log(response.text)
	      },
	      error: function (xhr, status, error) {
	        console.log(error)
	      }
	    });
	};
	
</script>

</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Povratak</a><br> 
		<h1>Aerodromi za preuzimanje</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
	
	<p id="obavijest">Trenutni broj aerodroma za preuzimanje:</p>
	
<%
	String greska = (String) request.getAttribute("greska");
	
	List<AerodromLetovi> aerodromi = (List<AerodromLetovi>) request.getAttribute("aerodromi");
	

	if(aerodromi != null && !aerodromi.isEmpty()){
%>
	  	<table border=1> 
	 
		<tr><th>Oznaka</th><th>Naziv</th><th>Država</th><th>Lokacija</th><th>Preuzimanje</th><th>Opcije</th></tr>
		<%
		for(AerodromLetovi a : aerodromi){ 
		%>
		<tr>
			<td><%=a.getAerodrom().getIcao() %></td>
			<td><%=a.getAerodrom().getNaziv() %></td>
			<td><%=a.getAerodrom().getDrzava() %></td>
			<td><%=a.getAerodrom().getLokacija().getLatitude() %>, <%=a.getAerodrom().getLokacija().getLongitude() %></td>
			<td><%=a.isPreuzimanje() ? "DA" : "PAUZA" %></td>
			<td>
				<a href="#" onclick="preuzimanje(event)" id="postDownload" data-icao="<%=a.getAerodrom().getIcao()%>" data-akcija="<%=!a.isPreuzimanje()%>">Promijeni status</a>
			</td>
		</tr>
		<%
		}
		%>
		</table>
		<br>
<% 
	}else greska = "Nema podataka za prikaz!";
%>
	<%
	if(greska!= null){
	  %>
	  	<%=greska %>
	  <%
	}
	%>

</main>
</body>
</html>