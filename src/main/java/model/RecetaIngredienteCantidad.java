package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "receta_ingredientes_cantidades")
@NamedQueries({
        @NamedQuery(name = "RecetaIngredienteCantidad.findAll", query = "SELECT ric FROM RecetaIngredienteCantidad ric"),
        @NamedQuery(name = "RecetaIngredienteCantidad.findCantidadIngredientes", query = "SELECT ric.ingredienteCantidad FROM RecetaIngredienteCantidad ric WHERE ric.receta_id = :recetaId")

})
@SequenceGenerator(name = "SEQ_RIC", initialValue = 1, allocationSize = 1)
public class RecetaIngredienteCantidad extends AbstractEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RIC")

    @JoinColumn(name = "RECETA_ID")
    private Long receta_id;


    @Column(name = "INGREDIENTE_CANTIDAD")
    private String ingredienteCantidad;




    public RecetaIngredienteCantidad(){

    }




//    GETTER Y SETTER
    public Long getReceta_id() {
        return receta_id;
    }

    public void setReceta_id(Long receta_id) {
        this.receta_id = receta_id;
    }

    public String getIngredienteCantidad() {
        return ingredienteCantidad;
    }

    public void setIngredienteCantidad(String ingredienteCantidad) {
        this.ingredienteCantidad = ingredienteCantidad;
    }

    @Override
    public String toString() {
        return "Cantidades de ingredientes metodo default";
    }
}
