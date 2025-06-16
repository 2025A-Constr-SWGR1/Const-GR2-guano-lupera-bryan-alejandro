<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="list-group">
    <c:choose>
        <c:when test="${empty notificaciones}">
            <li class="list-group-item text-muted">No tienes notificaciones.</li>
        </c:when>
        <c:otherwise>
            <c:forEach var="notificacion" items="${notificaciones}">
                <li class="list-group-item notificacion-item" data-id="${notificacion.id}" data-leida="${notificacion.leida}">
                    <i class="fas fa-info-circle text-primary me-2"></i>
                    <c:out value="${notificacion.mensaje}"/>
                    <c:if test="${!notificacion.leida}">
                        <span class="badge bg-secondary float-end">Nuevo</span>
                        <button class="btn btn-sm btn-outline-success ms-2 marcar-leida-btn float-end">Marcar como leída</button>
                    </c:if>
                    <br>
                    <small class="text-muted">
                        <%-- Formato de fecha --%>
                        <%
                            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                            entidades.Notificacion notif = (entidades.Notificacion) pageContext.getAttribute("notificacion");
                            out.print(notif.getFechaCreacion().format(formatter));
                        %>
                    </small>
                </li>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</ul>
<script>
// Lógica para marcar como leída visualmente y por backend
setTimeout(function() {
    document.querySelectorAll('.marcar-leida-btn').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var li = btn.closest('.notificacion-item');
            var id = li.getAttribute('data-id');
            fetch('notificaciones/leida?id=' + id, {
                method: 'POST',
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            });
            // Engaño visual: ocultar botón y badge, marcar como leída visualmente
            btn.style.display = 'none';
            var badge = li.querySelector('.badge');
            if (badge) badge.style.display = 'none';
            li.setAttribute('data-leida', 'true');
        });
    });
}, 100);
</script>
