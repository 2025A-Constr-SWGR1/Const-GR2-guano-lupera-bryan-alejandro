package servicios;

import DAO.PlanificacionDAO;
import DAO.UsuarioDAOImpl;
import entidades.Comensal;
import entidades.Planificacion;
import entidades.Restaurante;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class PlanificacionService {
    private final PlanificacionDAO planificacionDAO;
    private final NotificacionServiceInterface notificacionService;
    private static final Random random = new Random();
    private EntityManagerFactory emf;

    public PlanificacionService(PlanificacionDAO planificacionDAO) {
        if (planificacionDAO == null) {
            this.emf = null;
        } else {
            this.emf = Persistence.createEntityManagerFactory("UFood_PU");
        }
        this.planificacionDAO = planificacionDAO;

        this.usuarioDAO = new UsuarioDAOImpl(emf);
        notificacionService = null;

    }

    // Para el test
    public PlanificacionService(NotificacionServiceInterface notificacionService) {
        this.planificacionDAO = null;
        // this.calificacionDAO = null;
        this.usuarioDAO = null;
        this.notificacionService = notificacionService;
    }

    public Planificacion crearPlanificacion(String nombre, String hora, Comensal comensal) {
        validarParametrosCreacion(nombre, hora);
        Planificacion planificacion = new Planificacion(hora, nombre);
        if (comensal == null) {
            throw new IllegalArgumentException("Comensal no encontrado");
        }
        planificacion.setComensalPlanificador(comensal);
        return planificacion;
    }

    public void guardarPlanificacion(Planificacion planificacion) {
        planificacionDAO.save(planificacion);
    }

    private void validarParametrosCreacion(String nombre, String hora) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (hora == null || hora.trim().isEmpty()) {
            throw new IllegalArgumentException("La hora es requerida");
        }
    }

    public Boolean agregarComensales(Planificacion planificacion, List<Comensal> comensales) {
        for (Comensal comensal : comensales) {
            if (comensal != null) {
                planificacion.addComensal(comensal);
            }
        }
        return true;
    }

    public Boolean recomendarRestaurante(Restaurante restaurante) {
        final Double PUNTAJE_MINIMO = 3.5;
        final Double DISTANCIA_MAXIMA = 5.0;
        final int TIEMPO_MAXIMO_ESPERA = 30;

        if (restaurante == null) {
            throw new IllegalArgumentException("El restaurante no puede ser nulo");
        }
        if (restaurante.getPuntajePromedio() == null || restaurante.getDistanciaUniversidad() == null
                || restaurante.getTiempoEspera() == 0) {
            throw new IllegalArgumentException("Los atributos del restaurante no pueden ser nulos");
        }

        return restaurante.getPuntajePromedio() >= PUNTAJE_MINIMO
                && restaurante.getDistanciaUniversidad() <= DISTANCIA_MAXIMA
                && restaurante.getTiempoEspera() <= TIEMPO_MAXIMO_ESPERA;
    }

    public int calcularMinutosRestantesParaVotacion(LocalDateTime ahora, LocalDateTime horaLimite) {

        return (int) Duration.between(ahora, horaLimite).toMinutes();
    }

    public Restaurante obtenerRestauranteMasVotado(Map<Restaurante, Integer> votos) {

        return votos.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

    }

    public void cancelarPlanificacion(Planificacion planificacion) {
        // List<Comensal> comensales = planificacion.getComensales();

        // for (Comensal comensal : comensales) {
        // notificar(comensal, "La planificación ha sido cancelada");
        // }

        if (planificacion == null) {
            throw new IllegalArgumentException("La planificación no puede ser nula");
        }
        if (!"Activa".equalsIgnoreCase(planificacion.getEstado())) {
            throw new IllegalStateException("Solo se puede cancelar una planificación activa");
        }
        planificacion.setEstado("Cancelado");
    }

    public Restaurante resolverEmpateEnVotacion(Map<Restaurante, Integer> votos) {
        int maxVotos = votos.values().stream().max(Integer::compare).orElse(0);
        List<Restaurante> empatados = votos.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotos)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (empatados.size() > 1) {
            return empatados.get(random.nextInt(empatados.size()));
        }
        return empatados.isEmpty() ? null : empatados.get(0);
    }

    public void confirmarRestauranteDelGrupo(Planificacion planificacion, String restauranteConfirmado) {
        List<Comensal> comensales = planificacion.getComensales();
        for (Comensal comensal : comensales) {
            notificacionService.notificarRestauranteElegido(comensal, "Se ha confirmado: " + restauranteConfirmado);
        }
    }

    public void notificar(Comensal comensal, String restauranteConfirmado) {

    }
}
