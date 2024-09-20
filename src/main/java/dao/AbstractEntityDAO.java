package dao;


import model.AbstractEntity;
import model.EstadoActivo;
import model.EstadoInactivo;
import model.PersistentEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.sql.Connection;
import java.util.*;

public abstract class AbstractEntityDAO<T> implements Serializable {

    @PersistenceContext(unitName = "javicookBD")
    protected EntityManager em;


    public void create(final Object entity) throws Exception {
        beforeCreate((AbstractEntity) entity);
        em.persist(entity);
        afterCreate((AbstractEntity) entity);
    }


    public void update(final Object entity) throws Exception {
        beforeUpdate((AbstractEntity) entity);
        em.merge(entity);
        afterUpdate((AbstractEntity) entity);
    }

    /**
     * Elimina un Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    public void destroy(final Object entity) throws Exception {
        beforeDestroy((AbstractEntity) entity);
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        afterDestroy((AbstractEntity) entity);
    }

    /**
     * Desliga un Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    public void detach(final Object entity) throws Exception {
        beforeDetach((AbstractEntity) entity);
        em.detach(entity);
        afterDetach((AbstractEntity) entity);
    }

    /**
     * Retorna true si es un entity nuevo
     *
     * @param entity Entity
     * @return boolean
     */
    public boolean isNew(PersistentEntity entity) {
        return (entity == null) || (entity.getId() == null) || (entity.getId() == 0);
    }

    /**
     * Activa un Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    public void activate(final AbstractEntity entity) throws Exception {
        entity.setEstado(new EstadoActivo());
        entity.setFechaBaja(null);
//        entity.setUsuarioBaja(null);
        update(entity);
    }

    /**
     * Desactiva un Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    public void deactivate(final AbstractEntity entity) throws Exception {
        entity.setEstado(new EstadoInactivo());
        entity.setFechaBaja(new Date());
//        entity.setUsuarioBaja(usuario);
        update(entity);
    }

    /**
     * Retorna un entity a partir del id. Implementado en las concretas
     *
     * @param id id del entity
     * @return entity
     * @throws Exception
     */
    public abstract PersistentEntity findById(Long id) throws Exception;

    /**
     * Retorna un Entity a partir de la clase y del id
     *
     * @param <T>   clase del Entity
     * @param clase clase del Entity
     * @param id    id del Entity
     * @return Entity a partir de la clase y del id
     * @throws Exception
     */
    public <T> T findById(final Class<T> clase, final Long id) throws Exception {
        return em.find(clase, id);
    }

    /**
     * Verifica si existe un Entity a partir de un id o campo=valor Si el id es
     * 0 o null busca por campo=valor
     *
     * @param clase clase del Entity
     * @param id    id del Entity
     * @param campo campo a buscar
     * @param valor valor a buscar
     * @return true o false
     */
    public boolean exist(final Class<T> clase, Long id, String campo, String valor) {
        boolean exist = true;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clase);
        From<T, ?> root = cq.from(clase);
        while (campo.contains(".")) { //si hay que cruzar con mas clases
            String clasejoinX = campo.substring(0, campo.indexOf("."));
            campo = campo.substring(campo.indexOf(".") + 1);
            root = root.join(clasejoinX);
        }
        if (id == null || id == 0) {
            cq.where(cb.equal(root.<String>get(campo), valor));
        } else {
            cq.where(cb.and(cb.equal(root.<String>get(campo), valor), cb.notEqual(root.get("id"), id)));
        }
        try {
            em.createQuery(cq).getSingleResult();
        } catch (NoResultException nre) {
            exist = false;
        } catch (Exception e) {
        }
        return exist;
    }

    /**
     * Retorna todos los Entity de una clase
     *
     * @param clase clase del Entity
     * @return
     */
    public List<T> findAll(Class<T> clase) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        cq.select(cq.from(clase));
        return em.createQuery(cq).getResultList();
    }

    /**
     * Retorna todos los Entity de una clase a partir de un namedquery
     *
     * @param query nombre del namedquery
     * @return Lista de Entitys
     */
    public List<T> findAllNamedBy(String query) {
        return em.createNamedQuery(query).getResultList();
    }

    /**
     * Retorna una lista de entitys a partir de un namedquery con varios
     * parámetros
     *
     * @param namedQuery Nombre del query
     * @param predicados
     * @return AbstractEntity
     */
    public List<T> findAllMapNamedBy(String namedQuery, Map predicados) {
        Query q = em.createNamedQuery(namedQuery);
        Iterator claves = predicados.keySet().iterator();
        while (claves.hasNext()) {
            String clave = claves.next().toString();
            Object valor = predicados.get(clave);
            q.setParameter(clave, valor);
        }
        return q.getResultList();
    }
