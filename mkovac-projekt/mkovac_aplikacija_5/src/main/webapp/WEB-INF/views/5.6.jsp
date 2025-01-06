<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Letovi</title>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
		<div class="container-fluid">
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br>
		
		</div>
	</header>
	
	<main class="flex-grow-1">
		<div class="container mt-5">
	
		<h1>Letovi</h1>
		
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/spremljeni1">5.6.1 - Pregled spremljenih letova #1</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/spremljeni2">5.6.2 - Pregled spremljenih letova #2</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/letovi/OpenSkyNetwork">5.6.3 - Pregled letova s OpenSkyNetwork</a><br/>  
		
		</div>
	</main>
	
	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>

</body>
</html>