<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Registracija</title>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
		<div class="container-fluid">
			<a href="${pageContext.servletContext.contextPath}">Početna stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/korisnici">Povratak</a><br> 
		</div>
	</header>
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	%>
	<main class="flex-grow-1">
		
		<div class="container mt-5">
			<h1>Registracija</h1>
		
			<form action="" method="post">
				<label for="ime">Ime: </label><br>
				<input type="text" id="ime" name="ime"><br>
				<label for="prezime">Prezime: </label><br>
				<input type="text" id="prezime" name="prezime"><br>
				<label for="korime">Korisničko ime: </label><br>
				<input type="text" id="korime" name="korime"><br>
				<label for="lozinka">Lozinka: </label><br>
				<input type="password" id="lozinka" name="lozinka"><br><br>
				<button type="submit" class="btn btn-primary">Registriraj se</button>
			</form>
			
			<br>
		
			<%if(odgovor != null) {%>
			<%=odgovor %>
			<%} %>
		</div>
	</main>

	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>
</body>
</html>