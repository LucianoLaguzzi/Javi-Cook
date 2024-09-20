package controller;

import dao.IngredienteDAO;
import datamodel.GenericDataModel;
import model.Ingrediente;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="ingredienteBacking")
@SessionScoped
public class IngredienteBacking extends AbstractBacking<Ingrediente>{

    @EJB
    IngredienteDAO ingredienteDAO;

    private GenericDataModel<Ingrediente> dataModel;

    private Ingrediente ingrediente;


    public IngredienteBacking() {
        ingrediente = new Ingrediente();
    }
    @PostConstruct
    @Override
    public void init() {
        newEntity();
//      setEntityDAO(ingredienteDAO);
        setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
        setInactivos(false);
        filtrarInactivos();
    }
    @Override
    public void newEntity() {
        setEntity(new Ingrediente());
    }

    //Logica para el backing de ingredientes:












    //Getters y Setters
    @Override
    public GenericDataModel<Ingrediente> getDataModel() {
        return dataModel;
    }

    @Override
    public void setDataModel(GenericDataModel<Ingrediente> dataModel) {
        this.dataModel = dataModel;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
}
