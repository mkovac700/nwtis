
<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Svi aerodromi</title>

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
	
	function ocistiFilter(){
		document.getElementById("traziNaziv").value = "";
		document.getElementById("traziDrzavu").value = "";
		
		document.getElementById("filterForm").submit();
	};
</script>

</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Povratak</a><br> 
		<h1>Svi aerodromi</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
	
	<%
	
	String traziNaziv = (String) request.getAttribute("traziNaziv");
	String traziDrzavu = (String) request.getAttribute("traziDrzavu");
	
	if(traziNaziv==null) traziNaziv = "";
	if(traziDrzavu==null) traziDrzavu = "";
	
	%>
	
	<div>
		<form id="filterForm" action="" method="post">
			<label for="traziNaziv">Naziv: </label><br>
			<input type="text" id="traziNaziv" name="traziNaziv" value=<%=traziNaziv %>><br>
			<label for="traziDrzavu">Drzava: </label><br>
			<input type="text" id="traziDrzavu" name="traziDrzavu" value=<%=traziDrzavu %>><br><br>
			<button type="submit">Filtriraj</button> &nbsp;
			<button type="button" onclick="ocistiFilter()">Očisti filter</button>
		</form>
	</div>
	
	<p id="obavijest">Trenutni broj aerodroma za preuzimanje:</p>
	
<%
	int brStranice = 1; 
	String greska = (String) request.getAttribute("greska");
	
	if(request.getParameter("stranica") != null){ 
	  try{
	  	brStranice = Integer.parseInt(request.getParameter("stranica"));
	  }
	  catch(NumberFormatException e){
	    brStranice = 1;
	  }
	  if(brStranice < 1) 
	    response.sendRedirect(request.getContextPath()+"/mvc/aerodromi/svi");
	}
	
	List<Aerodrom> aerodromi = (List<Aerodrom>) request.getAttribute("aerodromi");
	

	if(aerodromi != null && !aerodromi.isEmpty()){
%>
	  	<table border=1> 
	 
		<tr><th>Oznaka</th><th>Naziv</th><th>Država</th><th>Lokacija</th><th>Opcije</th></tr>
		<%
		for(Aerodrom a : aerodromi){ 
		%>
		<tr>
			<td><%=a.getIcao() %></td>
			<td><%=a.getNaziv() %></td>
			<td><%=a.getDrzava() %></td>
			<td><%=a.getLokacija().getLatitude() %>, <%=a.getLokacija().getLongitude() %></td>
			<td>
				<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/<%= a.getIcao() %>">Pregled aerodroma</a>
				<a href="#" onclick="preuzimanje('<%=a.getIcao() %>')">Dodaj za preuzimanje JS</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/<%= a.getIcao() %>/preuzimanje">Dodaj za preuzimanje</a>
			</td>
		</tr>
		<%
		}
		%>
		</table>
		<br>
		Stranica: <%=brStranice %> <br>
		<br>
		<div style="display: flex;"> <!-- justify-content: center;" -->
			<form action="" method="post">
			  <input type="hidden" name="stranica" value="1" />
			  <input type="hidden" name="traziNaziv" value="<%=traziNaziv %>" />
			  <input type="hidden" name="traziDrzavu" value="<%=traziDrzavu %>" />
			  <button type="submit">Početak</button>
			</form>
			<form action="" method="post">
			  <input type="hidden" name="stranica" value="<%=brStranice-1 %>" />
			  <input type="hidden" name="traziNaziv" value="<%=traziNaziv %>" />
			  <input type="hidden" name="traziDrzavu" value="<%=traziDrzavu %>" />
			  <button type="submit">Prethodna stranica</button>
			</form>
			<form action="" method="post">
			  <input type="hidden" name="stranica" value="<%=brStranice+1 %>" />
			  <input type="hidden" name="traziNaziv" value="<%=traziNaziv %>" />
			  <input type="hidden" name="traziDrzavu" value="<%=traziDrzavu %>" />
			  <button type="submit">Sljedeća stranica</button>
			</form>
		</div>
<% 
	}
	else{
	  if(brStranice != 1)
	  	response.sendRedirect(request.getContextPath()+"/mvc/aerodromi/svi");
	  else greska = "Nema podataka za prikaz!";
	}
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