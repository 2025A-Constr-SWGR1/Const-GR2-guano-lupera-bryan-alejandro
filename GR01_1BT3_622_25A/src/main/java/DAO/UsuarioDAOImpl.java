package DAO;

import entidades.Comensal;
import entidades.Restaurante;
import entidades.Usuario;
import jakarta.persistence.*;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    private final EntityManagerFactory emf;

    public UsuarioDAOImpl(EntityManagerFactory emf) {
        if (emf == null) {
            throw new IllegalArgumentException("EntityManagerFactory cannot be null");
        }
        this.emf = emf;
    }


    @Override
    public Usuario findByNombreUsuario(String nombreUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario", Usuario.class)
                    .setParameter("nombreUsuario", nombreUsuario)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Comensal obtenerComensalPorNombreUsuario(String nombreUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Comensal c LEFT JOIN FETCH c.notificaciones WHERE c.nombreUsuario = :nombre", Comensal.class)
                    .setParameter("nombre", nombreUsuario)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                em.merge(usuario);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al guardar usuario", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Usuario usuario) {
        this.save(usuario); // Unificamos con el método save
    }

    @Override
    public Usuario findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Restaurante> obtenerTodosRestaurantes() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM Restaurante r", Restaurante.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Restaurante> buscarRestaurantesPorTipo(String tipoComida) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM Restaurante r WHERE r.tipoComida = :tipo", Restaurante.class)
                    .setParameter("tipo", tipoComida)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Restaurante> buscarRestaurantes(String busqueda) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM Restaurante r WHERE " +
                                    "LOWER(r.nombre) LIKE LOWER(:busqueda) OR " +
                                    "LOWER(r.tipoComida) LIKE LOWER(:busqueda)", Restaurante.class)
                    .setParameter("busqueda", "%" + busqueda + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Comensal obtenerComensalPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT c FROM Comensal c LEFT JOIN FETCH c.notificaciones WHERE c.id = :id", Comensal.class)
                .setParameter("id", id)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Double calcularPromedioCalificaciones(Long restauranteId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT AVG(c.puntaje) FROM Calificacion c WHERE c.restaurante.id = :restauranteId",
                            Double.class)
                    .setParameter("restauranteId", restauranteId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0.0;
        } finally {
            em.close();
        }
    }

    @Override
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}
