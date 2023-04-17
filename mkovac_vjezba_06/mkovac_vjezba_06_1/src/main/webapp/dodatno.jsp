<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenosti aerodroma 2</title>
</head>
<body>
	<form method="get" action=Vjezba_06_6>
		<input type="submit" value="Dohvati prvih deset aerodroma">
	</form>
	
	<%
	var greska = request.getAttribute("greska");
	if(greska!=null){
	  %>
	  	Javila se greška u radu <%= greska %>
	  <%
	  }
	  	
	  	var podaci = (java.util.List<org.foi.nwtis.mkovac.vjezba_06.Aerodrom>) request.getAttribute("podaci");
	  	if(podaci!=null){
	  %>

<table border=1>
	<tr><th>ICAO</th><th>Name</th></tr>
	<%
	for(org.foi.nwtis.mkovac.vjezba_06.Aerodrom u : podaci){
	%>
		<tr><td><%= u.icao() %></td><td><%= u.name() %></td></tr>
	<% } } %>
</table>
</body>
</html>