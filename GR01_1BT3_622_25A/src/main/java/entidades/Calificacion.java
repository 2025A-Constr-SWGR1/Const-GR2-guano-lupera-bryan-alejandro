package entidades;

import jakarta.persistence.*;

@Entity
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double puntaje;
    private String comentario;
    @ManyToOne
    @JoinColumn(name = "idComensal")
    private Comensal comensal;
    @ManyToOne
    @JoinColumn(name = "idRestaurante")
    private Restaurante restaurante;

    public Calificacion() {
    }

    public Calificacion(Double puntaje, String comentario, Comensal comensal, Restaurante restaurante) {
        this.puntaje = puntaje;
        this.comentario = comentario;
        this.comensal = comensal;
        this.restaurante = restaurante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Double puntaje) {
        this.puntaje = puntaje;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Comensal getComensal() {
        return comensal;
    }

    public void setComensal(Comensal comensal) {
        this.comensal = comensal;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    @Override
    public String toString() {
        return "Calificacion{" +
                "id=" + id +
                ", puntaje=" + puntaje +
                ", comentario='" + comentario + '\'' +
                ", comensal=" + comensal +
                ", restaurante=" + restaurante +
                '}';
    }
}
