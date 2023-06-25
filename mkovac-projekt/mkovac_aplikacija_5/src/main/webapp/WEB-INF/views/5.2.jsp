<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Korisnici</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br>
		<h1>Korisnici</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	
	<main>
		
		<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/registracija">5.2.1 - Registracija</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/prijava">5.2.2 - Prijava</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pregled">5.2.3 - Pregled</a><br/> 
	</main>

</body>
</html>