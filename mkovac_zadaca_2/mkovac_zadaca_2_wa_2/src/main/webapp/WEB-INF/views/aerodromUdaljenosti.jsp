<%@page import="org.foi.nwtis.podaci.UdaljenostAerodrom"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi udaljenost</title>
</head>
<body>
<%
	String icao = (String) request.getAttribute("icao");
%>
<h1>Udaljenost svih aerodroma od aerodroma <%=icao %></h1>

<%
	int brStranice = 1; 
	
	if(request.getParameter("stranica") != null){ //odBroja
	  brStranice = Integer.parseInt(request.getParameter("stranica"));//odBroja
	  if(brStranice < 1) 
	    response.sendRedirect(request.getContextPath()+"/mvc/aerodromi/"+icao+"/udaljenosti?stranica=1");
	}
	
	List<UdaljenostAerodrom> udaljenostAerodromi = (List<UdaljenostAerodrom>) request.getAttribute("udaljenostAerodromi");
	
%>
Stranica: <%=brStranice %>
<table border=1> 
	 
	<tr><th>Oznaka</th><th>Udaljenost (km)</th></tr>
	<%
	if(udaljenostAerodromi != null){
		for(UdaljenostAerodrom a : udaljenostAerodromi){ 
	%>
		<tr>
			<td><%=a.icao() %></td>
			<td><%=a.km() %></td>
		</tr>
	<%
		}
	}
	else
	  if(brStranice != 1) 
	    response.sendRedirect(request.getContextPath()+"/mvc/aerodromi/"+icao+"/udaljenosti?stranica=1");
	%>
</table>
<br>
<div style="display: flex;"> <!-- justify-content: center;" -->
	<form action="" method="get">
	  <input type="hidden" name="stranica" value="1" />
	  <button type="submit">Početak</button>
	</form>
	<form action="" method="get">
	  <input type="hidden" name="stranica" value="<%=brStranice-1 %>" />
	  <button type="submit">Prethodna stranica</button>
	</form>
	<form action="" method="get">
	  <input type="hidden" name="stranica" value="<%=brStranice+1 %>" />
	  <button type="submit">Sljedeća stranica</button>
	</form>
</div>
</body>
</html>