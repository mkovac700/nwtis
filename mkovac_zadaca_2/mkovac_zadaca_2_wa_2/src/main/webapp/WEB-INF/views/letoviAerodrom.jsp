<%@page import="com.google.gson.Gson"%>
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
	
	int brStranice = 1; 
	
	if(request.getParameter("stranica") != null){ //odBroja
	  brStranice = Integer.parseInt(request.getParameter("stranica"));//odBroja
	  if(brStranice < 1) 
	    response.sendRedirect(request.getContextPath()+"/mvc/letovi/aerodrom?icao="+icao+"&dan="+dan+"&stranica=1");
	}
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
	Gson gson = new Gson();
	List<LetAviona> letoviAviona = (List<LetAviona>) request.getAttribute("letoviAviona");
	
	if((icao != null && !icao.isEmpty()) && (dan != null && !dan.isEmpty())){
	  %>
	  Stranica: <%=brStranice %>
	  <table border=1> 
			<tr><th>ICAO24</th><th>Polazni aerodrom</th><th>Dolazni aerodrom</th><th>Oznaka</th><th>Opcije</th></tr>
	<%
		if(letoviAviona != null){
		  	boolean iskljuceno = false;
			for(LetAviona lv : letoviAviona){ 
			  iskljuceno = false;
			  String dolazni = lv.getEstArrivalAirport();
			  if(dolazni==null) {
			    dolazni="N/A";
			    iskljuceno = true;
			  }
			  String jsonLetAviona = gson.toJson(lv);
	%>
	<tr>
		<td><%=lv.getIcao24() %></td>
		<td><%=lv.getEstDepartureAirport() %></td>
		<td><%=dolazni %></td>
		<td><%=lv.getCallsign() %></td>
		<td>
			<form action="${pageContext.servletContext.contextPath}/mvc/letovi" method="post">
		  		<input type="hidden" name="url" value='/mvc/letovi/aerodrom?icao=<%=icao %>&dan=<%=dan %>&stranica=<%=brStranice %>' />
		  		<input type="hidden" name="jsonLetAviona" value='<%=jsonLetAviona %>' />
		  	<%if(iskljuceno){ %>
		  		<button type="submit" disabled>Spremi</button>
		  	<%}else{ %>
		  		<button type="submit">Spremi</button>
		  	<%} %>
			</form>
		</td>
	</tr>
	<% 
			}
		}
		else
		  if(brStranice != 1) 
		    response.sendRedirect(request.getContextPath()+"/mvc/letovi/aerodrom?icao="+icao+"&dan="+dan+"&stranica=1");
	%>
	</table>
	<br>
	<div style="display: flex;"> <!-- justify-content: center;" -->
		<form action="" method="get">
		  	<input type="hidden" name="icao" value="<%=icao %>" />
		  	<input type="hidden" name="dan" value="<%=dan %>" />
		  	<input type="hidden" name="stranica" value="1" />
		  	<button type="submit">Početak</button>
		</form>
		<form action="" method="get">
			<input type="hidden" name="icao" value="<%=icao %>" />
		  	<input type="hidden" name="dan" value="<%=dan %>" />
		  	<input type="hidden" name="stranica" value="<%=brStranice-1 %>" />
		  	<button type="submit">Prethodna stranica</button>
		</form>
		<form action="" method="get">
			<input type="hidden" name="icao" value="<%=icao %>" />
		  	<input type="hidden" name="dan" value="<%=dan %>" />
		  	<input type="hidden" name="stranica" value="<%=brStranice+1 %>" />
		  	<button type="submit">Sljedeća stranica</button>
		</form>
	</div>
	<% 
	}
	%>
</body>
</html>