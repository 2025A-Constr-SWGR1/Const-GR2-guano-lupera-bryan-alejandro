package entidades;

import jakarta.persistence.*;

@Entity
public class MenuDelDia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String descripcion;
    private int cantidadVotos;
    
    // NO agregues la relación @OneToMany aquí porque VotoMenuDelDia no tiene relación directa con MenuDelDia
    // Los votos están relacionados con el Restaurante, no con el MenuDelDia
    
    public MenuDelDia() {}
    
    public MenuDelDia(String descripcion, int cantidadVotos) {
        this.descripcion = descripcion;
        this.cantidadVotos = cantidadVotos;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getCantidadVotos() { return cantidadVotos; }
    public void setCantidadVotos(int cantidadVotos) { this.cantidadVotos = cantidadVotos; }
}
