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
		<a href="${pageContext.servletContext.contextPath}/mvc/korisnici">5.2 - Korisnici</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/nadzor">5.3 - Upravljanje poslužiteljem</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/poruke">5.4 - JMS poruke</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi">5.5 - Aerodromi</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi">5.6 - Letovi</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/dnevnik">5.7 - Dnevnik</a><br/> 
	</main>

</body>
</html>