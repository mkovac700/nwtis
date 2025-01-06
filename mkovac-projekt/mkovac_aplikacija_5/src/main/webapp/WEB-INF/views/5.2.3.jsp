<%@page import="org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnik"%>
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
<title>Pregled korisnika</title>

<script type="text/javascript">
	var socket = new WebSocket("ws://localhost:8080/mkovac_aplikacija_4/info");
	
	socket.onopen = function() {
		console.log("WebSocket veza uspostavljena!");
		
		var message = "dajBrojKorisnika";
	    socket.send(message);
	};
	
	socket.onmessage = function(event){
		var obavijest = event.data;
		document.getElementById("obavijest").innerText = obavijest;
	};
	
	socket.onclose = function(event){
		console.log("WebSocket veza zatvorena: " + event.code);
	};
	
	function ocistiFilter(){
		document.getElementById("ime").value = "";
		document.getElementById("prezime").value = "";
		
		document.getElementById("filterForm").submit();
	};
</script>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
		<div class="container-fluid">
			<a href="${pageContext.servletContext.contextPath}">Početna stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/korisnici">Povratak</a><br> 
		</div>
	</header>
	
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	String ime = (String) request.getAttribute("ime");
	String prezime = (String) request.getAttribute("prezime");
	
	if(ime==null) ime = "";
	if(prezime==null) prezime = "";
	
	List<Korisnik> korisnici = (List<Korisnik>) request.getAttribute("korisnici");
	%>
	
	<main class="flex-grow-1">
		
		<div class="container mt-5">
			<h1>Pregled korisnika</h1>
		
			<form id="filterForm" action="" method="post">
				<label for="ime">Ime: </label><br>
				<input type="text" id="ime" name="ime" value=<%=ime %>><br>
				<label for="prezime">Prezime: </label><br>
				<input type="text" id="prezime" name="prezime" value=<%=prezime %>><br><br>
				<button type="submit" class="btn btn-primary">Filtriraj</button> &nbsp;
				<button type="button" class="btn btn-secondary" onclick="ocistiFilter()">Očisti filter</button>
			</form>
			
			<br>
		
			<p id="obavijest">Trenutni broj korisnika:</p>
			
			<%if(korisnici != null && !korisnici.isEmpty()){ 
				int brojac = 0;
			%>
			
			<table class="table table-hover">
			
			<tr><th>Rbr.</th><th>Korisnik</th><th>Ime</th><th>Prezime</th></tr>
			<%
			for(Korisnik k : korisnici){
			%>
			<tr>
				<td><%=++brojac %> </td>
				<td><%=k.getKorisnik() %></td>
				<td><%=k.getIme() %></td>
				<td><%=k.getPrezime() %></td>
			</tr>
			<%
			}
			%>
			</table>
			
			<%}else odgovor = "Nema podataka za prikaz!"; %>
			
			<br>
			
			<%if(odgovor != null) {%>
			<%=odgovor %>
			<%} %>
		</div>
		
	</main>
	
	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>

</body>
</html>