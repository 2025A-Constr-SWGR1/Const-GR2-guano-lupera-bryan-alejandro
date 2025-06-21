package DAO;

import entidades.Suscripcion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SuscripcionDAO {

    private static final Logger LOGGER = Logger.getLogger(SuscripcionDAO.class.getName());
    private final EntityManagerFactory emf;

    public SuscripcionDAO(EntityManagerFactory emf) {
        if (emf == null) {
            throw new IllegalArgumentException("EntityManagerFactory cannot be null");
        }
        this.emf = emf;
    }

    public SuscripcionDAO() {
        this.emf = Persistence.createEntityManagerFactory("UFood_PU");
    }

    public boolean crear(Suscripcion nuevaSuscripcion) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(nuevaSuscripcion);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al crear suscripción", e);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean eliminarSuscripcion(Long idComensal, Long idRestaurante) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Suscripcion suscripcion = em.createQuery(
                "SELECT s FROM Suscripcion s WHERE s.comensal.id = :idComensal AND s.restaurante.id = :idRestaurante",
                Suscripcion.class)
                .setParameter("idComensal", idComensal)
                .setParameter("idRestaurante", idRestaurante)
                .getSingleResult();
            if (suscripcion != null) {
                em.remove(em.contains(suscripcion) ? suscripcion : em.merge(suscripcion));
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar suscripción", e);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
