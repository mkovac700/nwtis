<%@page import="org.foi.nwtis.podaci.UdaljenostAerodrom"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenost do svih aerodroma</title>
</head>
<body>
	<%
	String icao = (String) request.getAttribute("icao");
	String greska = (String) request.getAttribute("greska");
	%>

	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a>&nbsp; <a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">Povratak</a><br>
		<h1>
			Pregled udaljenosti do svih aerodroma od aerodroma
			<%=icao%></h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>

		<%
		int brStranice = 1;

		if (request.getParameter("stranica") != null) {
		  try {
		    brStranice = Integer.parseInt(request.getParameter("stranica"));
		  } catch (NumberFormatException e) {
		    brStranice = 1;
		  }
		  if (brStranice < 1)
		    response.sendRedirect(
		    request.getContextPath() + "/mvc/aerodromi/" + icao + "/udaljenosti?stranica=1");
		}

		List<UdaljenostAerodrom> udaljenostAerodromi =
		    (List<UdaljenostAerodrom>) request.getAttribute("udaljenostAerodromi");

		if (udaljenostAerodromi != null && !udaljenostAerodromi.isEmpty()) {
		%>

		<table border=1>

			<tr>
				<th>Oznaka</th>
				<th>Udaljenost (km)</th>
			</tr>
			<%
			for (UdaljenostAerodrom a : udaljenostAerodromi) {
			%>
			<tr>
				<td><%=a.icao()%></td>
				<td><%=a.km()%></td>
			</tr>
			<%
			}
			%>
		</table>
		<br> Stranica:
		<%=brStranice%><br> <br>
		<div style="display: flex;">
			<!-- justify-content: center;" -->
			<form action="" method="get">
				<input type="hidden" name="stranica" value="1" />
				<button type="submit">Početak</button>
			</form>
			<form action="" method="get">
				<input type="hidden" name="stranica" value="<%=brStranice - 1%>" />
				<button type="submit">Prethodna stranica</button>
			</form>
			<form action="" method="get">
				<input type="hidden" name="stranica" value="<%=brStranice + 1%>" />
				<button type="submit">Sljedeća stranica</button>
			</form>
		</div>
		<%
		} else {
		if (brStranice != 1)
		  response.sendRedirect(
		  request.getContextPath() + "/mvc/aerodromi/" + icao + "/udaljenosti?stranica=1");
		else
		  greska = "Nema podataka za prikaz!";
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