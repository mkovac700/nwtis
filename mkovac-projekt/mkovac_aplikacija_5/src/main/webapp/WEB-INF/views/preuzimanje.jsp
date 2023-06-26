<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Preuzimanje</title>

<script type="text/javascript">
	var socket = new WebSocket("ws://localhost:8080/mkovac_aplikacija_4/info");
	
	socket.onopen = function() {
		console.log("WebSocket veza uspostavljena!");
	};
	
	socket.onmessage = function(event){
		var obavijest = event.data;
		document.getElementById("obavijest").innerText = obavijest;
	};
	
	socket.onclose = function(event){
		console.log("WebSocket veza zatvorena: " + event.code);
	};
</script>

</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a> &nbsp;
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Povratak</a>
		<h1>Preuzimanje</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	
	<%
	
	String odgovor = (String) request.getAttribute("odgovor");
	
	if(odgovor != null){
	%>
	
	<%=odgovor %>

	<%} %>
	
	<p id="obavijest">Trenutni broj aerodroma za preuzimanje:</p>
</body>
</html>