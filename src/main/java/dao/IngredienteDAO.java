package dao;

import model.Ingrediente;
import model.PersistentEntity;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class IngredienteDAO extends AbstractEntityDAO<Ingrediente> {



    public Ingrediente findByNombre(String nombre) {
        TypedQuery<Ingrediente> query = em.createNamedQuery("Ingrediente.findByNombre", Ingrediente.class);
        query.setParameter("nombre", nombre);
        List<Ingrediente> ingredientes = query.getResultList();
        return ingredientes.isEmpty() ? null : ingredientes.get(0);
    }

    public List<String> findIngredientesByRecetaId(Long recetaId) {
        TypedQuery<String> query = em.createQuery(
                "SELECT i.nombre FROM Ingrediente i JOIN i.recetas r WHERE r.id = :recetaId", String.class);
        query.setParameter("recetaId", recetaId);
        return query.getResultList();
    }



    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}
