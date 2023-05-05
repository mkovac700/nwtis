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
	int brStranice = 1; //1
	//int brojRedova = (int) request.getAttribute("brojRedova");
	
	if(request.getParameter("stranica") != null){ //odBroja
	  brStranice = Integer.parseInt(request.getParameter("stranica"));//odBroja
	  if(brStranice < 1) 
	    response.sendRedirect(request.getContextPath()+"/mvc/aerodromi/svi?stranica=1");
	}
	
	List<Aerodrom> aerodromi = (List<Aerodrom>) request.getAttribute("aerodromi");
	
%>
Stranica: <%=brStranice %>
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
			<td>
				<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/<%= a.getIcao() %>">Pregled aerodroma</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/<%= a.getIcao() %>/udaljenosti">Pregled udaljenosti</a>
				<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/<%= a.getIcao() %>/najduljiPutDrzave">Pregled najduljeg puta države</a>
			</td>
		</tr>
	<%
		}
	}
	else
	  response.sendRedirect(request.getContextPath()+"/mvc/aerodromi/svi?stranica=1");
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