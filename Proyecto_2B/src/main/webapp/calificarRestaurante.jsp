<%@ page import="entidades.Restaurante" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="es">

<head>
    <title>U-Food | Calificar Restaurante</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap"
          rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="styles.css?v=1.0">
</head>

<body>
<div class="container mt-5">
    <% request.setAttribute("titulo", "Calificar Restaurante" ); request.setAttribute("botonAtras",
            true); %>
    <%@ include file="layout/header.jsp" %>

    <div class="card shadow">
        <div class="card-body">
            <div class="restaurant-info">
                <% Restaurante restaurante=(Restaurante) request.getAttribute("restaurante"); if
                (restaurante !=null) { %>
                <div class="restaurant-name">
                    <%= restaurante.getNombre() %>
                </div>
                <div class="restaurant-type mb-2">
                    <i class="fas fa-utensils me-1"></i>
                    <%= restaurante.getTipoComida() %>
                </div>
                <p class="mb-0">
                    <%= restaurante.getDescripcion() %>
                </p>
                <% } else { %>
                <div class="alert alert-warning">No se encontró información del
                    restaurante</div>
                <% } %>
            </div>

            <form action="${pageContext.request.contextPath}/calificar" method="POST">
                <input type="hidden" name="action" value="crear">
                <input type="hidden" name="idRestaurante"
                       value="<%= restaurante != null ? restaurante.getId() : "" %>">
                <input type="hidden" name="idComensal" value="${sessionScope.usuario.id}">
                <%
                    String[] opcionesDeRetorno = {
                            "Nunca",
                            "No creo",
                            "Tal vez",
                            "Puede que si",
                            "Seguro" };
                    String[] opcionesDeTiempoEspera = {
                            "más de 40 min",
                            "menos de 30 min",
                            "menos de 20 min",
                            "menos de 10 min",
                            "menos de 5 min" };
                    request.setAttribute("opcionesDeRetorno", opcionesDeRetorno);
                    request.setAttribute("opcionesTiempoEspera", opcionesDeTiempoEspera);
                %>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="calidadComida"/>
                    <jsp:param name="tituloCalificacion" value="Califica la comida:"/>
                </jsp:include>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="calidadServicio"/>
                    <jsp:param name="tituloCalificacion" value="Califica el servicio:"/>
                </jsp:include>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="limpieza"/>
                    <jsp:param name="tituloCalificacion" value="Califica la limpieza:"/>
                </jsp:include>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="ambiente"/>
                    <jsp:param name="tituloCalificacion" value="Califica el ambiente:"/>
                </jsp:include>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="relacionPrecioCalidad"/>
                    <jsp:param name="tituloCalificacion" value="Califica la relación Precio-Calidad:"/>
                </jsp:include>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="variedadMenu"/>
                    <jsp:param name="tituloCalificacion" value="Califica la variedad del Menú:"/>
                </jsp:include>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="accesibilidad"/>
                    <jsp:param name="tituloCalificacion" value="Califica la accesibilidad:"/>
                </jsp:include>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="tiempoEspera"/>
                    <jsp:param name="tituloCalificacion" value="Califica el tiempo de espera:"/>
                </jsp:include>

                <jsp:include page="components/calificarComponent.jsp">
                    <jsp:param name="name" value="volveria"/>
                    <jsp:param name="tituloCalificacion" value="Regresaría a este restaurante?"/>
                </jsp:include>

                <div class="mb-4">
                    <label for="comentario" class="form-label">Comentario:</label>
                    <textarea maxlength="100" class="form-control" id="comentario" name="comentario"
                              placeholder="Describe tu experiencia..." rows="4" required></textarea>
                    <p class="contador" id="contador">0/100 caracteres</p>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2">
                    <i class="fas fa-star me-2"></i> Enviar Calificación
                </button>
            </form>
        </div>
    </div>
</div>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const textarea = document.getElementById('comentario');
    const contador = document.getElementById('contador');

    textarea.addEventListener('input', () => {
        const longitud = textarea.value.length;
        console.log(longitud);
        contador.textContent = textarea.value.length + "/100 caracteres";
        console.log(contador.textContent);
        // Color rojo si se acerca al límite
        if (longitud > 90) {
            contador.classList.add("rojo");
            console.log('hola');
        } else {
            contador.classList.remove("rojo");
        }
    });
</script>
</body>

</html>
