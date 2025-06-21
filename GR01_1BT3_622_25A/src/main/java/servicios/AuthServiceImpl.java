package servicios;

import DAO.UsuarioDAO;
import entidades.Comensal;
import entidades.Restaurante;
import entidades.Usuario;
import exceptions.ServiceException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class AuthServiceImpl implements AuthService {
    private final UsuarioDAO usuarioDAO;

    public AuthServiceImpl(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public List<Restaurante> obtenerTodosRestaurantes() {
        return usuarioDAO.obtenerTodosRestaurantes();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }

    @Override
    public Usuario login(String nombreUsuario, String contrasena) throws ServiceException {
        Usuario usuario = usuarioDAO.findByNombreUsuario(nombreUsuario);

        if (usuario == null || !hashPassword(contrasena).equals(usuario.getContrasena())) {
            throw new ServiceException("Credenciales inválidas");
        }
        return usuario;
    }

    @Override
    public void registrarUsuarioRestaurante(Usuario usuario) throws ServiceException {
        if (usuarioDAO.findByNombreUsuario(usuario.getNombreUsuario()) != null) {
            throw new ServiceException("El nombre de usuario ya existe");
        }

        Restaurante restaurante = new Restaurante();
        restaurante.setNombreUsuario(usuario.getNombreUsuario());
        restaurante.setContrasena(hashPassword(usuario.getContrasena()));
        restaurante.setEmail(usuario.getEmail());
        restaurante.setTipoUsuario("RESTAURANTE");

        usuarioDAO.save(restaurante);
    }

    @Override
    public void registrarComensal(Usuario usuario, String tipoComidaFavorita) throws ServiceException {
        if (usuarioDAO.findByNombreUsuario(usuario.getNombreUsuario()) != null) {
            throw new ServiceException("El nombre de usuario ya existe");
        }

        Comensal comensal = new Comensal();
        comensal.setNombreUsuario(usuario.getNombreUsuario());
        comensal.setContrasena(hashPassword(usuario.getContrasena()));
        comensal.setEmail(usuario.getEmail());
        comensal.setTipoComidaFavorita(tipoComidaFavorita); // Nuevo campo
        comensal.setTipoUsuario("COMENSAL");

        usuarioDAO.save(comensal);
    }
}
