package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ingrediente")
@NamedQueries({
        @NamedQuery(name = "Ingrediente.findAllActivos", query = "SELECT i FROM Ingrediente i"),
        @NamedQuery(name = "Ingrediente.findByNombre", query = "SELECT i FROM Ingrediente i WHERE i.nombre = :nombre"),

})

@SequenceGenerator(name = "SEQ_ING", initialValue = 1, allocationSize = 1)
public class Ingrediente extends AbstractEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ING")

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;


    @ManyToMany(mappedBy = "ingredientes")
    private List<Receta> recetas;


    public Ingrediente() {

    }

    public Ingrediente(String nombre){
        this.nombre = nombre;
        this.recetas = new ArrayList<>(); // Inicializa la lista de recetas

    }


    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<Receta> getRecetas() {
        return recetas;
    }
    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    @Override
    public String toString() {
        return "Ingrediente: " + getNombre() ;
    }
}
