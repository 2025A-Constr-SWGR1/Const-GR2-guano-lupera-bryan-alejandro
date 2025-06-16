package servicios;

import entidades.Comensal;

public interface NotificacionServiceInterface {
    void notificarRestauranteElegido(Comensal comensal, String mensaje);
}
