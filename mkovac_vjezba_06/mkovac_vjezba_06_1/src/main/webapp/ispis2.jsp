<%@page import="java.util.List"%>
<%@page import="mkovac_vjezba_06.Udaljenost"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenosti aerodroma</title>
</head>
<body>
<%
	var greska = request.getAttribute("greska");
	if(greska!=null){
	  %>
	  	Javila se greška u radu <%= greska %>
	  <%
	  
	}
	
	var podaci = (List<Udaljenost>) request.getAttribute("podaci");
	float ukupnoKm = 0;
%>

<table border=1>
	<tr><th>Država</th><th>Udaljenost (km)</th></tr>
	<%for(Udaljenost u : podaci){ 
		ukupnoKm+= u.km();
	%>
		<tr><td><%= u.drzava() %></td><td><%= u.km() %></td></tr>
	<% }%>
	<tr><td>Ukupno:</td><td><%= ukupnoKm %></td></tr>
</table>
</body>
</html>