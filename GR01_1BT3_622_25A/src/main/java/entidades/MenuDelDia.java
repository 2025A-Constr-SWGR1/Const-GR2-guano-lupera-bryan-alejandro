package entidades;

import jakarta.persistence.*;

@Entity
public class MenuDelDia extends Menu {

    private int cantidadVotos;

    public MenuDelDia(String descripcion, int cantidadVotos) {
        super(descripcion);
        this.cantidadVotos = cantidadVotos;
    }

    public MenuDelDia() {}

    // Getters y setters
    public int getCantidadVotos() {
        return cantidadVotos;
    }

    public void setCantidadVotos(int cantidadVotos) {
        this.cantidadVotos = cantidadVotos;
    }
}
