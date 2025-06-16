<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entidades.Restaurante" %>
<%@ page import="entidades.Comparacion" %>
<!DOCTYPE html>
<html>
<head>
    <title>U-Food | Resultado Comparación</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="styles.css">
    <style>
        body {
            background-color: #f8f9fa;
            padding: 20px;
        }

        .card {
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .card-body {
            padding: 30px;
        }

        .better {
            font-color: #e8f5e9;
        }

        .no-rating {
            color: #6c757d;
            font-style: italic;
        }

        .rating-info {
            font-size: 0.9em;
            color: #6c757d;
            margin-top: 5px;
        }

        .table th {
            background-color: #0d6efd;
            color: white;
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <%
        request.setAttribute("titulo", "Resultado de Comparación");
        request.setAttribute("botonAtras", true);
        Restaurante restaurante1 = (Restaurante) session.getAttribute("restaurante1");
        Restaurante restaurante2 = (Restaurante) session.getAttribute("restaurante2");

        request.setAttribute("restaurante1", restaurante1);
        request.setAttribute("restaurante2", restaurante2);

        if (restaurante1 != null && restaurante2 != null) {
            Comparacion comparacion = new Comparacion(restaurante1, restaurante2);
            comparacion.realizarComparacion();
            request.setAttribute("comparaciones", comparacion.getComparaciones());
            request.setAttribute("resultadoFinal", comparacion.getResultadoFinal());
        }
    %>

    <%@ include file="layout/header.jsp" %>
</div>
<div class="container">
    <c:if test="${restaurante1 != null && restaurante2 != null}">
    <!-- El resto de tu código de la tabla permanece igual -->
    <div class="card shadow">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered">
                    <tr>
                        <th>Características</th>
                        <th>${restaurante1.nombre}</th>
                        <th>${restaurante2.nombre}</th>
                    </tr>
                    <tr>
                        <td>Tipo de Comida</td>
                        <td>${restaurante1.tipoComida}</td>
                        <td>${restaurante2.tipoComida}</td>
                    </tr>
                    <tr>
                        <td>Descripción</td>
                        <td>${restaurante1.descripcion}</td>
                        <td>${restaurante2.descripcion}</td>
                    </tr>
                    <tr>
                        <td>Horario</td>
                        <td>${restaurante1.horaApertura} - ${restaurante1.horaCierre}</td>
                        <td>${restaurante2.horaApertura} - ${restaurante2.horaCierre}</td>
                    </tr>
                    <!-- <tr>
                        <td>Puntaje Promedio</td>
                        <td>${restaurante1.puntajePromedio} ★</td>
                        <td>${restaurante2.puntajePromedio} ★</td>
                    </tr> -->
                    <!-- <tr>
                        <td>Tiempo de Espera</td>
                        <td>${restaurante1.tiempoEspera} minutos</td>
                        <td>${restaurante2.tiempoEspera} minutos</td>
                    </tr> -->
                    <tr>
                        <td>Distancia Universidad</td>
                        <td>${restaurante1.distanciaUniversidad} km</td>
                        <td>${restaurante2.distanciaUniversidad} km</td>
                    </tr>
                    <!-- <tr>
                        <td>Calidad</td>
                        <td>${restaurante1.calidad}🍴</td>
                        <td>${restaurante2.calidad}🍴</td>
                    </tr> -->
                    <tr>
                        <td>Precio</td>
                        <td>${restaurante1.precio}$</td>
                        <td>${restaurante2.precio}$</td>
                    </tr>
                </table>

                <div class="card mt-4">
                    <div class="card-body">
                        <h4 class="card-title mb-3">Análisis Comparativo</h4>
                        <div class="mb-3">
                                ${comparaciones}
                        </div>
                        <div class="alert alert-primary">
                                ${resultadoFinal}
                        </div>
                    </div>
                </div>

                <a href="comparar" class="btn btn-primary w-100 py-2 mt-3">
                    <i class="fas fa-redo me-2"></i> Realizar otra comparación
                </a>
            </div>
        </div>
        </c:if>
    </div>
</div>
</body>
</html>