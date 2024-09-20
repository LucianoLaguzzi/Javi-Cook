package converter;

import controller.AbstractBacking;
import dao.AbstractEntityDAO;
import model.PersistentEntity;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public abstract class AbstractEntityConverter implements Converter {
    public abstract String getBackingBeanName();

    private AbstractBacking getBackingBean(FacesContext context) {
        return (AbstractBacking) context.getApplication().getELResolver().getValue(context.getELContext(), null, this.getBackingBeanName());
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            AbstractBacking ab = getBackingBean(context);
            AbstractEntityDAO dao = ab.getEntityDAO();
            Object entity = ab.getEntity();
            return dao.findById(entity.getClass(), Long.valueOf(value));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convierte el objeto en un String de solo el id
     * @param context FacesContext
     * @param component UIComponent
     * @param value Entity
     * @return id del Entity
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            return ((PersistentEntity) value).getId().toString();
        }
        catch (Exception e){
            return "";
        }
    }


}




