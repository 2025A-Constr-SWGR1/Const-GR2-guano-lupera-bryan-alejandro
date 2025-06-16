<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>U-Food | Comparar Restaurantes</title>
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
        request.setAttribute("titulo", "Comparar restaurantes");
        request.setAttribute("botonAtras", true);
    %>
    <%@ include file="layout/header.jsp" %>
</div>
<c:if test="${not empty error}">
    <div class="alert alert-danger" role="alert">
            ${error}
    </div>
</c:if>

<%--<div class="card shadow">--%>
<div class="container mt-5">
    <div class="card-body">
        <form action="comparar" method="get">
            <input type="hidden" name="accion" value="comparar">

            <div class="mb-3">
                <label for="restaurante1" class="form-label">Primer Restaurante</label>
                <select class="form-select" name="restaurante1" id="restaurante1" required>
                    <option value="">Seleccione un Restaurante</option>
                    <c:forEach items="${restaurantes}" var="rest">
                        <option value="${rest.id}">${rest.nombre}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label for="restaurante2" class="form-label">Segundo Restaurante</label>
                <select class="form-select" name="restaurante2" id="restaurante2" required>
                    <option value="">Seleccione un restaurante</option>
                    <c:forEach items="${restaurantes}" var="rest">
                        <option value="${rest.id}">${rest.nombre}</option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2">
                <i class="fas fa-exchange-alt me-2"></i> Comparar Restaurantes
            </button>
        </form>
    </div>
</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.querySelector('form').addEventListener('submit', function (e) {
        const rest1 = document.getElementById('restaurante1').value;
        const rest2 = document.getElementById('restaurante2').value;

        if (rest1 === rest2) {
            e.preventDefault();
            alert('Por favor seleccione dos restaurantes diferentes');
        }
    });

    document.getElementById('restaurante2').addEventListener('change', function () {
        const rest1 = document.getElementById('restaurante1').value;
        if (this.value === rest1 && this.value !== '') {
            this.value = '';
            alert('Por favor seleccione un restaurante diferente');
        }
    });
</script>
</body>
</html>