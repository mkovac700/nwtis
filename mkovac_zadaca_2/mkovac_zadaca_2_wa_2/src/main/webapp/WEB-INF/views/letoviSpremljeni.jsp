<%@page import="java.time.ZoneId"%>
<%@page import="java.time.ZonedDateTime"%>
<%@page import="java.time.Instant"%>
<%@page import="org.foi.nwtis.rest.podaci.LetAvionaID"%>
<%@page import="java.util.List"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Spremljeni letovi</title>
</head>
<body>
<h1>Spremljeni letovi</h1>
<br>
<%
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
	List<LetAvionaID> letoviAviona = (List<LetAvionaID>) request.getAttribute("letoviAviona");
	
	if(letoviAviona != null && !letoviAviona.isEmpty()){
	  %>
	  <table border=1> 
			<tr><th>ICAO24</th><th>Polazni aerodrom</th><th>Dolazni aerodrom</th><th>Vrijeme polaska (<%=ZoneId.systemDefault().getId() %>)</th><th>Vrijeme dolaska (<%=ZoneId.systemDefault().getId() %>)</th><th>Oznaka</th><th>Opcije</th></tr>
	<%
		if(letoviAviona != null){
		  for(LetAvionaID la : letoviAviona){ 
			  long firstSeen = la.getFirstSeen();
			  long lastSeen = la.getLastSeen();
			  Instant instant1 = Instant.ofEpochSecond(firstSeen);
			  ZonedDateTime zonedDateTime1 = ZonedDateTime.ofInstant(instant1, ZoneId.systemDefault());
			  Instant instant2 = Instant.ofEpochSecond(lastSeen);
			  ZonedDateTime zonedDateTime2 = ZonedDateTime.ofInstant(instant2, ZoneId.systemDefault());
	%>
	<tr>
		<td><%=la.getIcao24() %></td>
		<td><%=la.getEstDepartureAirport() %></td>
		<td><%=la.getEstArrivalAirport()%></td>
		<td><%=zonedDateTime1.format(formatter) %></td>
		<td><%=zonedDateTime2.format(formatter) %></td>
		<td><%=la.getCallsign() %></td>
		<td>
			<form action="${pageContext.servletContext.contextPath}/mvc/letovi/<%=la.getId() %>" method="post">
		  		<input type="hidden" name="url" value='/mvc/letovi/spremljeni' />
		  		<button type="submit">Obriši</button>
			</form>
		</td>
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