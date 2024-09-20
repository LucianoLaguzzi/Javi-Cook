package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "valoracion_usuario")
@NamedQueries({
        @NamedQuery(name = "ValoracionUsuario.findAllValoracionUsuario", query = "SELECT v FROM ValoracionUsuario v WHERE v.receta.id = :recetaId"),

})

@SequenceGenerator(name = "SEQ_VAL", initialValue = 1, allocationSize = 1)
public class ValoracionUsuario extends AbstractEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VAL")

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_receta")
    private Receta receta;

    @Column(name = "valoracion")
    private int valoracion;


public ValoracionUsuario(){

}

public ValoracionUsuario(Usuario usuario, Receta receta, Integer valoracion){
    this.usuario = usuario;
    this.receta=receta;
    this.valoracion=valoracion;
}


    // Getters y setters
    @Override
    public String toString() {
        return "Valoracion de los usuarios";
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
}