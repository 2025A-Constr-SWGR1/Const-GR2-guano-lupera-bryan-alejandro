<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="entidades.Restaurante" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Resultados de Búsqueda</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<div class="container mt-5">
    <%
        request.setAttribute("titulo", "Resultados"); // Ejemplo: para resaltar menú
        request.setAttribute("botonAtras", true); // Ejemplo: para resaltar menú
    %>
    <%@ include file="layout/header.jsp" %>
</div>

<div class="container">
    <h2>Restaurantes encontrados:</h2>
    <div class="row" id="restaurantes-container">
        <c:choose>
            <c:when test="${empty restaurantesFiltrados}">
                <div class="col-12">
                    <div class="card no-restaurants">
                        <i class="fas fa-utensils fa-4x mb-3"></i>
                        <h3>No hay restaurantes disponibles</h3>
                        <p class="text-muted">No se encontraron restaurantes que coincidan con tu búsqueda.</p>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${restaurantesFiltrados}" var="restaurante">
                    <div class="col-md-6 col-lg-4 mb-4">
                        <div class="card restaurant-card h-100">
                            <div class="restaurant-img-placeholder">
                                <i class="fas fa-utensils fa-3x"></i>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">
                                    <c:out value="${not empty restaurante.nombre ? restaurante.nombre : 'Sin nombre'}"/>
                                </h5>
                                <p class="restaurant-type mb-2">
                                    <i class="fas fa-utensils me-1"></i>
                                    <c:out value="${not empty restaurante.tipoComida ? restaurante.tipoComida : 'No especificado'}"/>
                                </p>

                                <div class="mb-2">
                                    <c:choose>
                                        <c:when test="${restaurante.puntajePromedio > 0}">
                                            <c:forEach begin="1" end="5" var="i">
                                                <i class="fas fa-star ${i <= restaurante.puntajePromedio ? 'rating-stars' : 'text-secondary'}"></i>
                                            </c:forEach>
                                            <span class="ms-1">(<fmt:formatNumber value="${restaurante.puntajePromedio}"
                                                                                  pattern="#.##"/>)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Sin calificaciones</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <p class="card-text">
                                    <c:out value="${not empty restaurante.descripcion ? restaurante.descripcion : 'Sin descripción'}"/>
                                </p>

                                <div class="restaurant-actions">
                                    <c:if test="${not empty sessionScope.usuario && sessionScope.usuario.tipoUsuario == 'COMENSAL'}">
                                        <a href="${pageContext.request.contextPath}/calificar?idRestaurante=${restaurante.id}"
                                           class="btn btn-sm btn-outline-success">
                                            <i class="fas fa-star"></i> Calificar
                                        </a>
                                    </c:if>

                                    <c:if test="${not empty restaurante.historias && !restaurante.historias.isEmpty()}">
                                        <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal"
                                                data-bs-target="#menuModal${restaurante.id}">
                                            <i class="fas fa-utensils"></i> Ver Menú
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Modal para el menú -->
                    <c:if test="${not empty restaurante.historias && !restaurante.historias.isEmpty()}">
                        <div class="modal fade" id="menuModal${restaurante.id}" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Menú de ${restaurante.nombre}</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body menu-container">
                                        <c:forEach items="${restaurante.historias}" var="menu">
                                            <c:if test="${not empty menu}">
                                                <div class="menu-item mb-2">
                                                        ${menu}
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
<%--    <div class="restaurant-grid">--%>
<%--        <%--%>
<%--            List<Restaurante> restaurantes = (List<Restaurante>) request.getAttribute("restaurantesFiltrados");--%>
<%--            if (restaurantes != null && !restaurantes.isEmpty()) {--%>
<%--                for (Restaurante restaurante : restaurantes) {--%>
<%--        %>--%>
<%--        <div class="restaurant-card">--%>
<%--            <div class="restaurant-name"><%= restaurante.getNombre() %>--%>
<%--            </div>--%>
<%--            <div class="restaurant-description"><%= restaurante.getDescripcion() %>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <%--%>
<%--            }--%>
<%--        } else {--%>
<%--        %>--%>
<%--        <p>No se encontraron restaurantes que cumplan con los filtros seleccionados.</p>--%>
<%--        <%--%>
<%--            }--%>
<%--        %>--%>
<%--    </div>--%>
</div>

</body>
</html>
