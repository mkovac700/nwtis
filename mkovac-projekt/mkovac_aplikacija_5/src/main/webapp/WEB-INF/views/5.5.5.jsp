
<%@page import="org.foi.nwtis.podaci.UdaljenostAerodrom"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Izračun udaljenosti</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Povratak</a><br>
		<h1>Izračun udaljenosti između dva aerodroma</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
	
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
				id="icaoDo" name="icaoDo" value=<%=icaoDo%>>
			<button type="submit">Pretraži</button>
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
	</main>
</body>
</html>