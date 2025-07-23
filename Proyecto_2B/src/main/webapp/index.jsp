<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>

<head>
    <title>U-Food | Inicio</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap"
          rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="styles.css">
    <style>
        .menu-container .card {
            border-left: 4px solid #0d6efd;
            transition: transform 0.2s;
        }
        .menu-container .card:hover {
            transform: translateX(5px);
        }
        .btn-ver-menu {
            background-color: #6c757d;
            color: white;
        }
        .btn-ver-menu:hover {
            background-color: #5a6268;
            color: white;
        }
    </style>
</head>

<body>
<div class="container mt-5">
    <% request.setAttribute("titulo", "Lista de Restaurantes ");
        request.setAttribute("botonAtras", false); %>
    <%@ include file="layout/header.jsp" %>

    <div class="card shadow mb-4">
        <div class="card-body">
            <div class="row">
                <div class="col-12 d-flex flex-wrap justify-content-center gap-2">
                    <c:if test="${not empty sessionScope.usuario && sessionScope.usuario.tipoUsuario == 'COMENSAL'}">
                        <a href="${pageContext.request.contextPath}/filtrarRestaurantes.jsp"
                           class="btn btn-info">
                            <i class="fas fa-filter me-2"></i>Filtrar
                        </a>
                        <a href="${pageContext.request.contextPath}/planificar"
                                class="btn btn-success">
                        <i class="fas fa-calendar-plus me-2"></i>Crear Planificación
                        </a>
                        <a href="${pageContext.request.contextPath}/misPlanificaciones" class="btn btn-primary">
                            <i class="fas fa-calendar-alt me-2"></i>Mis Planificaciones
                        </a>
                        <a href="${pageContext.request.contextPath}/comparar" class="btn btn-warning">
                            <i class="fas fa-balance-scale me-2"></i>Comparar Restaurantes
                        </a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- Sección de restaurantes recomendados -->
    <c:if test="${not empty sessionScope.usuario}">
        <div class="card shadow mb-4">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0"><i class="fas fa-heart me-2"></i>
                    <c:choose>
                        <c:when test="${not empty restaurantesRecomendados && fn:length(restaurantesRecomendados) > 0}">
                            <c:choose>
                                <c:when test="${not empty sessionScope.usuario.tipoComidaFavorita}">
                                    Recomendados para ti (${fn:length(restaurantesRecomendados)})
                                </c:when>
                                <c:otherwise>
                                    Restaurantes Sugeridos (${fn:length(restaurantesRecomendados)})
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            Restaurantes Sugeridos
                        </c:otherwise>
                    </c:choose>
                </h4>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty restaurantesRecomendados && fn:length(restaurantesRecomendados) > 0}">
                        <div class="row">
                            <c:forEach items="${restaurantesRecomendados}" var="restaurante" varStatus="status">
                                <div class="col-md-6 col-lg-4 mb-4">
                                    <div class="card restaurant-card h-100">
                                        <div class="restaurant-img-placeholder">
                                            <i class="fas fa-utensils fa-3x"></i>
                                            <div class="position-badge">#${status.index + 1}</div>
                                        </div>
                                        <div class="card-body">
                                            <h5 class="card-title">
                                                <a href="${pageContext.request.contextPath}/detalleRestaurante?id=${restaurante.id}" class="text-decoration-none text-dark">
                                                    <c:out value="${not empty restaurante.nombre ? restaurante.nombre : 'Sin nombre'}"/>
                                                </a>
                                            </h5>
                                            <p class="restaurant-type mb-2">
                                                <i class="fas fa-utensils me-1"></i>
                                                <c:out value="${not empty restaurante.tipoComida ? restaurante.tipoComida : 'No especificado'}"/>
                                            </p>

                                            <div class="mb-2">
                                                <c:forEach begin="1" end="5" var="i">
                                                    <i class="fas fa-star ${i <= restaurante.puntajePromedio ? 'text-warning' : 'text-secondary'}"></i>
                                                </c:forEach>
                                                <span class="ms-1">(<fmt:formatNumber value="${restaurante.puntajePromedio}" pattern="#.##"/>)</span>
                                            </div>

                                            <p class="card-text">
                                                <c:out value="${not empty restaurante.descripcion ? restaurante.descripcion : 'Sin descripción'}"/>
                                            </p>

                                            <div class="d-grid gap-2 mt-3">
                                                <a href="${pageContext.request.contextPath}/detalleRestaurante?id=${restaurante.id}" class="btn btn-outline-primary btn-sm">
                                                    <i class="fas fa-info-circle me-1"></i>Ver detalles
                                                </a>
                                                <c:if test="${not empty restaurante.menus}">
                                                    <button class="btn btn-ver-menu btn-sm"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#menuModal${restaurante.id}">
                                                        <i class="fas fa-utensils me-1"></i>Ver Menú
                                                    </button>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Modal para el menú -->
                                <c:if test="${not empty restaurante.menus}">
                                    <div class="modal fade" id="menuModal${restaurante.id}" tabindex="-1">
                                        <div class="modal-dialog modal-lg">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">Menú de ${restaurante.nombre}</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="row">
                                                        <c:forEach items="${restaurante.menus}" var="menu">
                                                            <div class="col-md-6 mb-3">
                                                                <div class="card h-100">
                                                                    <div class="card-body">
                                                                        <h6 class="card-subtitle mb-2 text-muted">Menú</h6>
                                                                        <p class="card-text">${menu.contenido}</p>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                                        Cerrar
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-3">
                            <i class="fas fa-info-circle fa-2x text-muted mb-2"></i>
                            <p class="text-muted">No hay restaurantes sugeridos disponibles</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>

    <!-- Listado principal de restaurantes -->
    <div class="row" id="restaurantes-container">
        <c:choose>
            <c:when test="${empty restaurantes}">
                <div class="col-12">
                    <div class="card no-restaurants">
                        <i class="fas fa-utensils fa-4x mb-3"></i>
                        <h3>No hay restaurantes disponibles</h3>
                        <p class="text-muted">No se encontraron restaurantes que coincidan con tu búsqueda.</p>
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
                                    <a href="${pageContext.request.contextPath}/detalleRestaurante?id=${restaurante.id}" class="text-decoration-none text-dark">
                                        <c:out value="${not empty restaurante.nombre ? restaurante.nombre : 'Sin nombre'}"/>
                                    </a>
                                </h5>
                                <p class="restaurant-type mb-2">
                                    <i class="fas fa-utensils me-1"></i>
                                    <c:out value="${not empty restaurante.tipoComida ? restaurante.tipoComida : 'No especificado'}"/>
                                </p>

                                <div class="mb-2">
                                    <c:choose>
                                        <c:when test="${restaurante.puntajePromedio > 0}">
                                            <c:forEach begin="1" end="5" var="i">
                                                <i class="fas fa-star ${i <= restaurante.puntajePromedio ? 'text-warning' : 'text-secondary'}"></i>
                                            </c:forEach>
                                            <span class="ms-1">(<fmt:formatNumber value="${restaurante.puntajePromedio}" pattern="#.##"/>)</span>
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
                                        <a href="${pageContext.request.contextPath}/detalleRestaurante?id=${restaurante.id}"
                                           class="btn btn-sm btn-primary me-1">
                                            <i class="fas fa-info-circle"></i> Detalles
                                        </a>
                                        <a href="${pageContext.request.contextPath}/calificar?idRestaurante=${restaurante.id}"
                                           class="btn btn-sm btn-outline-success me-1">
                                            <i class="fas fa-star"></i> Calificar
                                        </a>
                                        <c:if test="${not empty restaurante.menuDelDia}">
                                            <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#menuModal${restaurante.id}">
                                                <i class="fas fa-utensils"></i> Almuerzo del Día
                                            </button>
                                        </c:if>
                                        <c:if test="${not empty restaurante.menus}">
                                            <button class="btn btn-sm btn-ver-menu"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#menuModal${restaurante.id}">
                                                <i class="fas fa-utensils"></i> Ver Menú
                                            </button>
                                        </c:if>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Modal para el menú -->
                    <c:if test="${not empty restaurante.menus}">
                        <div class="modal fade" id="menuModal${restaurante.id}" tabindex="-1">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Menú de ${restaurante.nombre}</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row">
                                            <c:forEach items="${restaurante.menus}" var="menu">
                                                <div class="col-md-6 mb-3">
                                                    <div class="card h-100">
                                                        <div class="card-body">
                                                            <h6 class="card-subtitle mb-2 text-muted">Menú</h6>
                                                            <p class="card-text">${menu.contenido}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                            Cerrar
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
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function () {
    // Manejar el formulario de búsqueda
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const searchTerm = document.getElementById('searchInput').value.trim();
            buscarRestaurantes(searchTerm);
        });
    }

    // Inicializar eventos
    inicializarBotonesVoto();

    // Verificar si hay restaurantes cargados
    const hasRestaurants = document.querySelectorAll('.restaurant-card').length > 0;
    const hasSearchParam = new URL(window.location.href).searchParams.get('busqueda');

    if (!hasRestaurants && !hasSearchParam) {
        cargarRestaurantes();
    }
});

