<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Upravljanje poslužiteljem</title>
<script type="text/javascript">
	function postaviOdabraniGumb(id){
		document.getElementById("komanda").value = id;
	}
</script>
</head>
<body>
	<header>
		<a href="${pageContext.servletContext.contextPath}">Početna stranica</a><br>
		<h1>Upravljanje poslužiteljem</h1>
		<%@ include file="zaglavlje.jsp"%>
	</header>
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	%>
	<main>
		<div>
			<form action="" method="post">
				<input type="hidden" id="komanda" name="komanda" value="">
				<button type="submit" onclick="postaviOdabraniGumb('STATUS')">STATUS</button>
				<button type="submit" onclick="postaviOdabraniGumb('KRAJ')">KRAJ</button>
				<button type="submit" onclick="postaviOdabraniGumb('INIT')">INIT</button>
				<button type="submit" onclick="postaviOdabraniGumb('PAUZA')">PAUZA</button>
				<button type="submit" onclick="postaviOdabraniGumb('INFO DA')">INFO DA</button>
				<button type="submit" onclick="postaviOdabraniGumb('INFO NE')">INFO NE</button>
			</form>
		</div>
		<br>
		<%if(odgovor != null) {%>
		<%=odgovor %>
		<%} %>
	</main>

</body>
</html>