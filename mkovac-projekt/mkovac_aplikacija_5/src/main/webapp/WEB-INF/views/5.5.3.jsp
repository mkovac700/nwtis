
<%@page import="org.foi.nwtis.mkovac_aplikacija_4.ws.WsAerodromi.endpoint.AerodromLetovi"%>
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
<body class="d-flex flex-column min-vh-100">
	<header>
	<div class="container-fluid">
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Povratak</a><br> 
	</div>
	</header>
	<main class="flex-grow-1">
	<div class="container mt-5">
	<h1>Aerodromi za preuzimanje</h1> <br>
	
	<p id="obavijest">Trenutni broj aerodroma za preuzimanje:</p>
	
<%
	String greska = (String) request.getAttribute("greska");
	
	List<AerodromLetovi> aerodromi = (List<AerodromLetovi>) request.getAttribute("aerodromi");
	

	if(aerodromi != null && !aerodromi.isEmpty()){
%>
	  	<table class="table table-hover"> 
	 
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
</div>
</main>
	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>
</body>
</html>