function inicializarBotonesVoto() {
    // Remover listeners previos
    document.querySelectorAll('.btn-like').forEach(function (btn) {
        const newBtn = btn.cloneNode(true);
        btn.parentNode.replaceChild(newBtn, btn);
    });

    // Agregar nuevos listeners
    document.querySelectorAll('.btn-like').forEach(function (btn) {
        btn.addEventListener('click', function () {
            const idRestaurante = btn.getAttribute('data-id');
            const icono = btn.querySelector('i');
            
            fetch('${pageContext.request.contextPath}/SvMenuDelDia', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: 'id=' + encodeURIComponent(idRestaurante)
            })
            .then(response => response.json())
            .then(data => {
                if (data.votoAgregado) {
                    // Voto agregado - botón rojo
                    btn.classList.add('voted');
                    icono.className = 'fas fa-heart';
                    btn.style.color = '#dc3545';
                } else {
                    // Voto removido - botón gris
                    btn.classList.remove('voted');
                    icono.className = 'far fa-heart';
                    btn.style.color = '#6c757d';
                }
                // Ya no se muestra modal de confirmación
            })
            .catch(error => {
                console.error('Error:', error);
            });
        });
    });
}

function cargarRestaurantes() {
    const container = document.getElementById('restaurantes-container');
    if (!container) return;

    container.innerHTML = '<div class="col-12 text-center"><div class="spinner-border text-primary"></div></div>';

    fetch('${pageContext.request.contextPath}/inicio')
        .then(response => response.text())
        .then(html => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;
            const restaurantesHTML = tempDiv.querySelector('#restaurantes-container').innerHTML;
            container.innerHTML = restaurantesHTML;
            inicializarBotonesVoto(); // Reinicializar después de cargar
        })
        .catch(error => {
            console.error('Error:', error);
            container.innerHTML = '<div class="col-12 text-center text-danger">Error al cargar restaurantes</div>';
        });
}

