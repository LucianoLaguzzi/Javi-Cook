package model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pasos_receta")
@NamedQueries({
        @NamedQuery(name = "PasosReceta.findAll", query = "SELECT pr FROM PasosReceta pr"),
        @NamedQuery(name = "PasosReceta.findPasosPorReceta", query = "SELECT pr.pasos FROM PasosReceta pr WHERE pr.receta_id = :recetaId")

})
@SequenceGenerator(name = "SEQ_PAS", initialValue = 1, allocationSize = 1)
public class PasosReceta extends AbstractEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAS")

    @JoinColumn(name = "RECETA_ID")
    private Long receta_id;


    @Column(name = "PASOS")
    private String pasos;


    public PasosReceta(){
    }










    //    GETTER Y SETTER


    public Long getReceta_id() {
        return receta_id;
    }

    public void setReceta_id(Long receta_id) {
        this.receta_id = receta_id;
    }

    public String getPasos() {
        return pasos;
    }

    public void setPasos(String pasos) {
        this.pasos = pasos;
    }

    @Override
    public String toString() {
        return "Pasos de la receta default";
    }
}
