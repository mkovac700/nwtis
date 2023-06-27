<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Prijava</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/korisnici">Povratak</a><br> 
		<h1>Prijava</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	%>
	<main>
		
		<div>
			<form action="" method="post">
				<label for="korime">Korisničko ime: </label><br>
				<input type="text" id="korime" name="korime"><br>
				<label for="lozinka">Lozinka: </label><br>
				<input type="password" id="lozinka" name="lozinka"><br><br>
				<button type="submit">Prijavi se</button>
			</form>
		</div>
		
		<br>
		
		<%if(odgovor != null) {%>
		<%=odgovor %>
		<%} %>
	</main>

</body>
</html>