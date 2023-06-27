<%@page import="org.foi.nwtis.podaci.Dnevnik"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dnevnik</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a><br>
		<h1>Dnevnik</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>

	<main>

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
				<button type="submit">Pretraži</button>
			</form>
		</div>
		<br>

		<%
		List<Dnevnik> dnevnik = (List<Dnevnik>) request.getAttribute("dnevnik");

		if (vrsta != null && !vrsta.isEmpty()) {
		  if (dnevnik != null && !dnevnik.isEmpty()) {
		%>
		<table border=1>
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
				<button type="submit">Početak</button>
			</form>
			<form action="" method="post">
				<input type="hidden" name="vrsta" value="<%=vrsta%>" /> <input
					type="hidden" name="stranica" value="<%=brStranice - 1%>" />
				<button type="submit">Prethodna stranica</button>
			</form>
			<form action="" method="post">
				<input type="hidden" name="vrsta" value="<%=vrsta%>" /> <input
					type="hidden" name="stranica" value="<%=brStranice + 1%>" />
				<button type="submit">Sljedeća stranica</button>
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

	</main>

</body>
</html>