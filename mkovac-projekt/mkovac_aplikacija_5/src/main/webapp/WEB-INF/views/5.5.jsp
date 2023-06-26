<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br>
		<h1>Aerodromi</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	
	<main>
		
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">5.5.1 - Svi aerodromi</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/preuzimanje">5.5.3 - Aerodromi za preuzimanje</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenosti2aerodroma">5.5.4 - Pregled udaljenosti između dva aerodroma</a><br/> 
	</main>

</body>
</html>