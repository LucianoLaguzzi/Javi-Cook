package model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * Clase abstracta para generalizacion de entidades persistentes
 *
 * @author edIT
 */
@MappedSuperclass
public abstract class AbstractEntity implements PersistentEntity<Long>{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
//    @Basic(optional = false)
//    @Column(name = "fecha_alta")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date fechaAlta;
    @Column(name = "fecha_baja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Column(name = "fecha_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEstado;
//    @JoinColumn(name = "usuario_alta_id", referencedColumnName = "id")
//    @ManyToOne
//    private Usuario usuarioAlta;
//    @JoinColumn(name = "usuario_baja_id", referencedColumnName = "id")
//    @ManyToOne
//    private Usuario usuarioBaja;
//    @JoinColumn(name = "usuario_estado_id", referencedColumnName = "id")
//    @ManyToOne
//    private Usuario usuarioEstado;

    public AbstractEntity(){
        this.estado = Estado.ESTADO_ACTIVO;
//        this.fechaAlta = new Date();
    }

    /**
     * Retorna el id del Entity
     *
     * @return Long
     */
    @Override
    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Retorna el hash del Entity
     *
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash = hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }


    /**
     * Compara Entitys por el id
     *
     * @param obj otro Entity
     * @return true si tienen el mismo id
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractEntity)) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) obj;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if (this.getId() == null && other.getId() == null){
            return super.equals(obj);
        }
        return true;
    }

    /**
     * Rescribe como abstract el toString
     *
     * @return String
     */
    @Override
    public abstract String toString();


    /**
     * Retorna si puede activarse el entity
     *
     * @return
     */
    @Override
        public boolean canActivate() {
        return this.getEstado().canActivate();
    }


    /**
     * Retrona si puede ser desactivada
     *
     * @return booleano
     */
    @Override
    public boolean canDeactivate() {
        return this.getEstado().canDeactivate();
    }


    /**
     * Retorna si puede ser editada
     *
     * @return booleano
     */
    @Override
    public boolean canEdit() {
        return this.getEstado().canEdit();
    }

    /**
     * Retorna si puede ser mostrada
     *
     * @return booleano
     */
//    @Override
//    public boolean canShow() {
//        return this.getEstado().canShow();
//    }


    /**
     * Retorna si puede ser actualizada
     *
     * @return booleano
     */
    @Override
    public boolean canUpdate() {
        return this.getEstado().canUpdate();
    }


    /**
     * Retorna si puede ser eliminada
     *
     * @return booleano
     */
    @Override
    public boolean canDelete() {
        return this.getEstado().canDelete();
    }

    /**
     * Retorna si puede ser clonada
     *
     * @return booleano
     */
//    @Override
//    public boolean canClone() {
//        return this.getEstado().canClone();
//    }


    /**
     * Retorna el estado del entity
     *
     * @return estado
     */

    public Estado getEstado() {
        return Estado.getEstado(estado);
    }



    public boolean canCreate() {
        return true;
    }

//    @Override
//    public boolean canNew() {
//        return true;
//    }


//    /**
//     * Cambia el estado del entity
//     *
//     * @param estado estado
//     */


    public void setEstado(Estado estado) {
        setEstado(estado.getNombre());
    }


//    /**
//     * Cambia el estado del entity
//     *
//     * @param estado estado
//     */
    public void setEstado(String estado) {
        this.estado = estado;
    }


//    public Date getFechaAlta() {
//        return fechaAlta;
//    }
//
//
//    public void setFechaAlta(Date fechaAlta) {
//        this.fechaAlta = fechaAlta;
//    }


    public Date getFechaBaja() {
        return fechaBaja;
    }


    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }


    public Date getFechaEstado() {
        return fechaEstado;
    }


    public void setFechaEstado(Date fechaEstado) {
        this.fechaEstado = fechaEstado;
    }


//    public Usuario getUsuarioAlta() {
//        return usuarioAlta;
//    }
//
//
//    public void setUsuarioAlta(Usuario usuarioAlta) {
//        this.usuarioAlta = usuarioAlta;
//    }
//
//
//    public Usuario getUsuarioBaja() {
//        return usuarioBaja;
//    }
//
//
//    public void setUsuarioBaja(Usuario usuarioBaja) {
//        this.usuarioBaja = usuarioBaja;
//    }
//
//
//    public Usuario getUsuarioEstado() {
//        return usuarioEstado;
//    }
//
//
//    public void setUsuarioEstado(Usuario usuarioEstado) {
//        this.usuarioEstado = usuarioEstado;
//    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("Bundle");
    }

    public ResourceBundle getCoreBundle() {
        return ResourceBundle.getBundle("META-INF/CoreBundle");
    }
}

