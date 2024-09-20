package model;


import java.io.Serializable;


public class EstadoActivo extends Estado implements Serializable{

    @Override
    public boolean canEdit(){
        return true;
    }

    @Override
    public boolean canUpdate(){
        return true;
    }

    @Override
    public boolean canDelete(){
        return false;
    }

    @Override
    public boolean canActivate() {
        return false;
    }

    @Override
    public boolean canDeactivate() {
        return true;
    }

//    @Override
//    public boolean canClone() {
//        return true;
//    }

    @Override
    public String getNombre() {
        return Estado.ESTADO_ACTIVO;
    }

    @Override
    public String toString(){
        return Estado.ESTADO_ACTIVO;
    }
//
//    @Override
//    public boolean canShow() {
//        return true;
//    }
}
