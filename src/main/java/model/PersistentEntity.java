package model;


import java.io.Serializable;
public interface PersistentEntity<PK extends Serializable> extends Serializable {
    public boolean canActivate();
    public boolean canDeactivate();
    public boolean canEdit();
//    public boolean canShow();
//    public boolean canNew();
    public boolean canUpdate();
    public boolean canDelete();
//    public boolean canClone();
    public Long getId();
}




