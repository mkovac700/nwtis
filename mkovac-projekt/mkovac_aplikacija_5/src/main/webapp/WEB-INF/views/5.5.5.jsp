
<%@page import="org.foi.nwtis.podaci.UdaljenostAerodrom"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Izračun udaljenosti</title>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
	<div class="container-fluid">
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Povratak</a><br>
	</div>
	</header>
	<main class="flex-grow-1">
	<div class="container mt-5">
	
	<h1>Izračun udaljenosti između dva aerodroma</h1> <br>
	
	<%
		String icaoOd = "";
		String icaoDo = "";

		String greska = (String) request.getAttribute("greska");
		UdaljenostAerodrom ua = (UdaljenostAerodrom) request.getAttribute("ua");

		if (request.getAttribute("icaoOd") != null)
		  icaoOd = (String)request.getAttribute("icaoOd");
		if (request.getAttribute("icaoDo") != null)
		  icaoDo = (String)request.getAttribute("icaoDo");
	%>

		<div style="display: flex;">
			<form action="" method="post">
				<label for="icaoOd">ICAO od:</label><br> <input type="text"
					id="icaoOd" name="icaoOd" value=<%=icaoOd%>><br> <label
					for="icaoDo">ICAO do:</label><br> <input type="text"
					id="icaoDo" name="icaoDo" value=<%=icaoDo%>><br><br>
				<button type="submit" class="btn btn-primary">Pretraži</button>
			</form>
		</div>
	
	<br>
	
	<%
	if ((icaoOd != null && !icaoOd.isEmpty()) && (icaoDo != null && !icaoDo.isEmpty())){
		if(ua!=null){ 
	%>
		Udaljenost (km): <%=ua.getKm() %><br>
	<%
		}else greska = "Nema podataka za prikaz"; 
	}
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