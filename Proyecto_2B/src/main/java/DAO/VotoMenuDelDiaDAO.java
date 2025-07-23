package DAO;

import entidades.VotoMenuDelDia;
import entidades.Usuario;
import entidades.Restaurante;
import jakarta.persistence.*;

public class VotoMenuDelDiaDAO {
    
    private EntityManagerFactory emf;
    
    public VotoMenuDelDiaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public VotoMenuDelDia buscarVotoPorUsuarioYRestaurante(Long usuarioId, Long restauranteId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VotoMenuDelDia> query = em.createQuery(
                "SELECT v FROM VotoMenuDelDia v WHERE v.usuario.id = :usuarioId AND v.restaurante.id = :restauranteId",
                VotoMenuDelDia.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("restauranteId", restauranteId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public void guardarVoto(VotoMenuDelDia voto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(voto);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public void eliminarVoto(VotoMenuDelDia voto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            VotoMenuDelDia votoManaged = em.merge(voto);
            em.remove(votoManaged);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}