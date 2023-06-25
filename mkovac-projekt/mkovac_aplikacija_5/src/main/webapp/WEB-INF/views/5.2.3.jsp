<%@page import="org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnik"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
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
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br>
		<h1>Pregled korisnika</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	String ime = (String) request.getAttribute("ime");
	String prezime = (String) request.getAttribute("prezime");
	
	if(ime==null) ime = "";
	if(prezime==null) prezime = "";
	
	List<Korisnik> korisnici = (List<Korisnik>) request.getAttribute("korisnici");
	%>
	<main>
		
		<div>
			<form id="filterForm" action="" method="post">
				<label for="ime">Ime: </label><br>
				<input type="text" id="ime" name="ime" value=<%=ime %>><br>
				<label for="prezime">Prezime: </label><br>
				<input type="text" id="prezime" name="prezime" value=<%=prezime %>><br><br>
				<button type="submit">Filtriraj</button> &nbsp;
				<button type="button" onclick="ocistiFilter()">Očisti filter</button>
			</form>
		</div>
		
		<br>
		
		<p id="obavijest">Trenutni broj korisnika:</p>
		
		<%if(korisnici != null && !korisnici.isEmpty()){ 
			int brojac = 0;
		%>
		
		<table border=1>
		
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
	</main>

</body>
</html>