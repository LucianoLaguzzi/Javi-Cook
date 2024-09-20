package datamodel;


import dao.AbstractEntityDAO;
import model.PersistentEntity;

import javax.faces.model.DataModel;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Datamodel generico
 *
 * @author Diego de la Riva
// * @param <T> Clase especifica
 */
public class GenericDataModel<T> extends DataModel<T> {

    private AbstractEntityDAO entityDAO;
    private Object entity;
    private Map predicados;

    /**
     * Constructor del datamodel
     *
     * @param dao Dao
     * @param entity Entity
     */
    public GenericDataModel(AbstractEntityDAO dao, Object entity) {
        this.entityDAO = dao;
        this.entity = entity;
        this.predicados = null;
    }

    /**
     * Constructor del datamodel
     *
     * @param dao Dao
     * @param entity Entity
     * @param predicados predicados
     */
    public GenericDataModel(AbstractEntityDAO dao, Object entity, Map predicados) {
        this.entityDAO = dao;
        this.entity = entity;
        this.predicados = predicados;
    }

    /**
     * Carga el datamodel con inicio, tamano, ordenacion y filtros
     *
     * @param first inicio
     * @param pageSize cantidad
     * @param sortField orden
     * @param sortOrder ascendente o descentende
     * @param filters filtros
     * @return Lista de Entitys
     */
//    @Override
    public List<T> load(int first, int pageSize, String sortField,
                        SortOrder sortOrder, Map<String, Object> filters) {
        if (filters == null) {
            filters = new HashMap<String, Object>();
        }
        if (getPredicados() != null) {
            filters.putAll(getPredicados());
        }
        //Filtro con like hacia delante......
        if (filters != null) {
            if (filters.containsKey("equipo_id.descripcion")) {
                Object o = filters.get("equipo_id.descripcion");
                String aux = "%" + (String) o;
                filters.replace("equipo_id.descripcion", aux);
            }
            if (filters.containsKey("equipo.descripcion")) {
                Object o = filters.get("equipo.descripcion");
                String aux = "%" + (String) o;
                filters.replace("equipo.descripcion", aux);
            }
        }

        List<T> result = new ArrayList<>();
        try {
            int ordenamiento = 0;
            if (sortOrder != null) {
                if (SortOrder.ASCENDING.equals(sortOrder)) {
                    ordenamiento = 1;
                } else if (SortOrder.DESCENDING.equals(sortOrder)) {
                    ordenamiento = -1;
                }
            }
            int cantidad = 0;
            Map<String, Object> filtros = new HashMap<>();
            filtros.putAll(filters);


            result = getEntityDAO().findAll(getEntity().getClass(), filters, first, pageSize, sortField, ordenamiento);
            cantidad = getEntityDAO().getRecordCount(getEntity().getClass(), filtros).intValue();

            this.setRowCount(cantidad);
        } catch (Exception ex) {
        } finally {
            return result;
        }
    }

    private void setRowCount(int cantidad) {
    }

    /**
     * Retorna el Dao
     *
     * @return Dao
     */
    public AbstractEntityDAO getEntityDAO() {
        return entityDAO;
    }

    /**
     * Modifica el Dao
     *
     * @param dao Dao
     */
    public void setEntityDAO(AbstractEntityDAO dao) {
        this.entityDAO = dao;
    }

    /**
     * Retorna el Entity
     *
     * @return Entity
     */
    public Object getEntity() {
        return entity;
    }

    /**
     * Cambia el Entity
     *
     * @param entity Entity
     */
    public void setEntity(Object entity) {
        this.entity = entity;
    }

    /**
     * Posibilita la selección
     *
     * @param entity
     * @return
     */
//    @Override
    public Object getRowKey(Object entity) {
        PersistentEntity pentity = (PersistentEntity) entity;
        return pentity != null ? pentity.getId() : null;
    }

    /**
     * Posibilita la selección
     *
     * @param rowKey
     * @return
     */
//    @Override
    public T getRowData(String rowKey) {
        List<PersistentEntity> list = (List<PersistentEntity>) getWrappedData();
        for (PersistentEntity pentity : list) {
            if (pentity.getId().toString().equals(rowKey)) {
                return (T) pentity;
            }
        }

        return null;
    }

    /**
     * @return the predicados
     */
    public Map getPredicados() {
        return predicados;
    }

    /**
     * @param predicados the predicados to set
     */
    public void setPredicados(Map predicados) {
        this.predicados = predicados;
    }

    @Override
    public boolean isRowAvailable() {
        return false;
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public T getRowData() {
        return null;
    }

    @Override
    public int getRowIndex() {
        return 0;
    }

    @Override
    public void setRowIndex(int i) {

    }

    @Override
    public Object getWrappedData() {
        return null;
    }

    @Override
    public void setWrappedData(Object o) {

    }
}
