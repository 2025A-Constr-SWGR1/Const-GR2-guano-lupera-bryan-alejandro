package servicios;

import DAO.CalificacionDAO;
import entidades.Comensal;
import entidades.Restaurante;
import DAO.UsuarioDAO;
import java.util.*;
import java.util.stream.Collectors;

public class RecomendacionService {
    private final UsuarioDAO usuarioDAO;
    private final CalificacionDAO calificacionDAO;

    // Constructor para producción
    public RecomendacionService(UsuarioDAO usuarioDAO, CalificacionDAO calificacionDAO) {
        this.usuarioDAO = usuarioDAO;
        this.calificacionDAO = calificacionDAO;
    }

    // Metodo principal para producción
    public List<Restaurante> obtenerRecomendaciones(Comensal comensal) {
        if (!esComensalValido(comensal)) {
            return Collections.emptyList();
        }

        List<Restaurante> restaurantes = usuarioDAO.obtenerTodosRestaurantes();
        actualizarPuntajes(restaurantes);

        return filtrarYOrdenar(restaurantes, comensal.getTipoComidaFavorita());
    }

    // Metodo sobrecargado para testing
    public List<Restaurante> obtenerRecomendaciones(Comensal comensal, List<Restaurante> restaurantes) {
        return esComensalValido(comensal) ?
                filtrarYOrdenar(new ArrayList<>(restaurantes), comensal.getTipoComidaFavorita()) :
                Collections.emptyList();
    }

    // --- Métodos privados compartidos ---
    private List<Restaurante> filtrarYOrdenar(List<Restaurante> restaurantes, String tipoComida) {
        String tipoNormalizado = normalizarTipoComida(tipoComida);

        return restaurantes.stream()
                .filter(this::tieneDatosValidos)
                .filter(r -> coincideTipoComida(r, tipoNormalizado))
                .sorted(Comparator.comparingDouble(Restaurante::getPuntajePromedio).reversed())
                .collect(Collectors.toList());
    }

    private void actualizarPuntajes(List<Restaurante> restaurantes) {
        restaurantes.forEach(r ->
                r.setPuntajePromedio(
                        Optional.ofNullable(calificacionDAO.calcularPromedioCalificaciones(r.getId()))
                                .orElse(0.0)
                )
        );
    }

    private boolean esComensalValido(Comensal comensal) {
        return comensal != null &&
                comensal.getTipoComidaFavorita() != null &&
                !comensal.getTipoComidaFavorita().trim().isEmpty();
    }

    private boolean tieneDatosValidos(Restaurante restaurante) {
        return restaurante.getTipoComida() != null &&
                !restaurante.getTipoComida().trim().isEmpty() &&
                restaurante.getPuntajePromedio() != null;
    }

    private boolean coincideTipoComida(Restaurante restaurante, String tipoBuscado) {
        return normalizarTipoComida(restaurante.getTipoComida()).equals(tipoBuscado);
    }

    private String normalizarTipoComida(String tipoComida) {
        return tipoComida.trim().toLowerCase();
    }
}