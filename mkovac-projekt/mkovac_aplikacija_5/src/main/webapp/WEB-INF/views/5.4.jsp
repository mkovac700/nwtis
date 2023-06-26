<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Poruke</title>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna
			stranica</a><br>
		<h1>Pregled primljenih poruka</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<main>
		<br>
		<%
		int brStranice = 1;
		
		if(request.getParameter("stranica") != null){ 
		  try{
		  	brStranice = Integer.parseInt(request.getParameter("stranica"));
		  }
		  catch(NumberFormatException e){
		    brStranice = 1;
		  }
		  if(brStranice < 1) 
		    response.sendRedirect(request.getContextPath()+"/mvc/poruke?stranica=1");
		}
		
		List<String> poruke = (List<String>) request.getAttribute("poruke");
		String greska = (String) request.getAttribute("greska");
		if (poruke != null && !poruke.isEmpty()) {
		  for (String poruka : poruke) {
		%>
		<%=poruka%><br>

		<%
		}
		%>  
		  
		<br>
		Stranica: <%=brStranice %> <br>
		<br>
		<div style="display: flex;"> 
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
		<br>
		<div style="display: flex;">
			<form action="" method="post">
			  <button type="submit">Obriši</button>
			</form>
		</div>
		  
		<%  
		} 
		else{
		  if(brStranice != 1)
		  	response.sendRedirect(request.getContextPath()+"/mvc/poruke?stranica=1");
		  else greska = "Nema podataka za prikaz!";
		}
		%>

		<%
		if (greska != null) {
		%>
		<%=greska%>
		<%
		}
		%>
	</main>
</body>
</html>