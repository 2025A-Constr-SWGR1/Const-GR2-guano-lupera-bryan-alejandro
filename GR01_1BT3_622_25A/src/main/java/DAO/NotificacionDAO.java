package DAO;

import entidades.Notificacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class NotificacionDAO {
    private EntityManagerFactory emf;

    public NotificacionDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void actualizar(Notificacion notificacion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(notificacion);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Notificacion buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Notificacion.class, id);
        } finally {
            em.close();
        }
    }
}
