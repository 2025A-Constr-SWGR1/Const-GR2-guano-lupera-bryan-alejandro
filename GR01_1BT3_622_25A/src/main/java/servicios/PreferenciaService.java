package servicios;

import DAO.UsuarioDAO;
import entidades.Comensal;
import entidades.Preferencia;
import entidades.Restaurante;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class PreferenciaService {
    private final UsuarioDAO usuarioDAO;

    public PreferenciaService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public List<Restaurante> aplicarPreferencia(Map<String, Object> parametrosPreferencia) {
        String tipoComida = (String) parametrosPreferencia.get("tipoComida");
        LocalTime horaApertura = (LocalTime) parametrosPreferencia.get("horaApertura");
        LocalTime horaCierre = (LocalTime) parametrosPreferencia.get("horaCierre");
        Double distancia = (Double) parametrosPreferencia.get("distancia");

        // Obtener todos los restaurantes (ahora son Usuarios de tipo RESTAURANTE)
        List<Restaurante> restaurantes = usuarioDAO.obtenerTodosRestaurantes();
        return restaurantes.stream()
                .filter(r -> r.getTipoComida().equalsIgnoreCase(tipoComida))
                .filter(r -> r.getHoraApertura().isBefore(horaApertura))
                .filter(r -> r.getHoraCierre().isAfter(horaCierre))
                .filter(r -> r.getDistanciaUniversidad() <= distancia)
                .sorted(Comparator.comparingDouble(this::calcularPuntajeRanking).reversed())
                .collect(Collectors.toList());
    }

    private double calcularPuntajeRanking(Restaurante restaurante) {
        return (1 / (1 + restaurante.getDistanciaUniversidad())) * 0.3 +
                restaurante.getPuntajePromedio() * 0.5;
    }

    public void crearPreferencia(String tipoComida, LocalTime horaApertura, LocalTime horaCierre,
                                 Double distanciaUniversidad, Long idComensal) {
        Comensal comensal = (Comensal) usuarioDAO.obtenerComensalPorId(idComensal);
        if (comensal != null) {
            Preferencia preferencia = new Preferencia(tipoComida, horaApertura, horaCierre,
                    distanciaUniversidad, comensal);
            comensal.agregarPreferencia(preferencia);
            usuarioDAO.save(comensal); // Actualiza el comensal con su nueva preferencia
        }
    }
}