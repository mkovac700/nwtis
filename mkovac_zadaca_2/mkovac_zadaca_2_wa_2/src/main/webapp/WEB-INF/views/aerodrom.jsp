<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodrom</title>
</head>
<body>
<h1>Jedan aerodrom</h1>
icao: ${aerodrom.icao} <br>
Naziv: ${aerodrom.naziv} <br>
Drzava: ${aerodrom.drzava} <br>
Lokacija: ${aerodrom.lokacija.latitude}, ${aerodrom.lokacija.longitude}
</body>
</html>