function buscarRestaurantes(searchTerm) {
    const container = document.getElementById('restaurantes-container');
    if (!container) return;

    container.innerHTML = '<div class="col-12 text-center"><div class="spinner-border text-primary"></div></div>';

    const url = '${pageContext.request.contextPath}/inicio?busqueda=' + encodeURIComponent(searchTerm);

    fetch(url)
        .then(response => response.text())
        .then(html => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;
            const restaurantesHTML = tempDiv.querySelector('#restaurantes-container').innerHTML;
            container.innerHTML = restaurantesHTML;
            inicializarBotonesVoto(); // Reinicializar después de buscar
            window.history.pushState({}, '', url);
        })
        .catch(error => {
            console.error('Error:', error);
            container.innerHTML = '<div class="col-12 text-center text-danger">Error en la búsqueda</div>';
        });
}
        if (typeof bootstrap !== 'undefined' && bootstrap.Modal) {
            document.querySelectorAll('.modal').forEach(modalEl => {
                new bootstrap.Modal(modalEl);
            });
        }

        // Función para manejar likes
        document.querySelectorAll('.btn-like').forEach(function(btn) {
            btn.addEventListener('click', function() {
                if (btn.classList.contains('clicked')) return;

                const idRestaurante = btn.getAttribute('data-id');

                fetch('${pageContext.request.contextPath}/SvMenuDelDia', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: 'id=' + encodeURIComponent(idRestaurante)
                });

                btn.classList.add('clicked');
                var modal = new bootstrap.Modal(document.getElementById('modalVotoConfirmado'));
                modal.show();
            });
        });

        // Cerrar modales de menú cuando se cierra el modal de confirmación
        var modalVotoConfirmado = document.getElementById('modalVotoConfirmado');
        if (modalVotoConfirmado) {
            modalVotoConfirmado.addEventListener('hidden.bs.modal', function() {
                document.querySelectorAll('.modal.show').forEach(function(modal) {
                    if (modal.id.startsWith('menuModal')) {
                        var bsModal = bootstrap.Modal.getInstance(modal);
                        if (bsModal) {
                            bsModal.hide();
                        }
                    }
                });
            });
        }
    });
</script>

<!-- Modal de voto confirmado -->
<div class="modal fade" id="modalVotoConfirmado" tabindex="-1"
     aria-labelledby="modalVotoConfirmadoLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="modalVotoConfirmadoLabel">
                    <i class="fas fa-check-circle me-2"></i>Voto confirmado
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Cerrar"></button>
            </div>
            <div class="modal-body text-center">
                ¡Tu voto ha sido registrado correctamente!
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success"
                        data-bs-dismiss="modal">Aceptar
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Modal para mostrar menú del día -->
<c:forEach items="${restaurantes}" var="restaurante">
    <c:if test="${not empty restaurante.menuDelDia}">
        <div class="modal fade" id="menuModal${restaurante.id}" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Menú de ${restaurante.nombre}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body menu-container">
                        <div class="position-relative alert alert-info">
                            <strong>Menú del Día:</strong>
                            <pre style="white-space: pre-wrap;" class="mb-0">${restaurante.menuDelDia.descripcion}</pre>

                            <!-- Solo mostrar botón si hay usuario logueado -->
                            <c:if test="${not empty sessionScope.usuario}">
                                <!-- Verificar si usuario ya votó usando el atributo del request -->
                                <c:set var="usuarioYaVoto" value="${requestScope['usuarioYaVoto_'.concat(restaurante.id)]}" />
                                
                                <!-- Botón de voto -->
                                <button class="btn-like position-absolute ${usuarioYaVoto ? 'voted' : ''}"
                                        type="button" 
                                        data-id="${restaurante.id}" 
                                        title="Me gusta"
                                        style="color: ${usuarioYaVoto ? '#dc3545' : '#6c757d'}; top: 10px; right: 10px; background: none; border: none; font-size: 1.5rem; cursor: pointer;">
                                    <i class="${usuarioYaVoto ? 'fas' : 'far'} fa-heart"></i>
                                </button>
                            </c:if>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</c:forEach>
</body>
</html>