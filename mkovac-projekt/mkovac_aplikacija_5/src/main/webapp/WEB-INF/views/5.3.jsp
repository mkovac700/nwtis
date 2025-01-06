<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Upravljanje poslužiteljem</title>
<script type="text/javascript">
	function postaviOdabraniGumb(id){
		document.getElementById("komanda").value = id;
	}
</script>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
		<div class="container-fluid">
			<a href="${pageContext.servletContext.contextPath}">Početna stranica</a>
		</div>
	</header>
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	%>
	<main class="flex-grow-1">
		<div class="container mt-5">
			<h1>Upravljanje poslužiteljem</h1>
			<br>
			<form action="" method="post">
				<input type="hidden" id="komanda" name="komanda" value="">
				<button type="submit" class="btn btn-primary" onclick="postaviOdabraniGumb('STATUS')">STATUS</button>
				<button type="submit" class="btn btn-danger" onclick="postaviOdabraniGumb('KRAJ')">KRAJ</button>
				<button type="submit" class="btn btn-success" onclick="postaviOdabraniGumb('INIT')">INIT</button>
				<button type="submit" class="btn btn-warning" onclick="postaviOdabraniGumb('PAUZA')">PAUZA</button>
				<button type="submit" class="btn btn-info" onclick="postaviOdabraniGumb('INFO DA')">INFO DA</button>
				<button type="submit" class="btn btn-secondary" onclick="postaviOdabraniGumb('INFO NE')">INFO NE</button>
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