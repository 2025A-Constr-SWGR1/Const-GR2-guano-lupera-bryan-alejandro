package servicios;

import entidades.Usuario;
import exceptions.ServiceException;

public interface AuthService {
    Usuario login(String nombreUsuario, String contrasena) throws ServiceException;
    void registrarUsuarioRestaurante(Usuario usuario) throws ServiceException;
    void registrarComensal(Usuario usuario, String tipoComidaFavorita) throws ServiceException;
}