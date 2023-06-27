<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Letovi</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br>
		<h1>Letovi</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	
	<main>
		
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/spremljeni1">5.6.1 - Pregled spremljenih letova #1</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/spremljeni2">5.6.2 - Pregled spremljenih letova #2</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/OpenSkyNetwork">5.6.3 - Pregled letova s OpenSkyNetwork</a><br/>  
	</main>

</body>
</html>