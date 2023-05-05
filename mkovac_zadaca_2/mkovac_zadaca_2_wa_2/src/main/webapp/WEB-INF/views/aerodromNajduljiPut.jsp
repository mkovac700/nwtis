<%@page import="org.foi.nwtis.podaci.UdaljenostAerodromDrzava"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodrom najdulji put</title>
</head>
<body>
<h1>Najdulji put</h1>
<%
UdaljenostAerodromDrzava uad = (UdaljenostAerodromDrzava)request.getAttribute("udaljenostAerodromDrzava");
%>
icao: <%= uad.icao() %> <br>
Drzava:  <%= uad.drzava() %><br>
km: <%= uad.km() %>
</body>
</html>