//
//    /**
//     * Retorna todos los Entity de una clase a partir de un namedquery con
//     * parametros
//     *
//     * @param query nombre del namedquery
//     * @param parameter parametro del query
//     * @param value valor del parametro
//     * @return Lista de Entitys
//     */
//    public List<T> findAllNamedBy(String query, String parameter, String value) {
//        return em.createNamedQuery(query)
//                .setParameter(parameter, value)
//                .getResultList();
//    }
//
//    /**
//     * Retorna todos los Entity de una clase a partir de un namedquery con
//     * parametros
//     *
//     * @param query nombre del namedquery
//     * @param parameter parametro del query
//     * @param value valor del parametro
//     * @return Lista de Entitys
//     */
//    public List<T> findAllNamedBy(String query, String parameter, Long value) {
//        return em.createNamedQuery(query)
//                .setParameter(parameter, value)
//                .getResultList();
//    }
//
//    /**
//     * Retorna un Entity a partir de un namedquery
//     *
//     * @param query nombre del namedquery
//     * @param parameter parametro del query
//     * @param value valor del parametro
//     * @return Entity
//     */
//    public Object findNamedBy(String query, String parameter, String value) {
//        try {
//            return em.createNamedQuery(query)
//                    .setParameter(parameter, value)
//                    .getSingleResult();
//        } catch (NoResultException nre) {
//            return null;
//        }
//    }
//
//    public Object findNamedBy(String query, String parameter, int value) {
//        try {
//            return em.createNamedQuery(query)
//                    .setParameter(parameter, value)
//                    .getSingleResult();
//        } catch (NoResultException nre) {
//            return null;
//        }
//    }

    /**
     * Retorna un Entity a partir de un namedquery
     *
     * @param query     nombre del namedquery
     * @param parameter parametro del query
     * @param value     valor del parametro
     * @return Entity
     */
    public Object findNamedBy(String query, String parameter, Long value) {
        try {
            return em.createNamedQuery(query)
                    .setParameter(parameter, value)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


//    /**
//     * Retorna un Entity a partir de un namedquery y un id
//     *
//     * @param query nombre del namedquery
//     * @param id id del Entity
//     * @return Entity
//     */
//    public Object findNamedBy(String query, Long id) {
//        return em.createNamedQuery(query)
//                .setParameter("id", id)
//                .getSingleResult();
//    }
//
//    /**
//     * Retorna un Entity a partir de un namedquery y un Entity
//     *
//     * @param query nombre del namedquery
//     * @param pe Entity
//     * @return Entity
//     */
//    public Object findNamedBy(String query, PersistentEntity pe) {
//        try{
//            return em.createNamedQuery(query)
//                .setParameter("entity", pe)
//                .getSingleResult();
//        }
//        catch (NoResultException nre){
//            return null;
//        }
//    }

    /**
     * Retorna un objeto a partir de un query sin parametros
     *
     * @param query
     * @return
     */
    public T findNamedBy(String query) {
        try {
            Query nq = em.createNamedQuery(query);
            return (T) nq.setMaxResults(1).getResultList().get(0);
        } catch (NoResultException nre) {
            return null;
        }
    }


    /**
     * Retorna entities entre un rango de fechas
     *
     * @param query
     * @param desde
     * @param hasta
     * @return
     */
    public List<T> findBetween(String query, Date desde, Date hasta) {
        return em.createNamedQuery(query)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList();
    }

    /**
     * Retorna entities con filtro de predicados, offset y limit
     *
     * @param query
     * @param predicados
     * @param offset
     * @param limit
     * @return
     */
    public List<T> findAllNamedBy(String query, Map predicados, int offset, int limit) {
        Query q = em.createNamedQuery(query);
        Iterator claves = predicados.keySet().iterator();
        while (claves.hasNext()) {
            String clave = claves.next().toString();
            String valor = predicados.get(clave).toString();
            q.setParameter(clave, valor);
        }
        return q.setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    /**
     * Retorna entities con offset y limit
     *
     * @param namedQuery
     * @param offset
     * @param limit
     * @return
     */
    public List<T> findAllNamedBy(String namedQuery, int offset, int limit) {
        Query q = em.createNamedQuery(namedQuery);
        return q.setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    /**
     * Retorna todos los Entitys de una clase a partir de un namedquery, un
     * Entity y un parametro
     *
     * @param query      query nativo
     * @param predicados
     * @param c
     * @return Lista de Entitys
     */
    public List findAllByNativeQuery(String query, Map predicados, final Class<T> c) {
        Query q = em.createNativeQuery(query, c);
        Iterator claves = predicados.keySet().iterator();
        while (claves.hasNext()) {
            String clave = claves.next().toString();
            String valor = predicados.get(clave).toString();
            q.setParameter(clave, valor);
        }
        return (List<T>) q.getResultList();
    }

    public List findAllByNativeQuery(String query, Object... params) {
        Query nq = em.createNativeQuery(query);
        int i = 1;
        for (Object value : params) {
            nq.setParameter("param" + i, value);
            i++;
        }
        return (List<T>) nq.getResultList();
    }

    private Predicate getPredicate(From<T, ?> path, CriteriaBuilder cb, String clave, String valor, boolean negativo) {
        if (negativo) {
            return cb.notLike(cb.upper(path.<String>get(clave)), "%" + valor.toUpperCase() + "%");
        } else {
            if(valor == null){
                return cb.isNull(cb.upper(path.<String>get(clave)));
            }
            return cb.like(cb.upper(path.<String>get(clave)), "%" + valor.toUpperCase() + "%");
        }
    }

    private Predicate getPredicate(From<T, ?> path, CriteriaBuilder cb, String clave, Double valor, boolean negativo) {
        if (negativo) {
            return cb.notEqual(path.<Number>get(clave), valor);
        } else {
            return cb.equal(path.<Number>get(clave), valor);
        }
    }

    private Predicate getPredicate(From<T, ?> path, CriteriaBuilder cb, String clave, Long valor, boolean negativo) {
        if (negativo) {
            return cb.notEqual(path.<Long>get(clave), valor);
        } else {
            return cb.equal(path.<Long>get(clave), valor);
        }
    }

    private Predicate getPredicateNull(From<T, ?> path, CriteriaBuilder cb, String clave, boolean negativo) {
        if (negativo) {
            return cb.isNull(path.<Object>get(clave));
        } else {
            return cb.isNull(path.<Object>get(clave));
        }
    }

    protected Predicate parsePredicate(From<T, ?> path, CriteriaBuilder cb, String clave, Object valor) throws Exception {
        Class<?> tipo = path.get(clave).getJavaType();
        boolean negativo = (valor != null && !valor.toString().isEmpty() && valor.toString().charAt(0) == '!');
        String dato = null;
        if (valor != null) {
            dato = negativo ? (String) valor.toString().substring(1) : (String) valor.toString();
        }

        Predicate nuevopredicado = getPredicate(path, cb, clave, dato, negativo);
        if (valor instanceof String[]) {
            Expression<String> exp = path.<String>get(clave);
            List<String> list = Arrays.asList((String[]) valor);
            nuevopredicado = exp.in(list);
        } else {
            if (valor instanceof Collection<?>) {
                Expression<Long> exp = path.<Long>get(clave).as(Long.class);
                nuevopredicado = exp.in(((Collection<?>) valor).toArray());
            } else {
                if (Number.class.isAssignableFrom(tipo)) {
                    if (dato != null) {
                        nuevopredicado = getPredicate(path, cb, clave, Double.valueOf(dato), negativo);
                    }
                }
                if (tipo.getCanonicalName().equals("java.lang.Long")) {
                    if (dato == null) {
                        nuevopredicado = getPredicateNull(path, cb, clave, negativo);
                    } else {
                        nuevopredicado = getPredicate(path, cb, clave, Long.valueOf(dato), negativo);
                    }

                }
                if (tipo.getCanonicalName().equals("java.util.Date")) {
                    Date fecha = util.Util.parseDateWithEx(dato);
                    Calendar c = Calendar.getInstance();
                    c.setTime(fecha);
                    c.set(Calendar.HOUR, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    Date desde = c.getTime();
                    c.set(Calendar.HOUR, 23);
                    c.set(Calendar.MINUTE, 59);
                    c.set(Calendar.SECOND, 59);
                    Date hasta = c.getTime();
                    nuevopredicado = cb.between(path.<Date>get(clave), desde, hasta);
                }
                if (tipo.getCanonicalName().toLowerCase().contains("boolean")) {
                    Boolean bool = Boolean.valueOf(valor.toString());
                    if (bool) {
                        nuevopredicado = cb.isTrue(path.<Boolean>get(clave));
                    } else {
                        nuevopredicado = cb.isFalse(path.<Boolean>get(clave));
                    }
                }

            }
        }
        return nuevopredicado;
    }

    public List<T> findAllGlobal(Class<T> clase, Object value, int offset, int limit, String orderField, int ordenamiento) {
        Query q = em.createNamedQuery(clase.getSimpleName() + ".findAllComplete");
        return q.setParameter("data", value).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    public Long getRecordCountGlobal(Class<T> clase, Object value) {
        Query q = em.createNamedQuery(clase.getSimpleName() + ".findAllCompleteCount");
        return (Long) q.setParameter("data", value).getSingleResult();
    }

    /**
     * Retorna todos los Entitys de una clase con filtros, offsets y limites
     *
     * @param clase        clase de Entity
     * @param predicados   filtros a aplicar
     * @param offset       desde donde
     * @param limit        cuantos
     * @param orderField   campo por el que ordena
     * @param ordenamiento ascendente o descendente
     * @return Lista de Entitys
     */
    public List<T> findAll(Class<T> clase, Map predicados, int offset, int limit, String orderField, int ordenamiento) {

        //filtros por fecha
        String campoFechaMayor = (String) predicados.remove("campoFechaMayor");
        String campoFechaMenor = (String) predicados.remove("campoFechaMenor");
        Date valorFechaMayor = (Date) predicados.remove("valorFechaMayor");
        Date valorFechaMenor = (Date) predicados.remove("valorFechaMenor");

        //filtros fecha between
        String campoFechaMayorConsulta = (String) predicados.remove("campoFechaMayorConsulta");
        String campoFechaMenorConsulta = (String) predicados.remove("campoFechaMenorConsulta");
        Date valorFechaConsulta = (Date) predicados.remove("valorFechaConsulta");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clase);
        Root<T> path = cq.from(clase);
        cq.select(path);
        From<T, ?> ordenapor = path; //join o path por el que ordena
        //si el orden no viene como filtro, lo agrega para que haga el join
        if ((orderField != null) && (predicados.get(orderField) == null)) {
            predicados.put(orderField, "");
        }
        Iterator<String> claves = predicados.keySet().iterator();
        Predicate predicate = cb.conjunction();
        if (campoFechaMayor != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(path.<Date>get(campoFechaMayor), util.Util.nvl(valorFechaMayor, new Date())));
        }
        if (campoFechaMenor != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(path.<Date>get(campoFechaMenor), util.Util.nvl(valorFechaMenor, new Date())));
        }
        if (valorFechaConsulta != null) {
            predicate = cb.between(cb.literal(valorFechaConsulta), path.<Date>get(campoFechaMenorConsulta), path.<Date>get(campoFechaMayorConsulta));
        }
        while (claves.hasNext()) { //por cada clave
            String clave = claves.next();
            Object valor = predicados.get(clave);
            if (!clave.contains(".")) { //si no tiene relaciones es como antes
                try {
                    Predicate nuevopredicado = parsePredicate(path, cb, clave, valor);
                    predicate = cb.and(predicate, nuevopredicado);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            } else { //si tiene relaciones, arma un join por cada relación
                From<T, ?> join = path; //path base desde donde arranca a cruzar clases
                String campobusqueda = clave.substring(clave.lastIndexOf(".") + 1); //campo de búsqueda
                String clasejoin = clave.substring(0, clave.lastIndexOf(".")); //clase a cruzar
                while (clasejoin.contains(".")) { //si hay que cruzar con mas clases
                    String clasejoinX = clasejoin.substring(0, clasejoin.indexOf("."));
                    clasejoin = clasejoin.substring(clasejoin.indexOf(".") + 1);
                    join = join.join(clasejoinX);
                }
                join = join.join(clasejoin); //agrega el último
                //si es la clave por la que se ordena se queda con el join y con el campo
                if (clave.equals(orderField)) {
                    ordenapor = join;
                    orderField = orderField.substring(orderField.lastIndexOf(".") + 1);
                }
                //finalmente agrega el predicado al join correspondiente
                try {
                    Predicate nuevopredicado = parsePredicate(join, cb, campobusqueda, valor);
                    predicate = cb.and(predicate, nuevopredicado);
                } catch (Exception ex) {
                }
                //   predicate = cb.and(predicate, cb.like(join.<String>get(campobusqueda), "%" + valor + "%"));
            }
        }
        cq.where(predicate); //setea los predicados
        //ordena
        if (ordenamiento != 0 && orderField != null) {
            if (ordenamiento > 0) {
                cq.orderBy(cb.asc(ordenapor.get(orderField)));
            } else {
                cq.orderBy(cb.desc(ordenapor.get(orderField)));
            }
        }
        if (campoFechaMayor != null) {
            predicados.put("campoFechaMayor", campoFechaMayor);
            predicados.put("valorFechaMayor", valorFechaMayor);
        }
        if (campoFechaMenor != null) {
            predicados.put("campoFechaMenor", campoFechaMenor);
            predicados.put("valorFechaMenor", valorFechaMenor);
        }
        if (valorFechaConsulta != null) {
            predicados.put("campoFechaMayorConsulta", campoFechaMayorConsulta);
            predicados.put("campoFechaMenorConsulta", campoFechaMenorConsulta);
            predicados.put("valorFechaConsulta", valorFechaConsulta);
        }
        return em.createQuery(cq).setFirstResult(offset).setMaxResults(limit).getResultList();

    }

    /**
     * Retorna todos los Entitys de una clase con filtros, offsets y limites
     *
     * @param clase         clase de Entity
     * @param predicados    filtros a aplicar
     * @param offset        desde donde
     * @param limit         cuantos
     * @param orderField    campo por el que ordena
     * @param ordenamiento  ascendente o descendente
     * @param nombreEmpresa empresa
     * @return Lista de Entitys
     */
    public List<T> findAll(Class<T> clase, Map predicados, int offset, int limit, String orderField, int ordenamiento, String nombreEmpresa) {
        predicados.put("empresa", nombreEmpresa);
        return findAll(clase, predicados, offset, limit, orderField, ordenamiento);
    }

    /**
     * Retorna la cantidad de registros que cumplen con la condicion
     *
     * @param clase      clase del Entity
     * @param predicados filtros
     * @return Long
     */
    public Long getRecordCount(Class<T> clase, Map predicados) {
        String campoFechaMayor = (String) predicados.remove("campoFechaMayor");
        String campoFechaMenor = (String) predicados.remove("campoFechaMenor");
        Date valorFechaMayor = (Date) predicados.remove("valorFechaMayor");
        Date valorFechaMenor = (Date) predicados.remove("valorFechaMenor");

        String campoFechaMayorConsulta = (String) predicados.remove("campoFechaMayorConsulta");
        String campoFechaMenorConsulta = (String) predicados.remove("campoFechaMenorConsulta");
        Date valorFechaConsulta = (Date) predicados.remove("valorFechaConsulta");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> path = cq.from(clase);
        cq.select(cb.count(path));
        Predicate predicate = cb.conjunction();
        Iterator claves = predicados.keySet().iterator();
        if (campoFechaMayor != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(path.<Date>get(campoFechaMayor), util.Util.nvl(valorFechaMayor, new Date())));
        }
        if (campoFechaMenor != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(path.<Date>get(campoFechaMenor), util.Util.nvl(valorFechaMenor, new Date())));
        }
        if (valorFechaConsulta != null) {
            predicate = cb.between(cb.literal(valorFechaConsulta), path.<Date>get(campoFechaMenorConsulta), path.<Date>get(campoFechaMayorConsulta));
        }
        while (claves.hasNext()) {
            String clave = claves.next().toString();
            Object valor = predicados.get(clave);
            if (!clave.contains(".")) {
                try {
                    Predicate nuevopredicado = parsePredicate(path, cb, clave, valor);
                    predicate = cb.and(predicate, nuevopredicado);
                } catch (Exception ex) {
                }
            } else {
                String camposub = clave.substring(clave.lastIndexOf(".") + 1);
                String clasesub = clave.substring(0, clave.lastIndexOf("."));
                From<T, ?> otro = path;
                while (clasesub.contains(".")) {
                    String clasex = clasesub.substring(0, clasesub.indexOf("."));
                    clasesub = clasesub.substring(clasesub.indexOf(".") + 1);
                    otro = otro.join(clasex);
                }
                otro = otro.join(clasesub);
                try {
                    Predicate nuevopredicado = parsePredicate(otro, cb, camposub, valor);
                    predicate = cb.and(predicate, nuevopredicado);
                } catch (Exception ex) {
                }
                //    predicate = cb.and(predicate, cb.like(otro.<String>get(camposub), "%" + valor + "%"));
            }
        }
        cq.where(predicate);
        return em.createQuery(cq).getSingleResult();
    }

    /**
     * Retorna la cantidad de registros que cumplen con la condicion
     *
     * @param clase         clase del Entity
     * @param predicados    filtros
     * @param nombreEmpresa empresa
     * @return Long
     */
    public Long getRecordCount(Class<T> clase, Map predicados, String nombreEmpresa) {
        predicados.put("empresa", nombreEmpresa);
        return getRecordCount(clase, predicados);
    }


    protected void beforeCreate(final AbstractEntity entity) throws Exception {
        entity.setFechaEstado(new Date());
//        entity.setFechaAlta(new Date());
        entity.setEstado(new EstadoActivo());
        entity.setFechaBaja(null);
//        entity.setUsuarioAlta(usuario);
    }


    protected void afterCreate(final AbstractEntity entity) throws Exception {
    }

    /**
     * Antes de eliminar el Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    protected void beforeDestroy(final AbstractEntity entity) throws Exception {
    }

    /**
     * Despues de eliminar el Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    protected void afterDestroy(final AbstractEntity entity) throws Exception {
    }

    /**
     * Antes de actualizar el Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    protected void beforeUpdate(final AbstractEntity entity) throws Exception {
//        entity.setUsuarioEstado(usuario);
        entity.setFechaEstado(new Date());
    }

    /**
     * Despues de actualizar el Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    protected void afterUpdate(final AbstractEntity entity) throws Exception {
    }

    /**
     * Antes de deligar el Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    protected void beforeDetach(final AbstractEntity entity) throws Exception {
    }

    /**
     * Despues de desligar el Entity
     *
     * @param entity Entity
     * @throws Exception
     */
    protected void afterDetach(final Object entity) throws Exception {
    }

    public Connection getConnection() {
        return em.unwrap(Connection.class);
    }

    //----------------Nuevos Metodos------------------------
    public List<T> findAllNamedBy(String query, Object... params) {
        return (List<T>) findAllObjectsNamedBy(query, params);
    }

    public List<Object[]> findAllObjectsNamedBy(String query, Object... params) {
        Query nq = em.createNamedQuery(query);
        int i = 1;
        for (Object value : params) {
            nq.setParameter("param" + i, value);
            i++;
        }
        return nq.getResultList();
    }

    public List<Object[]> findAllObjectsNamedBy(String query) {
        Query nq = em.createNamedQuery(query);
        return nq.getResultList();
    }

    public List<Object> findAllObjectNamedBy(String query) {
        Query nq = em.createNamedQuery(query);
        return nq.getResultList();
    }

    public T findNamedBy(String query, Object... params) {
        return (T) findObjectNamedBy(query, params);
    }

    public Object findObjectNamedBy(String query, Object... params) {
        try {
            Query nq = em.createNamedQuery(query);
            int i = 1;
            for (Object value : params) {
                nq.setParameter("param" + i, value);
                i++;
            }
            nq.setMaxResults(1);
            return nq.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<T> findBetween(String query, Date desde, Date hasta, Object... params) {
        return (List<T>) findObjectsBetween(query, desde, hasta, params);
    }

    public List<Object[]> findObjectsBetween(String query, Date desde, Date hasta, Object... params) {
        Query nq = em.createNamedQuery(query);
        nq.setParameter("desde", desde);
        nq.setParameter("hasta", hasta);
        int i = 1;
        for (Object value : params) {
            nq.setParameter("param" + i, value);
            i++;
        }
        return nq.getResultList();
    }

    /**
     * Retorna un objeto a partir de un namedquery
     *
     * @param query
     * @return Object
     */
    public Object findObjectNamedBy(String query) {
        try {
            return em.createNamedQuery(query)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Retorna int namedquery función de agregación count
     *
     * @param query
     * @return
     */
    public long getCountNamedBy(String query) {
        return (long) findObjectNamedBy(query);
    }

    /**
     * Retorna int namedquery con parámetros función de agregación count
     *
     * @param query
     * @param params
     * @return
     */
    public long getCountNamedBy(String query, Object... params) {
        return (long) findObjectNamedBy(query, params);
    }

    /**
     * Retorna un objeto en un rango de fechas
     *
     * @param query
     * @param desde
     * @param hasta
     * @return
     */
    public Object findObjectBetween(String query, Date desde, Date hasta) {
        try {
            return em.createNamedQuery(query)
                    .setParameter("desde", desde)
                    .setParameter("hasta", hasta)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Retorna un object en un rango de fechas con parámetros
     *
     * @param query
     * @param desde
     * @param hasta
     * @param params
     * @return
     */
    public Object findObjectBetween(String query, Date desde, Date hasta, Object... params) {
        try {
            Query nq = em.createNamedQuery(query);
            nq.setParameter("desde", desde);
            nq.setParameter("hasta", hasta);
            int i = 1;
            for (Object value : params) {
                nq.setParameter("param" + i, value);
                i++;
            }
            nq.setMaxResults(1);
            return nq.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    //----------------CONFIGURACIONES-------------------------------------------

    /**
     * Obtiene el valor a partir de la clave
     *
     * @param clave clave a buscar
     * @return valor de la clave
     */
//    public String getConfiguracion(String clave) {
//        Configuracion c = (Configuracion) this.findNamedBy("Configuracion.findByNombre", clave);
//        if (c != null) {
//            return c.getValor();
//        }
//        return null;
//    }

    /**
     * Obtiene el valor desencriptado a partir de la clave
     *
     * @param clave clave a buscar
     * @return valor de la clave
     */
//    public String getConfiguracionUncrypt(String clave) {
//        return util.Util.cript(this.getConfiguracion(clave), "los4+1", -1);
//    }

//    /**
//     * Mofifica el valor a partir de la clave
//     *
//     * @param clave      clave a buscar
//     * @param nuevoValor nuevoValor de la clave
//     * @param usuario
//     * @throws java.lang.Exception
//     */
//    public void setConfiguracion(String clave, String nuevoValor, Usuario usuario) throws Exception {
//        Configuracion c = (Configuracion) this.findNamedBy("Configuracion.findByNombre", clave);
//        c.setValor(nuevoValor);
//        this.update(c, usuario);
//    }

//    /*--------------------DATOS DIGITALES------------------------*/
//    public String getOutputPath() {
//        return this.getConfiguracion("outputPath");
//    }
//
//
//    /*--------------------LDAP------------------------*/
//    public String getLdapBaseDn() {
//        return this.getConfiguracion("LDAP_BASE_DN");
//    }
//
//    public String getLdapBaseDnExternos() {
//        return this.getConfiguracion("LDAP_BASE_DN_EXTERNOS");
//    }
//
//    public String getLdapUser() {
//        return this.getConfiguracion("LDAP_USER");
//    }

//    public String getLdapPasswd() {
//        return this.getConfiguracionUncrypt("LDAP_PASSWD");
//    }

//    public String getLdapUrl() {
//        return this.getConfiguracion("LDAP_URL");
//    }
//
//    public String getLdapPort() {
//        return this.getConfiguracion("LDAP_PORT");
//    }
//
//    public String getLdapFactory() {
//        return this.getConfiguracion("LDAP_FACTORY");
//    }
//
//    public String getLdapAuthentication() {
//        return this.getConfiguracion("LDAP_AUTHENTICATION");
//    }
//
//    public String getLdapFilter() {
//        return this.getConfiguracion("LDAP_FILTER");
//    }
//
//    public String getLdapPruebaUrl() {
//        return this.getConfiguracion("LDAPPRUEBA_URL");
//    }

    //----------------DOMINIOS-------------------------------------------

//    /**
//     * Obtiene el valor a partir de la clave
//     *
//     * @param prefijo
//     * @param valor
//     * @return valor de la clave
//     */
//    public Dominio getDominio(String prefijo, String valor) {
//        Dominio d = (Dominio) this.findNamedBy("Dominio.findByPrefijoValor", prefijo, valor);
//        return d;
//    }
//
//    public List<Dominio> getDominios(String prefijo) {
//        return (List<Dominio>) this.findAllNamedBy("Dominio.findByPrefijo", prefijo);
//    }


}
