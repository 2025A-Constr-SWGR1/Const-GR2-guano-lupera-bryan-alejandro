package entidades;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Planificacion {
    public String nombre;
    public String hora;
    public String estado;
    public String estadoVotacion; // "No iniciada", "En progreso", "Terminada"

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "planificacion_comensal", joinColumns = @JoinColumn(name = "planificacion_id"), inverseJoinColumns = @JoinColumn(name = "comensal_id"))
    private List<Comensal> comensales = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "idComensalPlanificador")
    private Comensal comensalPlanificador;

    // Changed to ManyToMany to support multiple restaurants
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "planificacion_restaurante", joinColumns = @JoinColumn(name = "planificacion_id"), inverseJoinColumns = @JoinColumn(name = "restaurante_id"))
    private List<Restaurante> restaurantes = new ArrayList<>();

    // Restaurant with the highest number of votes
    @ManyToOne
    @JoinColumn(name = "restaurante_ganador_id")
    private Restaurante restauranteGanador;

    public Planificacion() {
    }

    public Planificacion(String hora, String nombre) {
        this.hora = hora;
        this.nombre = nombre;
        estado = "Activa";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public List<Comensal> getComensales() {
        return comensales;
    }

    public void setComensales(List<Comensal> comensales) {
        this.comensales = comensales;
    }

    public void addComensal(Comensal comensal) {
        if (this.comensales == null) {
            this.comensales = new ArrayList<>();
        }
        if (this.comensales.contains(comensal)) {
            throw new IllegalArgumentException("El comensal ya está en esta planificación");
        }
        this.comensales.add(comensal);
    }

    public List<Restaurante> getRestaurantes() {
        if (this.restaurantes == null) {
            this.restaurantes = new ArrayList<>();
        }
        return restaurantes;
    }

    public void setRestaurantes(List<Restaurante> restaurantes) {
        this.restaurantes = restaurantes;
    }

    public void addRestaurante(Restaurante restaurante) {
        if (restaurante == null || restaurante.getNombre() == null || restaurante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurante no válido");
        }
        if (this.restaurantes == null) {
            this.restaurantes = new ArrayList<>();
        }
        if (!this.restaurantes.contains(restaurante)) {
            this.restaurantes.add(restaurante);
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Comensal getComensalPlanificador() {
        return comensalPlanificador;
    }

    public void setComensalPlanificador(Comensal comensalPlanificador) {
        this.comensalPlanificador = comensalPlanificador;
    }
}
