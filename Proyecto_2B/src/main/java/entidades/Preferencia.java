package entidades;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "preferencias") // Añade nombre de tabla explícito
public class Preferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoComida;
    private LocalTime horaApertura;
    private LocalTime horaCierre;
    private Double distanciaUniversidad;

    @ManyToOne
    @JoinColumn(name = "comensal_id")
    private Comensal comensal;

    public Preferencia() {}

    public Preferencia(String tipoComida, LocalTime horaApertura,
                       LocalTime horaCierre, Double distancia, Comensal comensal) {
        this.tipoComida = tipoComida;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.distanciaUniversidad = distancia;
        this.comensal = comensal;
    }

    // Añade getter y setter para comensal
    public Comensal getComensal() {
        return comensal;
    }

    public void setComensal(Comensal comensal) {
        this.comensal = comensal;
    }

    // Resto de getters y setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    public Double getDistanciaUniversidad() {
        return distanciaUniversidad;
    }

    public void setDistanciaUniversidad(Double distanciaUniversidad) {
        this.distanciaUniversidad = distanciaUniversidad;
    }
}