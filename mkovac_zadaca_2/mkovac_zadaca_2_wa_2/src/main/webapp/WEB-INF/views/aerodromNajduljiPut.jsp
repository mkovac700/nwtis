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
String icao = "";
String drzava = "";
float km = 0;
if(uad != null){
  icao = uad.icao();
  drzava = uad.drzava();
  km = uad.km();
}
%>
ICAO: <%= icao %> <br>
Država:  <%= drzava %><br>
Udaljenost (km): <%= km %>
</body>
</html>