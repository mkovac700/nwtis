<%@page import="org.foi.nwtis.rest.podaci.LetAviona"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Letovi aviona na dan</title>
</head>
<body>
<h1>Letovi s aerodroma na dan</h1>

<%
	String icao = "";
	String dan = "";
	
	if (request.getParameter("icao") != null) icao = request.getParameter("icao");
	if (request.getParameter("dan") != null) dan = request.getParameter("dan");
%>

<div style="display: flex;">
<form action="" method="get">
	  <label for="icao">ICAO:</label><br>
	  <input type="text" id="icao" name="icao" value=<%=icao %>><br>
	  <label for="dan">Dan:</label><br>
	  <input type="text" id="dan" name="dan" placeholder="dd.MM.yyyy" value=<%=dan %>>
	  <button type="submit">Pretraži</button>
</form>
</div>
<br>
<%
	List<LetAviona> letoviAviona = (List<LetAviona>) request.getAttribute("letoviAviona");
	if((icao != null && !icao.isEmpty()) && (dan != null && !dan.isEmpty())){
	  %>
	  <table border=1> 
			<tr><th>ICAO24</th><th>Polazni aerodrom</th><th>Dolazni aerodrom</th><th>Oznaka</th><th>Opcije</th></tr>
	<%
		if(letoviAviona != null){
			for(LetAviona lv : letoviAviona){ 
	%>
	<tr>
		<td><%=lv.getIcao24() %></td>
		<td><%=lv.getEstDepartureAirport() %></td>
		<td><%=lv.getEstArrivalAirport() %></td>
		<td><%=lv.getCallsign() %></td>
		<td></td>
	</tr>
	<% 
			}
		}
	%>
	</table>
	<% 
	}
	%>
</body>
</html>