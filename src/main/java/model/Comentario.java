package model;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "comentario")
@NamedQueries({
        @NamedQuery(name = "Comentario.findAll", query = "SELECT c FROM Comentario c"),
        @NamedQuery(name = "Comentario.findFechaById", query = "SELECT c.fecha FROM Comentario c WHERE c.receta.id = :recetaId")

})
@SequenceGenerator(name = "SEQ_COM", initialValue = 1, allocationSize = 1)
public class Comentario extends AbstractEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COM")

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "receta_id")
    private Receta receta;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "FECHA")
    private String fecha;



    public Comentario() {

    }

    public Comentario(Usuario usuario, Receta receta, String comentario){
        this.usuario = usuario;
        this.receta = receta;
        this.comentario = comentario;
        this.fecha =getFecha();
    }









//    Getter y Setter
    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha() {
        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatoArgentino = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("es", "AR"));
        String fechaYHora = fechaActual.format(formatoArgentino);
        return fechaYHora;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return null;
    }
}
