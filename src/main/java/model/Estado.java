package model;

import java.io.Serializable;


public abstract class Estado implements Serializable{
    public static final String ESTADO_ACTIVO = "A";
    public static final String ESTADO_INACTIVO = "B";
//    public static final String ESTADO_CARGANDO = "C";
//    public static final String ESTADO_APROBADO = "D";
    public abstract boolean canActivate();
    public abstract boolean canDeactivate();
    public abstract boolean canEdit();
//    public abstract boolean canShow();
    public abstract boolean canUpdate();
    public abstract boolean canDelete();
//    public abstract boolean canClone();

    /**
     * Retorna un estado de acuerdo al string
     * @param estado string
     * @return estado
     */
    public static Estado getEstado(String estado){
        Estado state = null;
        switch (estado) {
            case ESTADO_ACTIVO: state = new EstadoActivo();
                break;
            case ESTADO_INACTIVO: state = new EstadoInactivo();
                break;
        }
        return state;
    }

    /**
     * Retorna el nombre del estado
     * Implementad en las subclases
     * @return string
     */
    public abstract String getNombre();

}

