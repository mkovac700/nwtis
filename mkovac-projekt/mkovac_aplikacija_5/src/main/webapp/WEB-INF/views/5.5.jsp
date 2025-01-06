<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Aerodromi</title>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
		<div class="container-fluid">
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br>
		</div>
	</header>
	
	<main class="flex-grow-1">
		<div class="container mt-5">
	
		<h1>Aerodromi</h1>
		
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">5.5.1 - Svi aerodromi</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/preuzimanje">5.5.3 - Aerodromi za preuzimanje</a><br/> 
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenosti2aerodroma">5.5.4 - Pregled udaljenosti između dva aerodroma</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenost2aerodroma/izracun">5.5.5 - Izračun udaljenosti između dva aerodroma</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenosti2aerodroma/izracun1">5.5.6 - Izračun udaljenosti do aerodroma unutar države #1</a><br/>
		<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenosti2aerodroma/izracun2">5.5.7 - Izračun udaljenosti do aerodroma unutar države #2</a><br/>   
		
		</div>
	</main>

	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>
</body>
</html>