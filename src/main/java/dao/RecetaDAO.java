package dao;

import model.PersistentEntity;
import model.Receta;
import model.Usuario;
import model.ValoracionUsuario;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RecetaDAO extends AbstractEntityDAO<Receta> {


    // Método para encontrar un rango específico de recetas
    public List<Receta> findRange(int inicio, int cantidad) {
        TypedQuery<Receta> query = em.createQuery("SELECT r FROM Receta r ORDER BY r.id DESC", Receta.class)
                .setFirstResult(inicio) // Establece el primer resultado del rango
                .setMaxResults(cantidad); // Establece la cantidad máxima de resultados a obtener
        return query.getResultList();
    }


    public List<String> findCantidadIngredientes(Long recetaId) {
        TypedQuery<String> query = em.createNamedQuery("RecetaIngredienteCantidad.findCantidadIngredientes", String.class);
        query.setParameter("recetaId", recetaId);
        return query.getResultList();
    }



    public List<String> findPasosPorReceta(Long recetaId) {
        TypedQuery<String> query = em.createNamedQuery("PasosReceta.findPasosPorReceta", String.class);
        query.setParameter("recetaId", recetaId);
        return query.getResultList();
    }


    public List<ValoracionUsuario> findAllValoracionUsuario(Long recetaId) {
        TypedQuery<ValoracionUsuario> query = em.createNamedQuery("ValoracionUsuario.findAllValoracionUsuario", ValoracionUsuario.class);
        query.setParameter("recetaId", recetaId);
        return query.getResultList();
    }


    public boolean findUsuarioHaValoradoReceta(Usuario usuario, Long idReceta) {

            // Realiza una consulta para verificar si el usuario ya ha valorado la receta
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(v) FROM ValoracionUsuario v WHERE v.usuario = :usuario AND v.receta.id = :idReceta", Long.class);
            query.setParameter("usuario", usuario);
            query.setParameter("idReceta", idReceta);
            Long count = query.getSingleResult();
            // Si el recuento es mayor que cero, significa que el usuario ya ha valorado la receta
            return count > 0;
    }


    public ValoracionUsuario findValoracionUsuario(Usuario usuario, Long idReceta) {
        TypedQuery<ValoracionUsuario> query = em.createQuery(
                "SELECT v FROM ValoracionUsuario v WHERE v.usuario = :usuario AND v.receta.id = :idReceta",
                ValoracionUsuario.class);
        query.setParameter("usuario", usuario);
        query.setParameter("idReceta", idReceta);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null; // Si no se encuentra ninguna valoración para el usuario y la receta, devolver null
        }
    }

    public List<Receta> buscarPorIngredientes(String filtroIngredientes,int inicio, int cantidad) {
        String jpql = "SELECT DISTINCT r FROM Receta r " +
                "JOIN r.ingredientes i " +
                "WHERE LOWER(i.nombre) LIKE :filtroIngredientes";
        TypedQuery<Receta> query = em.createQuery(jpql, Receta.class)
                .setFirstResult(inicio) // Establece el primer resultado del rango
                .setMaxResults(cantidad); // Establece la cantidad máxima de resultados a obtener
        query.setParameter("filtroIngredientes", "%" + filtroIngredientes.toLowerCase() + "%");
        return query.getResultList();
    }


    public List<Receta> findTop3Recetas() {
        TypedQuery<Receta> query = em.createQuery("SELECT r FROM Receta r ORDER BY r.valoracion DESC", Receta.class)
                .setMaxResults(3); // Obtener solo las 3 mejores recetas
        return query.getResultList();
    }

    public void eliminarReceta(Long idReceta) {
        try {
            Receta receta = em.find(Receta.class, idReceta);
            if (receta != null) {
                System.out.println("Receta a eliminar con id " + idReceta);
                em.remove(receta);
            } else {
                System.out.println("No se encontró la receta con id " + idReceta + " en la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarValoracionDeUsuario(Long idReceta) {
        // Eliminar los pasos asociados a la receta
        Query query = em.createQuery("DELETE FROM ValoracionUsuario vu WHERE vu.receta.id = :idReceta");
        query.setParameter("idReceta", idReceta);
        query.executeUpdate();
    }



    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return null;
    }
}
