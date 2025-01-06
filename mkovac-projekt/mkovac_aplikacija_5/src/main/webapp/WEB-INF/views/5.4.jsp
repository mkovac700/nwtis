<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="hr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<title>Poruke</title>
</head>
<body class="d-flex flex-column min-vh-100">
	<header>
		<div class="container-fluid">
			<a href="${pageContext.servletContext.contextPath}">Početna stranica</a>
		</div>
	</header>
	<main class="flex-grow-1">
	<div class="container mt-5">
		<h1>Pregled primljenih poruka</h1>
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
	</div>
	</main>
	
	<footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p class="mb-0"><%@ include file="zaglavlje.jsp"%></p>
        </div>
    </footer>
</body>
</html>