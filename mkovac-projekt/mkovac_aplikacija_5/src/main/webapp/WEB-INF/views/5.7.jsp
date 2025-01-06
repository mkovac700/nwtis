<%@page import="org.foi.nwtis.podaci.Dnevnik"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Dnevnik</title>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
		<div class="container-fluid">
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a><br>
		</div>
	</header>

	<main class="flex-grow-1">
	
		<div class="container mt-5">
	
		<h1>Dnevnik</h1>

		<%
		String vrsta = "";
		if (request.getAttribute("vrsta") != null) {
		  vrsta = (String) request.getAttribute("vrsta");
		}

		String greska = null;

		int brStranice = 1;

		if (request.getAttribute("stranica") != null) { //odBroja
		  brStranice = Integer.parseInt((String) request.getAttribute("stranica"));//odBroja
		  if (brStranice < 1)
		    response.sendRedirect(request.getContextPath() + "/mvc/dnevnik");
		}
		%>

		<div style="display: flex;">
			<form action="" method="post">
				<label for="vrsta">Odaberite vrstu:</label> <select name="vrsta">
					<option value="AP2" ${vrsta.equals("AP2") ? 'selected' : '' }>AP2</option>
					<option value="AP4" ${vrsta.equals("AP4") ? 'selected' : '' }>AP4</option>
					<option value="AP5" ${vrsta.equals("AP5") ? 'selected' : '' }>AP5</option>
				</select>
				<button type="submit" class="btn btn-primary btn-sm">Pretraži</button>
			</form>
		</div>
		<br>

		<%
		List<Dnevnik> dnevnik = (List<Dnevnik>) request.getAttribute("dnevnik");

		if (vrsta != null && !vrsta.isEmpty()) {
		  if (dnevnik != null && !dnevnik.isEmpty()) {
		%>
		<table class="table table-hover">
			<tr>
				<th>Zahtjev</th>
				<th>Metoda</th>
				<th>Vrsta</th>
				<th>Vremenska oznaka</th>
			</tr>

			<%
			for (Dnevnik d : dnevnik) {
			%>
			<tr>
				<td><%=d.getZahtjev()%></td>
				<td><%=d.getMetoda()%></td>
				<td><%=d.getVrsta()%></td>
				<td><%=d.getVremenskaOznaka()%></td>
			</tr>
			<%
			}
			%>
		</table>
		<br> Stranica:
		<%=brStranice%><br> <br>
		<div style="display: flex;">
			<form action="" method="post">
				<input type="hidden" name="vrsta" value="<%=vrsta%>" /> <input
					type="hidden" name="stranica" value="1" />
				<button type="submit" class="btn btn-outline-primary">Početak</button>
			</form> &nbsp;
			<form action="" method="post">
				<input type="hidden" name="vrsta" value="<%=vrsta%>" /> <input
					type="hidden" name="stranica" value="<%=brStranice - 1%>" />
				<button type="submit" class="btn btn-outline-primary">Prethodna stranica</button>
			</form> &nbsp;
			<form action="" method="post">
				<input type="hidden" name="vrsta" value="<%=vrsta%>" /> <input
					type="hidden" name="stranica" value="<%=brStranice + 1%>" />
				<button type="submit" class="btn btn-outline-primary">Sljedeća stranica</button>
			</form>
		</div>
		<%
		} else {
		if (brStranice != 1)
		  response.sendRedirect(request.getContextPath() + "/mvc/dnevnik");
		else
		  greska = "Nema podataka za prikaz!";
		}
		}
		%>

		<%
		if (greska != null) {
		%>
		<%=greska%>
		<%
		}
		%>
		
		<br>
		
		</div>

	</main>
	
	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>

</body>
</html>