package model;


import java.io.Serializable;


public class EstadoInactivo extends Estado implements Serializable{

    @Override
    public boolean canActivate() {
        return true;
    }

    @Override
    public boolean canDeactivate() {
        return false;
    }

    @Override
    public boolean canEdit() {
        return false;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public boolean canDelete() {
        return true;
    }

//    @Override
//    public boolean canClone() {
//        return true;
//    }

    @Override
    public String getNombre() {
        return Estado.ESTADO_INACTIVO;
    }

    @Override
    public String toString(){
        return Estado.ESTADO_INACTIVO;
    }

//    @Override
//    public boolean canShow() {
//        return true;
//    }
}
