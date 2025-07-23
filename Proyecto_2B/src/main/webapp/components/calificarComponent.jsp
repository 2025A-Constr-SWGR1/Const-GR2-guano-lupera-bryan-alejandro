
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    String[] opcionesDeRetorno = (String[]) request.getAttribute("opcionesDeRetorno");
    String[] opcionesTiempoEspera = (String[]) request.getAttribute("opcionesTiempoEspera");
    String[] opciones = new String[5];

    if (request.getParameter("name").equals("volveria")  ){
        opciones = opcionesDeRetorno;
    }
    if (request.getParameter("name").equals("tiempoEspera")){
        opciones = opcionesTiempoEspera;
    }
    if (!request.getParameter("name").equals("volveria") && !request.getParameter("name").equals("tiempoEspera")){
        opciones = new String[] { "", "", "", "", "" };
    }
%>
<div class="mb-3">
    <label class="form-label">${param.tituloCalificacion}</label>
    <div class="rating-option d-flex justify-content-between mb-3">

        <input type="radio" id="star5-${param.name}" name="${param.name}" value="5" required>
        <label for="star5-${param.name}">★<div class="rating-text"><%=opciones[4]%></div></label>

        <input type="radio" id="star4-${param.name}" name="${param.name}" value="4">
        <label for="star4-${param.name}">★<div class="rating-text"><%=opciones[3]%></div></label>

        <input type="radio" id="star3-${param.name}" name="${param.name}" value="3">
        <label for="star3-${param.name}">★<div class="rating-text"><%=opciones[2]%></div></label>

        <input type="radio" id="star2-${param.name}" name="${param.name}" value="2">
        <label for="star2-${param.name}">★<div class="rating-text"><%=opciones[1]%></div></label>

        <input type="radio" id="star1-${param.name}" name="${param.name}" value="1">
        <label for="star1-${param.name}">★<div class="rating-text"><%=opciones[0]%></div></label>

    </div>
</div>
</body>
</html>
