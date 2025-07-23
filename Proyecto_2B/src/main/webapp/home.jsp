<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>U-Food | Descubre los mejores restaurantes</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="container mt-5">
    <% request.setAttribute("titulo", "Bienvenido a U-Food"); %>
    <% request.setAttribute("botonAtras", false); %>
    <%@ include file="layout/header.jsp" %>

    <!-- Banner de bienvenida para no logueados -->
    <div class="card shadow mb-4 bg-light">
        <div class="card-body text-center">
            <h2 class="mb-3"><i class="fas fa-utensils text-primary me-2"></i>Descubre los mejores restaurantes</h2>
            <p class="lead">Regístrate o inicia sesión para acceder a todas las funcionalidades</p>
            <div class="d-flex justify-content-center gap-3 mt-3">
                <a href="${pageContext.request.contextPath}/registro" class="btn btn-primary">
                    <i class="fas fa-user-plus me-2"></i>Registrarse
                </a>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary">
                    <i class="fas fa-sign-in-alt me-2"></i>Iniciar sesión
                </a>
            </div>
        </div>
    </div>

    <!-- Sección de restaurantes -->
    <div class="card shadow mb-4">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0"><i class="fas fa-store me-2"></i>Restaurantes disponibles</h4>
        </div>
        <div class="card-body">
            <div class="row" id="restaurantes-container">
                <c:choose>
                    <c:when test="${empty restaurantes}">
                        <div class="col-12">
                            <div class="alert alert-info text-center">
                                <i class="fas fa-info-circle fa-2x mb-3"></i>
                                <h4>No hay restaurantes disponibles</h4>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${restaurantes}" var="restaurante">
                            <div class="col-md-6 col-lg-4 mb-4">
                                <div class="card restaurant-card h-100">
                                    <div class="restaurant-img-placeholder">
                                        <i class="fas fa-utensils fa-3x"></i>
                                    </div>
                                    <div class="card-body">
                                        <h5 class="card-title">
                                            <a href="${pageContext.request.contextPath}/detalleRestaurante?id=${restaurante.id}"
                                               class="text-decoration-none text-dark">
                                                <c:out value="${restaurante.nombre}"/>
                                            </a>
                                        </h5>
                                        <p class="restaurant-type mb-2">
                                            <i class="fas fa-utensils me-1"></i>
                                            <c:out value="${restaurante.tipoComida}"/>
                                        </p>
                                        <div class="mb-2">
                                            <c:forEach begin="1" end="5" var="i">
                                                <i class="fas fa-star ${i <= restaurante.puntajePromedio ? 'text-warning' : 'text-secondary'}"></i>
                                            </c:forEach>
                                            <span class="ms-1">
                                                (<fmt:formatNumber value="${restaurante.puntajePromedio}" pattern="#.##"/>)
                                            </span>
                                        </div>
                                        <div class="d-grid mt-3">
                                            <a href="${pageContext.request.contextPath}/detalleRestaurante?id=${restaurante.id}"
                                               class="btn btn-outline-primary btn-sm">
                                                <i class="fas fa-info-circle me-1"></i>Ver detalles
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Puedes agregar aquí funcionalidad JS específica para no logueados
    });
</script>
</body>
</html>