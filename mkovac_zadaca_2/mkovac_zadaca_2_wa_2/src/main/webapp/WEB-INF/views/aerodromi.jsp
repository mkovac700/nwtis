<%@page import="java.util.List"%>
<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi</title>

</head>
<body>
<h1>Svi aerodromi</h1>

<%
	int stranica = 1;
	int brojRedova = (int) request.getAttribute("brojRedova");
	if(request.getParameter("odBroja") != null){
	  stranica = Integer.parseInt(request.getParameter("odBroja"));
	}
	
	List<Aerodrom> aerodromi = (List<Aerodrom>) request.getAttribute("aerodromi");
	
%>

<table border=1>
	 
	<tr><th>Oznaka</th><th>Naziv</th><th>Država</th><th>Lokacija</th><th>Opcije</th></tr>
	<%
	if(aerodromi != null){
		for(Aerodrom a : aerodromi){ 
	%>
		<tr>
			<td><%=a.getIcao() %></td>
			<td><%=a.getNaziv() %></td>
			<td><%=a.getDrzava() %></td>
			<td><%=a.getLokacija().getLatitude() %>, <%=a.getLokacija().getLongitude() %></td>
			<td></td>
		</tr>
	<%}} %>
</table>
<button type="button" onclick="sljedeca()">Sljedeća stranica</button>
<script>
	function sljedeca(){
		var trenutna = <%= stranica%>;
		trenutna += <%= brojRedova%>;
		location.href = "?odBroja=" + trenutna;
	}
</script>
</body>
</html>