
<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
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
<title>Svi aerodromi</title>

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
	
	function ocistiFilter(){
		document.getElementById("traziNaziv").value = "";
		document.getElementById("traziDrzavu").value = "";
		
		document.getElementById("filterForm").submit();
	};
	
	function preuzimanje(event) {
		
		event.preventDefault();
		
		var baseUrl = window.location.href;
		var url = baseUrl + "/preuzimanje";
		
		var icao = event.target.dataset.icao;
		
	    $.ajax({
	      type: "POST",
	      url: url, 
	      data: { icao: icao }, 
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
	
	<%
	
	String traziNaziv = (String) request.getAttribute("traziNaziv");
	String traziDrzavu = (String) request.getAttribute("traziDrzavu");
	
	if(traziNaziv==null) traziNaziv = "";
	if(traziDrzavu==null) traziDrzavu = "";
	
	%>
	
	<div class="container mt-5">
		<h1>Svi aerodromi</h1>
	
		<form id="filterForm" action="" method="post">
			<label for="traziNaziv">Naziv: </label><br>
			<input type="text" id="traziNaziv" name="traziNaziv" value=<%=traziNaziv %>><br>
			<label for="traziDrzavu">Država: </label><br>
			<input type="text" id="traziDrzavu" name="traziDrzavu" value=<%=traziDrzavu %>><br><br>
			<button type="submit" class="btn btn-primary">Filtriraj</button> &nbsp;
			<button type="button" class="btn btn-secondary" onclick="ocistiFilter()">Očisti filter</button>
		</form>
		
		<br>
		
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
		
	  	<table class="table table-hover"> 
	 
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
				<a href="#" onclick="preuzimanje(event)" id="postDownload" data-icao="<%=a.getIcao()%>">Dodaj za preuzimanje</a>
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
			  <button type="submit" class="btn btn-outline-primary">Početak</button>
			</form> &nbsp;
			<form action="" method="post">
			  <input type="hidden" name="stranica" value="<%=brStranice-1 %>" />
			  <input type="hidden" name="traziNaziv" value="<%=traziNaziv %>" />
			  <input type="hidden" name="traziDrzavu" value="<%=traziDrzavu %>" />
			  <button type="submit" class="btn btn-outline-primary">Prethodna stranica</button>
			</form> &nbsp;
			<form action="" method="post">
			  <input type="hidden" name="stranica" value="<%=brStranice+1 %>" />
			  <input type="hidden" name="traziNaziv" value="<%=traziNaziv %>" />
			  <input type="hidden" name="traziDrzavu" value="<%=traziDrzavu %>" />
			  <button type="submit" class="btn btn-outline-primary">Sljedeća stranica</button>
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
		
		<br>
	</div>

</main>
	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>
</body>
</html>