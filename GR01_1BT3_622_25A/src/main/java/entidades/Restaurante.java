package entidades;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurantes")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Restaurante extends Usuario {

    // Atributos específicos del restaurante
    private String nombre;
    private String descripcion;
    private String tipoComida;
    private LocalTime horaApertura;
    private LocalTime horaCierre;
    private Double puntajePromedio = 0.0;
    private int precio;
    private Double distanciaUniversidad;
    private int calidad;
    private int tiempoEspera;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "restaurante_historias", joinColumns = @JoinColumn(name = "restaurante_id"), foreignKey = @ForeignKey(name = "fk_restaurante_historias"))
    @Column(name = "historia", length = 1000)
    private List<String> historias = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Calificacion> calificaciones;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Suscripcion> suscripciones = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_del_dia_id")
    private MenuDelDia menuDelDia;

    public Restaurante() {
    }

    public Restaurante(String nombreUsuario, String contrasena, String email, String nombre,
            String tipoComida) {
        this.setNombreUsuario(nombreUsuario);
        this.setContrasena(contrasena);
        this.setEmail(email);
        this.nombre = nombre;
        this.tipoComida = tipoComida;
        this.setTipoUsuario("RESTAURANTE");
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

    // Métodos getId() y setId() heredados de Usuario - No es necesario redefinirlos
    // Se accede a ellos mediante super.getId() y super.setId() si es necesario

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public Double getPuntajePromedio() {
        return this.puntajePromedio;
    }

    public void setPuntajePromedio(Double puntajePromedio) {
        this.puntajePromedio = puntajePromedio != null ? puntajePromedio : 0.0;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Double getDistanciaUniversidad() {
        return distanciaUniversidad;
    }

    public void setDistanciaUniversidad(Double distanciaUniversidad) {
        this.distanciaUniversidad = distanciaUniversidad;
    }

    public int getCalidad() {
        return calidad;
    }

    public void setCalidad(int calidad) {
        this.calidad = calidad;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public List<String> getHistorias() {
        if (this.historias == null) {
            this.historias = new ArrayList<>();
        }
        return this.historias;
    }

    public void setHistorias(List<String> historias) {
        this.historias = historias;
    }

    public void agregarHistoria(String historia) {
        this.historias.add(historia);
    }

    public List<Suscripcion> getSuscripciones() {
        return suscripciones;
    }

    public void setSuscripciones(List<Suscripcion> suscripciones) {
        this.suscripciones = suscripciones;
    }

    public MenuDelDia getMenuDelDia() {
        return menuDelDia;
    }

    public void setMenuDelDia(MenuDelDia menuDelDia) {
        this.menuDelDia = menuDelDia;
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + getId() + // Acceso correcto al id heredado
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipoComida='" + tipoComida + '\'' +
                ", horaApertura=" + horaApertura +
                ", horaCierre=" + horaCierre +
                ", historias=" + historias +
                '}';
    }
}
