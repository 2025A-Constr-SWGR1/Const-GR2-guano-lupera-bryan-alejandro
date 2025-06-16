package DAO;

import entidades.MenuDelDia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MenuDAO {

    private EntityManagerFactory emf;

    public MenuDAO() {
        this.emf = Persistence.createEntityManagerFactory("UFood_PU");
    }

    public void guardar(MenuDelDia menuDelDia) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(menuDelDia);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void cerrar() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

}
