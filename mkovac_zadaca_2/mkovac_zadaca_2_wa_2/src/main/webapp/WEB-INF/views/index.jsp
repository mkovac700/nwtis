<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Početna</title>
</head>
<body>
	<%@ include file="zaglavlje.jsp"%>
	<main>
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Pregled aerodroma</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenosti2aerodroma">Pregled udaljenosti između dva aerodroma</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/letoviAerodrom">Pregled polazaka/letova s aerodroma</a><br/>
		<a href="${pageContext.servletContext.contextPath}">Pregled polazaka/letova između dva aerodroma</a><br/>
		<a href="${pageContext.servletContext.contextPath}">Pregled spremljenih letova</a><br/>
	</main>

</body>
</html>