package dao;

import model.PersistentEntity;

import javax.ejb.Stateless;

@Stateless
public class GenericDAO<T> extends AbstractEntityDAO<T>{

    @Override
    public PersistentEntity findById(Long id) throws Exception {
        return this.findById(PersistentEntity.class, id);
    }

}


