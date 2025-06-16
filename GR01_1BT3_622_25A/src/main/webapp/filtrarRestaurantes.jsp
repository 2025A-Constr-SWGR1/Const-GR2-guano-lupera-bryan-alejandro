<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Filtrar Restaurantes</title>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #f2f4f7;
      margin: 0;
      padding: 0;
    }
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background-color: white;
      padding: 20px;
      box-shadow: 0px 2px 5px rgba(0,0,0,0.1);
    }
    .header h1 {
      color: #007bff;
      margin: 0;
    }
    .user-info {
      display: flex;
      align-items: center;
    }
    .user-circle {
      width: 40px;
      height: 40px;
      background-color: #ff6b6b;
      color: white;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      margin-right: 10px;
    }
    .container {
      background-color: white;
      margin: 40px auto;
      padding: 30px;
      border-radius: 12px;
      max-width: 600px;
      box-shadow: 0px 2px 10px rgba(0,0,0,0.1);
    }
    label {
      font-weight: bold;
      display: block;
      margin-top: 15px;
      margin-bottom: 5px;
    }
    input, select {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 8px;
      margin-bottom: 10px;
      box-sizing: border-box;
    }
    .button-group {
      display: flex;
      justify-content: space-between;
      gap: 10px;
      margin-top: 20px;
    }
    .button-group button {
      flex: 1;
      background-color: #007bff;
      color: white;
      padding: 12px;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      cursor: pointer;
    }
    .button-group button.save {
      background-color: #28a745; /* Verde para guardar */
    }
    .button-group button:hover {
      opacity: 0.9;
    }
  </style>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="styles.css">
</head>
<body>

  <div class="container mt-5">
    <%
      request.setAttribute("titulo", "Filtrar Restaurantes"); // Ejemplo: para resaltar menú
      request.setAttribute("botonAtras", true); // Ejemplo: para resaltar menú
    %>
    <%@ include file="layout/header.jsp" %>
  </div>


<div class="container">
  <form id="filtroForm" action="${pageContext.request.contextPath}/SvPreferencia" method="get">
    <label for="tipoComida">Tipo de Comida</label>
    <select id="tipoComida" name="tipoComida" required>
      <option value="">Seleccione una opción</option>
      <option value="COMIDA RAPIDA">Comida Rápida</option>
      <option value="COMIDA COSTEÑA">Comida Costeña</option>
      <option value="COMIDA CASERA">Comida Casera</option>
      <option value="PLATOS A LA CARTA">Platos a la Carta</option>
    </select>

    <label for="horaApertura">Hora de Apertura (mínima)</label>
    <input type="time" id="horaApertura" name="horaApertura">

    <label for="horaCierre">Hora de Cierre (máxima)</label>
    <input type="time" id="horaCierre" name="horaCierre">

    <label for="distancia">Distancia Máxima (km)</label>
    <input type="number" step="0.1" id="distancia" name="distancia" placeholder="Ej: 2.5">

    <div class="button-group">
      <button type="submit" name="accion" value="buscar">Buscar Restaurantes</button>
      <button type="button" class="save" onclick="guardarPreferencia()">Guardar Preferencia</button>
    </div>
    <input type="hidden" name="idComensal" value="1">
  </form>
</div>

<script>
  function guardarPreferencia() {
    const form = document.getElementById('filtroForm');
    form.action = '${pageContext.request.contextPath}/SvPreferencia'; // Nueva URL que maneja POST
    form.method = 'post';
    form.submit();
  }
</script>

</body>
</html>
