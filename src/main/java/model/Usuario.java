package model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@NamedQueries({
        @NamedQuery(name = "Usuario.findAllActivos", query = "SELECT u FROM Usuario u WHERE u.estado = 'A'"),
        @NamedQuery(name = "Usuario.findAllNamedBy", query = "SELECT u FROM Usuario u WHERE u.estado = 'A'"),
        @NamedQuery(name = "Usuario.findByNombreYContrasenia", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.contrasenia = :contrasenia"),
        @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u LEFT JOIN FETCH u.recetasFavoritas WHERE u.nombre = :nombre"),
        @NamedQuery(name = "Usuario.findByIdWithRecetasFavoritas", query = "SELECT u FROM Usuario u LEFT JOIN FETCH u.recetasFavoritas WHERE u.id = :id"),

})
@SequenceGenerator(name = "SEQ_USU", initialValue = 1, allocationSize = 1)
public class Usuario extends AbstractEntity implements Serializable {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USU")

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CONTRASENIA")
    private String contrasenia;


    private String imagenPerfil;


    @ManyToMany
    @JoinTable(
            name = "usuario_favorito_receta",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "receta_id")
    )
    private List<Receta> recetasFavoritas;



    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "fecha_expiracion_token")
    private LocalDateTime fechaExpiracionToken;





    public Usuario(){
        this.imagenPerfil = "img/default-image.jpg";
        this.recetasFavoritas = new ArrayList<>();
    }

    public Usuario(String nombre, String contrasenia, String email, String imagenPerfil) {
        this.nombre = nombre;
        this.contrasenia=contrasenia;
        this.email=email;
        this.imagenPerfil = imagenPerfil;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }


    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public List<Receta> getRecetasFavoritas() {
        return recetasFavoritas;
    }

    public void setRecetasFavoritas(List<Receta> recetasFavoritas) {
        this.recetasFavoritas = recetasFavoritas;
    }



    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public LocalDateTime getFechaExpiracionToken() {
        return fechaExpiracionToken;
    }

    public void setFechaExpiracionToken(LocalDateTime fechaExpiracionToken) {
        this.fechaExpiracionToken = fechaExpiracionToken;
    }



    @Override
    public String toString() {
        return  getNombre();
    }



}