package controller;

import dao.RecetaDAO;
import datamodel.GenericDataModel;
import model.Receta;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class PasosRecetaBacking extends AbstractBacking<Receta> {

    @EJB
    RecetaDAO recetaDAO;

    private Receta receta;

    private List<String> pasosReceta = new ArrayList<>();

    private String nuevoPaso;

    public PasosRecetaBacking() {
        receta = new Receta();
    }


    @PostConstruct
    @Override
    public void init() {
        newEntity();
        setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
        setInactivos(false);
        filtrarInactivos();
        // Obtener el ID de la receta de los parámetros de la solicitud
        FacesContext context = FacesContext.getCurrentInstance();
        String idRecetaString = context.getExternalContext().getRequestParameterMap().get("idReceta");
        if (idRecetaString != null) {
            long idReceta = Long.parseLong(idRecetaString);
            try {
                receta = recetaDAO.findById(Receta.class, idReceta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public List<String> obtenerPasosReceta(Long idReceta) {
        try {
            pasosReceta = recetaDAO.findPasosPorReceta(idReceta);
            if (!pasosReceta.isEmpty()){
                return pasosReceta;
            }else{
                List<String> listaVacia = new ArrayList<>();
                listaVacia.add("No hay pasos");
                return listaVacia;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


    public String formatPasos(String pasos) {
        String[] pasosArray = pasos.split("\\r?\\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pasosArray.length; i++) {
            // Capitalizar la primera letra del paso
            String paso = capitalizeFirstLetter(pasosArray[i]);
            // Agregar el número de paso dentro de un div con una clase CSS
            builder.append("<div class=\"paso-item\"><div class=\"numero-paso\">").append((i + 1)).append("</div>").append("<div class=\"texto-paso\">").append(paso).append("</div></div>");
        }
        return builder.toString();
    }


    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }



    @Override
    public void newEntity() {
        setEntity(new Receta());
    }

// Getter y Setter para receta


    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public List<String> getPasosReceta() {
        return pasosReceta;
    }

    public void setPasosReceta(List<String> pasosReceta) {
        this.pasosReceta = pasosReceta;
    }

    public String getNuevoPaso() {
        return nuevoPaso;
    }

    public void setNuevoPaso(String nuevoPaso) {
        this.nuevoPaso = nuevoPaso;
    }



}
