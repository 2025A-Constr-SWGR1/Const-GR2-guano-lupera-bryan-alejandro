package entidades;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "comensales")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Comensal extends Usuario {

    @ManyToMany(mappedBy = "comensales")
    private List<Planificacion> planificaciones;

    @OneToMany(mappedBy = "comensal", cascade = CascadeType.ALL)
    private List<Preferencia> preferencias;

    @OneToMany(mappedBy = "comensal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Notificacion> notificaciones = new ArrayList<>();

    @OneToMany(mappedBy = "comensal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Suscripcion> suscripciones = new ArrayList<>();

    @Column(name = "tipo_comida_favorita")
    private String tipoComidaFavorita;

    public Comensal() {
        this.setTipoUsuario("COMENSAL");
    }

    public Comensal(String nombreUsuario, String contrasena, String email, List<Preferencia> preferencias) {
        this.setNombreUsuario(nombreUsuario);
        this.setContrasena(contrasena);
        this.setEmail(email);
        this.setTipoUsuario("COMENSAL");
        this.preferencias = preferencias;
        this.planificaciones = null;

        if (preferencias != null) {
            preferencias.forEach(p -> p.setComensal(this));
        }
    }

    public String getTipoComidaFavorita() {
        return tipoComidaFavorita;
    }

    public void setTipoComidaFavorita(String tipoComidaFavorita) {
        this.tipoComidaFavorita = tipoComidaFavorita;
    }

    public List<Preferencia> getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(List<Preferencia> preferencias) {
        this.preferencias = preferencias;

        if (preferencias != null) {
            preferencias.forEach(p -> p.setComensal(this));
        }
    }

    public void agregarPreferencia(Preferencia preferencia) {
        preferencias.add(preferencia);
        preferencia.setComensal(this);
    }

    public void removerPreferencia(Preferencia preferencia) {
        preferencias.remove(preferencia);
        preferencia.setComensal(null);
    }

    public List<Planificacion> getPlanificaciones() {
        return planificaciones;
    }

    public void setPlanificaciones(List<Planificacion> planificaciones) {
        this.planificaciones = planificaciones;
    }

    public List<Suscripcion> getSuscripciones() {
        return suscripciones;
    }

    public void setSuscripciones(List<Suscripcion> suscripciones) {
        this.suscripciones = suscripciones;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public void agregarNotificacion(String mensaje) {
        System.out.println("dentro de agregarNotificacion");
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje);
        notificacion.setComensal(this); // Relación bidireccional
        System.out.println(notificacion.getMensaje());
        notificaciones.add(notificacion);
    }

    @Override
    public String toString() {
        return "Comensal: " + super.getId();
    }
}
