<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Početna stranica</title>
</head>
<body>
	<header>
		<h1>Početna stranica</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	
	<main>
		
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Pregled aerodroma</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenosti2aerodroma">Pregled udaljenosti između dva aerodroma</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/poruke">Pregled primljenih JMS poruka</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/meteo">Pregled meteoroloških podataka za adresu</a><br/>
	</main>

</body>
</html>