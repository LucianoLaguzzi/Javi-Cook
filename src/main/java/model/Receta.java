package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "receta")
@NamedQueries({
        @NamedQuery(name = "Receta.findAll", query = "SELECT r FROM Receta r"),

})
@SequenceGenerator(name = "SEQ_REC", initialValue = 1, allocationSize = 1)
public class Receta extends AbstractEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REC")

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;


    @Column(name = "TITULO")
    private String titulo;

    @ElementCollection
    @CollectionTable(name = "pasos_receta", joinColumns = @JoinColumn(name = "RECETA_ID"))
    @Column(name = "PASOS")
    private List<String> pasos;

    @Column(name = "IMAGEN")
    private String imagen;


    @Column(name = "FECHA")
    private String fecha;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    private Usuario usuario;

    @Column(name = "CATEGORIA")
    private String categoria;

    @ManyToMany
    @JoinTable(
            name = "receta_ingrediente",
            joinColumns = @JoinColumn(name = "id_receta"),
            inverseJoinColumns = @JoinColumn(name = "id_ingrediente")
    )
    private List<Ingrediente> ingredientes;

    @Column(name = "VALORACION")
    private int valoracion;

    @Column(name = "DIFICULTAD")
    private String dificultad;

    @Column(name = "TIEMPO_PREPARACION")
    private String tiempo_preparacion;

    @ElementCollection
    @CollectionTable(name="receta_ingredientes_cantidades", joinColumns = @JoinColumn(name = "RECETA_ID"))
    @Column(name="INGREDIENTE_CANTIDAD")
    private List<String> ingredientesCantidades;

    @ManyToMany(mappedBy = "recetasFavoritas")
    private List<Usuario> usuariosFavoritos;




    public Receta() {
        pasos = new ArrayList<>();
        ingredientes = new ArrayList<>();
        ingredientesCantidades = new ArrayList<>();

    }



    public Receta(String titulo,List<String> ingredientesCantidades,Usuario usuario, List<String> pasos, String path, String fechaYHora,String categoria,String dificultad, String tiempo_preparacion) {
        this.titulo = titulo;
        this.usuario = usuario;
        this.pasos = pasos;
        this.imagen = path;
        System.out.println(path);
        this.fecha = fechaYHora;
        this.categoria = categoria;
        this.dificultad = dificultad;
        this.ingredientes = new ArrayList<>();
        this.tiempo_preparacion = tiempo_preparacion;
        this.ingredientesCantidades=ingredientesCantidades;

        this.setEstado(new EstadoActivo());

    }



//Getter y Setter
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public List<String> getPasos() {
        return pasos;
    }

    public void setPasos(List<String> pasos) {
        this.pasos = pasos;
    }

    public String getImagen() {
        // Ruta completa de la imagen
        String rutaCompleta = imagen;
        // Parte de la ruta que deseas mantener
        String parteDeseada = "img/fotos/";
        // Obtener la posición de la parte deseada en la ruta completa
        int indiceInicio = rutaCompleta.indexOf(parteDeseada);
        // Verificar si la parte deseada se encuentra en la ruta completa
        if (indiceInicio != -1) {
            // Devolver la parte relativa de la ruta
            return rutaCompleta.substring(indiceInicio);
        } else {
            // Si la parte deseada no se encuentra, devolver la ruta completa sin cambios
            return imagen;
        }
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getTiempo_preparacion() {
        return tiempo_preparacion;
    }

    public void setTiempo_preparacion(String tiempo_preparacion) {
        this.tiempo_preparacion = tiempo_preparacion;
    }

    public List<String> getIngredientesCantidades() {
        return ingredientesCantidades;
    }
    public void setIngredientesCantidades(List<String> ingredientesCantidades) {
        this.ingredientesCantidades = ingredientesCantidades;
    }


    public List<Usuario> getUsuariosFavoritos() {
        return usuariosFavoritos;
    }

    public void setUsuariosFavoritos(List<Usuario> usuariosFavoritos) {
        this.usuariosFavoritos = usuariosFavoritos;
    }

    @Override
    public String toString() {
        return "Receta: " + getTitulo();
    }
}
