<%@page import="org.foi.nwtis.mkovac_aplikacija_4.ws.WsKorisnici.endpoint.Korisnik"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled korisnika</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br>
		<h1>Pregled korisnika</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	
	List<Korisnik> korisnici = (List<Korisnik>) request.getAttribute("korisnici");
	%>
	<main>
		
		<div>
			<form action="" method="post">
				<label for="ime">Ime: </label> 
				<input type="text" id="ime" name="ime"> &nbsp;
				<label for="prezime">Prezime: </label>
				<input type="text" id="prezime" name="prezime"> &nbsp;
				<button type="submit">Filtriraj</button>
			</form>
		</div>
		
		<br>
		
		<%if(korisnici != null && !korisnici.isEmpty()){ 
			int brojac = 0;
		%>
		
		<table border=1>
		
		<tr><th>Rbr.</th><th>Korisnik</th><th>Ime</th><th>Prezime</th></tr>
		<%
		for(Korisnik k : korisnici){
		%>
		<tr>
			<td><%=++brojac %> </td>
			<td><%=k.getKorisnik() %></td>
			<td><%=k.getIme() %></td>
			<td><%=k.getPrezime() %></td>
		</tr>
		<%
		}
		%>
		</table>
		
		<%}else odgovor = "Nema podataka za prikaz!"; %>
		
		<br>
		
		<%if(odgovor != null) {%>
		<%=odgovor %>
		<%} %>
	</main>

</body>
</html>