<%@page import="java.util.List"%>
<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Svi aerodromi</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br> 
		<h1>Svi aerodromi</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
<%
	int brStranice = 1; 
	String greska = (String) request.getAttribute("greska");
	
	if(request.getParameter("stranica") != null){ 
	  try{
	  	brStranice = Integer.parseInt(request.getParameter("stranica"));
	  }
	  catch(NumberFormatException e){
	    brStranice = 1;
	  }
	  if(brStranice < 1) 
	    response.sendRedirect(request.getContextPath()+"/mvc/aerodromi/svi?stranica=1");
	}
	
	List<Aerodrom> aerodromi = (List<Aerodrom>) request.getAttribute("aerodromi");
	

	if(aerodromi != null && !aerodromi.isEmpty()){
%>
	  	<table border=1> 
	 
		<tr><th>Oznaka</th><th>Naziv</th><th>Država</th><th>Lokacija</th><th>Opcije</th></tr>
		<%
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
		%>
		</table>
		<br>
		Stranica: <%=brStranice %> <br>
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
<% 
	}
	else{
	  if(brStranice != 1)
	  	response.sendRedirect(request.getContextPath()+"/mvc/aerodromi/svi?stranica=1");
	  else greska = "Nema podataka za prikaz!";
	}
%>
	<%
	if(greska!= null){
	  %>
	  	<%=greska %>
	  <%
	}
	%>

</main>
</body>
</html>