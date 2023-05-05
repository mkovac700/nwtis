<%@page import="org.foi.nwtis.podaci.Udaljenost"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenost aerodroma</title>
</head>
<body>
<h1>Udaljenost između dva aerodroma po državama</h1>

<%
	String icaoOd = "";
	String icaoDo = "";
	
	if (request.getParameter("icaoOd") != null) icaoOd = request.getParameter("icaoOd");
	if (request.getParameter("icaoDo") != null) icaoDo = request.getParameter("icaoDo");
%>

<div style="display: flex;">
<form action="" method="get">
	  <label for="icaoOd">ICAO od:</label><br>
	  <input type="text" id="icaoOd" name="icaoOd" value=<%=icaoOd %>><br>
	  <label for="icaoDo">ICAO do:</label><br>
	  <input type="text" id="icaoDo" name="icaoDo" value=<%=icaoDo %>>
	  <button type="submit">Pretraži</button>
</form>
</div>
<br>
<%
	float ukupno = 0;
	List<Udaljenost> udaljenost2Aerodroma = (List<Udaljenost>) request.getAttribute("udaljenost2Aerodroma");
	if((icaoOd != null && !icaoOd.isEmpty()) && (icaoDo != null && !icaoDo.isEmpty())){
	  %>
	  	<table border=1> 
			<tr><th>Država</th><th>Udaljenost (km)</th></tr>
			<%
				if(udaljenost2Aerodroma != null){
					for(Udaljenost u : udaljenost2Aerodroma){ 
					  ukupno += u.km();
			%>
			<tr>
				<td><%=u.drzava() %></td>
				<td><%=u.km() %></td>
			</tr>
			<% 
					}
				}
			%>
			<tr>
				<td><b>Ukupno (km)</b></td>
				<td><b><%=ukupno %></b></td>
			</tr>
		</table>
	  <%
	}

%>

</body>
</html>