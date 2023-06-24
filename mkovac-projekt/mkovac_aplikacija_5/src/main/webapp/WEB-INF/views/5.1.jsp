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
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/aerodrom">Pregled polazaka/letova s aerodroma</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/aerodromi">Pregled polazaka/letova između dva aerodroma</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/spremljeni">Pregled spremljenih letova</a><br/>
	</main>

</body>
</html>