package dao;

import model.Comentario;
import model.PersistentEntity;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ComentarioDAO extends AbstractEntityDAO<Comentario>{


    public List<Comentario> findByRecetaId(Long recetaId) {
        TypedQuery<Comentario> query = em.createQuery(
                "SELECT c FROM Comentario c JOIN FETCH c.usuario WHERE c.receta.id = :recetaId ORDER BY c.fecha ASC", Comentario.class);
        query.setParameter("recetaId", recetaId);
        return query.getResultList();
    }

    public String findFechaByComentario(Comentario comentario) {
        TypedQuery<String> query = em.createQuery(
                "SELECT c.fecha FROM Comentario c WHERE c = :comentario", String.class);
        query.setParameter("comentario", comentario);
        List<String> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }


    public void eliminarComentariosDeReceta(Long idReceta) {
        // Eliminar los pasos asociados a la receta
        Query query = em.createQuery("DELETE FROM Comentario c WHERE c.receta.id = :idReceta");
        query.setParameter("idReceta", idReceta);
        query.executeUpdate();
    }


    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}
