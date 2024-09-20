package controller;


//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;

import dao.AbstractEntityDAO;
import datamodel.GenericDataModel;
import model.AbstractEntity;
import model.PersistentEntity;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;



public abstract class AbstractBacking<T> implements Serializable {



//    private UploadedFile file;
    private AbstractEntity entity;
    private GenericDataModel<T> dataModel;
    private AbstractEntityDAO<T> entityDAO;
    private Date desde;
    private Date hasta;
    private boolean verInactivos;
    private String contexto = "index";
    private String contextoAnterior = "index";
    private String from;
    private boolean inactivos = false;
    private List<Boolean> columnasVisibles = Arrays.asList(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
    private int activeTab = 0;


//    protected static Logger logger = LoggerFactory.getLogger(AbstractBacking.class);

    /**
     * Retorna el DAO del Backing
     *
     * @return DAO
     */
    public AbstractEntityDAO<T> getEntityDAO() {
        return entityDAO;
    }

    /**
     * Setea el DAO para el backing
     *
     * @param entityDAO DAO
     */
    public void setEntityDAO(AbstractEntityDAO<T> entityDAO) {
        this.entityDAO = entityDAO;
    }

    /**
     * Retorna el Entity asociado al BackingBean
     *
     * @return Entity asociado al BackingBean
     */
    public AbstractEntity getEntity() {
        return entity;
    }

    /**
     * Cambia el Entity
     *
     * @param entity nuevo Entity
     */
    public void setEntity(AbstractEntity entity) {
        if (entity == null) {
            informarValidacion("entity_no_existe", "entity_no_existe");
        } else {
            this.entity = entity;
        }
    }

    /**
     * Inicializa el Backing. Se invoca despues del constructor
     */
    @PostConstruct
    public abstract void init();

    /**
     * Retorna el DataModel para generacion de vistas
     *
     * @return Iterable del tipo del Entity
     */
    public GenericDataModel<T> getDataModel() {
        return dataModel;
    }

    /**
     * Setea el DataModel
     *
     * @param dataModel DataModel
     */
    public void setDataModel(GenericDataModel<T> dataModel) {
        this.dataModel = dataModel;
        showInactivos();
    }

    /**
     * Retorna todos los entitys activos
     *
     * @return
     */
    public List<T> findAllActivos() {
        String query = entity.getClass().getSimpleName() + ".findAllActivos";
        return getEntityDAO().findAllNamedBy(query);
    }

    /**
     * Retorna todos los entitys activos
     *
     * @return
     */
    public List<T> findAll() {
        String query = entity.getClass().getSimpleName() + ".findAll";
        return getEntityDAO().findAllNamedBy(query);
    }

    /**
     * Búsqueda para autocomplete Puede recibir un query como parametro
     *
     * @param data valor a buscar
     * @return Objetos que cumplen con el criterio de búsqueda
     */
    public List<T> complete(String data) {
        FacesContext context = FacesContext.getCurrentInstance();
        String paramQuery = util.Util.nvl(UIComponent.getCurrentComponent(context).getAttributes().get("query"), "findAllComplete").toString();
        String finalQuery = getEntity().getClass().getSimpleName() + "." + paramQuery;
        PersistentEntity pentity = (PersistentEntity) UIComponent.getCurrentComponent(context).getAttributes().get("entity");
        if (pentity == null) {
            return getEntityDAO().findAllNamedBy(finalQuery, '%' + data.toUpperCase() + '%');
        } else {
            return getEntityDAO().findAllNamedBy(finalQuery, pentity, '%' + data.toUpperCase() + '%');
        }
    }


    /**
     * Retorna el mapa de sesion
     *
     * @return Mapa de sesion
     */
    public Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }

    /**
     * Retorna el mapa de requests
     *
     * @return Mapa de request
     */
    public Map<String, Object> getRequestMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
    }

    /**
     * Retorna el path real de la aplicacion
     *
     * @return Path de la aplicacion
     */
    public static String getPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    }

    /**
     * Retorna todo el log de mensajes
     *
     * @param messages mensajes
     * @return log de mensajes
     */
    protected String logMessage(String... messages) {
        String message = "";
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        message += "IP: " + ipAddress + " ";
        for (String m : messages) {
            message += m;
        }
        return message;
    }

    /**
     * Retorna la página de forward
     *
     * @return página
     */
    public String getForwardPage() {
        String viewId = (String) getSessionMap().get("forwardPage");
        String params = "?faces-redirect=true";
        Map<String, String> mapParams = (Map<String, String>) getSessionMap().get("forwardPageParams");
        if (mapParams != null) {
            for (String paramKey : mapParams.keySet()) {
                String paramValue = mapParams.get(paramKey);
                params += "&" + paramKey + "=" + paramValue;
            }
            return viewId + params;
        } else {
            return viewId;
        }
    }

    /**
     * Realiza la validacion
     *
     * @throws ValidatorException
     */
    public void validar() throws ValidatorException {
    }

    /**
     * Validar debe sobreescribrse en las clases que lo necesiten
     *
     * @param context   FacesContext
     * @param component UIComponent
     * @param value     nombre del ambito
     * @throws ValidatorException
     */
    public void validar(FacesContext context, UIComponent component, Object value) {
    }

    public String mostrarEntity(int id, String name) throws Exception {
        AbstractEntity pentity = (AbstractEntity) getEntityDAO().findById(getEntity().getClass(), Long.valueOf(id));
        setEntity(pentity);
        return "/admin/show?faces-redirect=true&name=" + name + "&id=" + id;
    }

    public String crearEntity() {
        String path = getEntity().getClass().getSimpleName().toLowerCase();
        return "/admin/" + path + "/new";
    }

    public String editarEntity() {
        String path = getEntity().getClass().getSimpleName().toLowerCase();
        return "/admin/" + path + "/edit";
    }

    public void informarInternacionalizado(PersistentEntity entity, String accion, String head, String body, FacesMessage.Severity severity, Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("Bundle");
        String nombre = "";
        try {
            head = bundle.getString(head);
            body = bundle.getString(body);
            accion = bundle.getString(accion);
            if (entity != null) {
                nombre = entity.toString();
            } else {
                nombre = "";
            }

        } catch (Exception e) {
        }
        body = MessageFormat.format(body, args);
        FacesMessage msg = new FacesMessage(severity, accion + " " + nombre, body);
        FacesContext.getCurrentInstance().addMessage(head, msg);
    }

    public void informarInternacionalizado(PersistentEntity entity, String accion, String head, String body, FacesMessage.Severity severity) {
        ResourceBundle bundle = ResourceBundle.getBundle("Bundle");
        String nombre = "";
        try {
            head = bundle.getString(head);
        } catch (Exception e) {
        }
        try {
            body = bundle.getString(body);
        } catch (Exception e) {
        }
        try {
            accion = bundle.getString(accion);
        } catch (Exception e) {
        }
        if (entity != null) {
            nombre = entity.toString();
        } else {
            nombre = "";
        }
        FacesMessage msg = new FacesMessage(severity, accion + " " + nombre, body);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void informarError(PersistentEntity entity, String accion, String head, String body) {
        informarInternacionalizado(entity, accion, head, body, FacesMessage.SEVERITY_ERROR);
    }

    public void informarError(PersistentEntity entity, String accion, String head, String body, Object... args) {
        informarInternacionalizado(entity, accion, head, body, FacesMessage.SEVERITY_ERROR, args);
    }

    public void informarMensaje(PersistentEntity entity, String accion, String head, String body) {
        informarInternacionalizado(entity, accion, head, body, FacesMessage.SEVERITY_INFO);
    }

    public void informarMensaje(PersistentEntity entity, String accion, String head, String body, Object... args) {
        informarInternacionalizado(entity, accion, head, body, FacesMessage.SEVERITY_INFO, args);
    }

    /**
     * Genera la excepcion de validacion
     *
     * @param entity entity
     * @param accion accion
     * @param head   titulo del mensaje
     * @param body   cuerpo del mensaje
     * @throws ValidatorException
     */
    public void informarValidacion(PersistentEntity entity, String accion, String head, String body) throws ValidatorException {
        informarInternacionalizado(entity, accion, head, body, FacesMessage.SEVERITY_WARN);
        throw new ValidatorException(new FacesMessage(head, body));
    }

    /**
     * Genera la excepcion de validacion
     *
     * @param head titulo del mensaje
     * @param body cuerpo del mensaje
     * @throws ValidatorException
     */
    public void informarValidacion(String head, String body) throws ValidatorException {
        informarValidacion(null, "validar", head, body);
    }


    /**
     * Crea un Entity invocando al Dao
     *
     * @return path del index de Entitys
     */
    public String crear() {
        String result = null;
        String nombre = getEntity().toString();
//        logger.info(logMessage("Creando ", nombre));
        try {
            validar();
            getEntityDAO().create(getEntity());
            String path = getEntity().getClass().getSimpleName().toLowerCase();
            result = "/admin/" + path + "/index?faces-redirect=true&name=" + getEntity();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            ;
//            logger.error(logMessage(ex.getMessage()));
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Crear " + nombre, ex.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return result;
    }

//    public void crearModal() {
//        if (crear() != null) {
////            RequestContext.getCurrentInstance().closeDialog(getEntity());
//            PrimeFaces.current().dialog().closeDynamic(getEntity());
//        }
//    }

    /**
     * Crea el entity y devuelve la pantalla de visualización
     *
     * @return pantalla de visualización
     */
    public String crearYMostrar() {
        if (crear() != null) {
            String path = getEntity().getClass().getSimpleName().toLowerCase();
            return "/admin/" + path + "/show?faces-redirect=true&id=" + getEntity().getId();
        } else {
            return null;
        }
    }

//    public void onRowSelect(SelectEvent event) {
//        setEntity((AbstractEntity) event.getObject());
////        RequestContext.getCurrentInstance().closeDialog(getEntity());
//        PrimeFaces.current().dialog().closeDynamic(getEntity());
//    }

//    public void abrirDialog(String dialogo) {
//        Map<String, Object> options = new HashMap<>();
//        options.put("resizable", true);
//        options.put("draggable", true);
//        options.put("modal", true);
//        options.put("width", 640);
//        options.put("height", 600);
//        options.put("contentWidth", "100%");
//        options.put("contentHeight", "100%");
//        options.put("headerElement", "customheader");
////        RequestContext.getCurrentInstance().openDialog(dialogo, options, null);
//        PrimeFaces.current().dialog().openDynamic(dialogo, options, null);
//    }

    public void abrirDialog() {
        Map<String, String> parametros = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String url = parametros.get("url");
        String width = parametros.getOrDefault("width", "1000");
        String height = parametros.getOrDefault("height", "600");
        String resizable = parametros.getOrDefault("resizable", "true");
        String draggable = parametros.getOrDefault("draggable", "true");
        String modal = parametros.getOrDefault("modal", "true");
        String contentWidth = parametros.getOrDefault("contentWidth", "100") + "%";
        String contentHeight = parametros.getOrDefault("contentHeight", "100") + "%";
        String closable = parametros.getOrDefault("closable", "true");
        Map<String, List<String>> params = new HashMap<>();
        for (int i = 1; i <= parametros.size() - 3; i++) {
            String param = "param" + i;
            String value = "value" + i;
            String paramname = parametros.get(param);
            if (paramname != null) {
                List<String> values = new ArrayList<>();
                values.add(parametros.get(value));
                params.put(paramname, values);
            }
        }
        Map<String, Object> options = new HashMap();
        options.put("resizable", "true".equals(resizable));
        options.put("draggable", "true".equals(draggable));
        options.put("modal", "true".equals(modal));
        options.put("width", width);
        options.put("height", height);
        options.put("contentWidth", contentWidth);
        options.put("contentHeight", contentHeight);
        options.put("headerElement", "customheader");
        options.put("closable", "true".equals(closable));
        options.put("responsive", "true");
//        PrimeFaces.current().dialog().openDynamic(url, options, params);
    }

    /**
     * Actualiza los datos del Entity invocando al Dao
     *
     * @return path del index de Entitys
     */
    public String actualizar() {
        String nombre = getEntity().toString();
        String result = null;
//        logger.info(logMessage("Actualizando ", nombre));
        try {
            validar();
            getEntityDAO().update(getEntity());
            String path = getEntity().getClass().getSimpleName().toLowerCase();
            result = "/admin/" + path + "/index?faces-redirect=true&name=" + getEntity() + "&id=" + getEntity().getId();
        } catch (Exception ex) {
//            logger.error(logMessage(ex.getMessage()));
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Actualizar " + nombre, ex.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return result;

    }

    /**
     * Actualiza el entity y devuelve la pantalla de visualización
     *
     * @return pantalla de visualización
     */
    public String actualizarYMostrar() {
        actualizar();
        String path = getEntity().getClass().getSimpleName().toLowerCase();
        return "/admin/" + path + "/show?faces-redirect=true&id=" + getEntity().getId();
    }

    /**
     * Borra un Entity invocando al Dao
     *
     * @param id Entity a borrar
     * @return path del index de Entitys
     * @throws Exception
     */
    public String borrar(int id) throws Exception {
        AbstractEntity entity = (AbstractEntity) getEntityDAO().findById(getEntity().getClass(), Long.valueOf(id));
        String nombre = entity.toString();
//        logger.info(logMessage("Eliminando ", nombre));
        try {
            getEntityDAO().destroy(entity);
        } catch (Exception ex) {
//            logger.error(logMessage(ex.getMessage()));
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eliminar " + nombre, ex.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        return null;
    }

    /**
     * Activa un Entity invocando al Dao
     *
     * @param id Entity a activar
     * @return path del index de Entitys
     * @throws Exception
     */
    public String activar(int id) throws Exception {
        AbstractEntity entity = (AbstractEntity) getEntityDAO().findById(getEntity().getClass(), Long.valueOf(id));
        String nombre = entity.toString();

//        logger.info(logMessage("Activando ", nombre));
        try {
            getEntityDAO().activate(entity);
        } catch (Exception ex) {
//            logger.error(logMessage(ex.getMessage()));
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Activar " + nombre, ex.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        return null;
    }

    /**
     * Desctiva un Entity invocando al Dao
     *
     * @param id Entity a desactivar
     * @return path del index de Entitys
     * @throws Exception
     */
    public String desactivar(int id) throws Exception {
        AbstractEntity entity = (AbstractEntity) getEntityDAO().findById(getEntity().getClass(), Long.valueOf(id));
        String nombre = entity.toString();

//        logger.info(logMessage("Desactivando ", nombre));
        try {
            getEntityDAO().deactivate(entity);
        } catch (Exception ex) {
//            logger.error(logMessage(ex.getMessage()));
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Desactivar " + nombre, ex.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        return null;
    }

//    public UploadedFile getFile() {
//        return file;
//    }
//
//    public void setFile(UploadedFile file) {
//        this.file = file;
//    }
//
//    public String upload(FileUploadEvent event) {
//        try {
//            String sufijo = (String) event.getComponent().getAttributes().get("sufijo");
//            UploadedFile uploadedFile = event.getFile();
////            util.Util.guardarArchivo(uploadedFile, "imagen_" + getEntity().getClass().getSimpleName().toLowerCase() + "_" + ((PersistentEntity) getEntity()).getId() + "_" + sufijo + ".png");
//        } catch (Exception ex) {
//        }
//        String path = getEntity().getClass().getSimpleName().toLowerCase();
//        return "/admin/" + path + "/index?faces-redirect=true";
//    }

    public String getImagen() {
        return getImagen(((AbstractEntity) getEntity()).getId());
    }

    public String getImagen(Long id) {
        return "/uploads/imagen_" + getEntity().getClass().getSimpleName().toLowerCase() + "_" + id + ".png";
    }

    public String getImagen(Long id, String sufijo) {
        return "/uploads/imagen_" + getEntity().getClass().getSimpleName().toLowerCase() + "_" + id + "_" + sufijo + ".png";
    }

    public String getImagenPerfil(Long id, String sufijo) {
        if (existsImage(id, sufijo)) {
            return "/uploads/" + getEntity().getClass().getSimpleName().toLowerCase() + "_" + id + "_" + sufijo + ".png";
        } else {
            return "/resources/images/no_foto_perfil.png";
        }

    }

    public boolean existsImage(Long id, String sufijo) {
        String path = "/uploads/" + getEntity().getClass().getSimpleName().toLowerCase() + "_" + id + "_" + sufijo + ".png";
        if (FacesContext.getCurrentInstance().getExternalContext().getRealPath("/uploads/" + getEntity().getClass().getSimpleName().toLowerCase() + "_" + id + "_" + sufijo + ".png") == null) {
            return false;
        }
        return true;
    }

    /**
     * Hace el foco en un campo cuando abre
     *
     * @param campo campo para focus
     */
    public void focus(String campo) {
        if (!FacesContext.getCurrentInstance().isPostback()) {
//            RequestContext.getCurrentInstance().execute("document.getElementById('" + campo + "').focus();");
//            PrimeFaces.current().executeScript("document.getElementById('" + campo + "').focus();");
        }
    }

//    public Usuario getCurrentUser() {
//        return (Usuario) getSessionMap().get("currentUser");
//    }

//    public void setCurrentUser(Usuario currentUser) {
//        getSessionMap().remove("currentUser");
//        if (null != currentUser) {
//            getSessionMap().put("currentUser", currentUser);
//        }
//    }

    public List<String> getAccionesHabilitadas() {
        return (List<String>) getSessionMap().get("accionesHabilitadas");
    }

    public void setAccionesHabilitadas(List<String> accionesHabilitadas) {
        getSessionMap().remove("accionesHabilitadas");
        if (null != accionesHabilitadas) {
            getSessionMap().put("accionesHabilitadas", accionesHabilitadas);
        }
    }

    public List<String> getAcciones() {
        return (List<String>) getSessionMap().get("acciones");
    }

    public void setAcciones(List<String> acciones) {
        getSessionMap().remove("acciones");
        if (null != acciones) {
            getSessionMap().put("acciones", acciones);
        }
    }

    public boolean isUserLoggedIn() {
        return getSessionMap().containsKey("currentUser");
    }

    public static String sino(boolean value) {
        return util.Util.sino(value);
    }

    public static String sn(boolean value) {
        return util.Util.sn(value);
    }

//    public DefaultStreamedContent getReporte(PersistentEntity entity, String nombreReporte, String tituloReporte, String descripcionReporte) {
//        List lista = new ArrayList();
//        lista.add(entity);
//        return getReporte(lista, nombreReporte, tituloReporte, descripcionReporte);
//    }
//
//    public DefaultStreamedContent getReporte(PersistentEntity entity, String nombreReporte, HashMap params) {
//        List lista = new ArrayList();
//        lista.add(entity);
//        return getReporte(lista, nombreReporte, params);
//    }
//
//    public DefaultStreamedContent getReporte(Object entity, String nombreReporte, HashMap params) {
//        List lista = new ArrayList();
//        lista.add(entity);
//        return getReporte(lista, nombreReporte, params);
//    }

//    public DefaultStreamedContent getReporte(List lista, String nombreReporte, String tituloReporte, String descripcionReporte) {
//        HashMap params = new HashMap();
//        params.put("tituloReporte", tituloReporte);
//        params.put("descripcionReporte", descripcionReporte);
//        return getReporte(lista, nombreReporte, params);
//    }

//    public DefaultStreamedContent getReporte(List lista, String nombreReporte, HashMap params) {
//        try {
//            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(lista);
//            String logo = "/resources/images/admin/logo.png"; //debe salir del ambito
//            String logoPath = servletContext.getRealPath(logo);
//            params.put("logo", logoPath);
//            params.put("usuario", this.getCurrentUser() != null ? this.getCurrentUser().getUsuarioHumanized() : "");
//            String absoluteDiskPath = servletContext.getRealPath("/WEB-INF/reports/" + nombreReporte + ".jasper");
//            JasperPrint jasperPrint = JasperFillManager.fillReport(absoluteDiskPath, params, jRBeanCollectionDataSource);
//            JasperExportManager.exportReportToPdfStream(jasperPrint, output);
//            InputStream input = new ByteArrayInputStream(output.toByteArray());
//            return new DefaultStreamedContent(input, "attachment", nombreReporte + ".pdf");
//        } catch (Exception ex) {
//            logger.error(logMessage(ex.getMessage()));
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, nombreReporte, ex.getLocalizedMessage());
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            return null;
//        }
//    }

//    public DefaultStreamedContent getReporteApi(String nombreReporte, String codReporte, HashMap params) {
//        try {
//            final MediaType jJSON = MediaType.parse("application/json; charset=utf-8");
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("nombreReporte", nombreReporte);
//            jsonObject.put("cod", codReporte);
//            params.forEach((k,v) -> jsonObject.put(String.valueOf(k), v));
//            OkHttpClient client = new OkHttpClient();
//            RequestBody body = RequestBody.create(jJSON, String.valueOf(jsonObject));
//            String pathApi = configuracionDAO.getConfiguracion("urlApiReportesProd");
//            Request request = new Request.Builder()
//                    .url(pathApi + "/obtener")
//                    .post(body)
//                    .build();
//            InputStream input = client.newCall(request).execute().body().byteStream();
//            return new DefaultStreamedContent(input, "application/pdf", nombreReporte + ".pdf");
//        } catch (Exception ex) {
//            logger.error(logMessage(ex.getMessage()));
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, nombreReporte, ex.getLocalizedMessage());
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            return null;
//        }
//    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public boolean isVerInactivos() {
        return verInactivos;
    }

    public void setVerInactivos(boolean verInactivos) {
        this.verInactivos = verInactivos;
    }

    public boolean isInactivos() {
        return inactivos;
    }

    public void setInactivos(boolean inactivos) {
        this.inactivos = inactivos;
    }

    public List<Boolean> getColumnasVisibles() {
        return columnasVisibles;
    }

    public void setColumnasVisibles(List<Boolean> columnasVisibles) {
        this.columnasVisibles = columnasVisibles;
    }

    public String getTodayLong() {
        return util.Util.formatLONG(new Date());
    }

    //-----------------USUARIO EMPRESA-----------------
//    public UsuarioEmpresa getCurrentUserEmpresa() {
//        return (UsuarioEmpresa) getSessionMap().get("currentUserEmpresa");
//    }
//
//    public void setCurrentUserEmpresa(UsuarioEmpresa currentUserEmpresa) {
//        getSessionMap().remove("currentUserEmpresa");
//        if (null != currentUserEmpresa) {
//            getSessionMap().put("currentUserEmpresa", currentUserEmpresa);
//        }
//    }

    public boolean isUserEmpresaLoggedIn() {
        return getSessionMap().containsKey("currentUserEmpresa");
    }

    //----------------MENSAJES BASICOS-----------------
    public void error(String titulo, String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje));
    }

    public void info(String titulo, String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje));
    }

    //------------------ACCIONES-----------------------
////    public List<String> accionesAll() {
////        List<Accion> acciones = accionDAO.findAllNamedBy("Accion.findAllActivos");
////        List<String> nombres = new ArrayList<>();
////        for (Accion accion : acciones) {
////            nombres.add(accion.getNombre());
////        }
////        return nombres;
////    }
//
//    public List<String> accionesHabilitadas(Usuario usuario) {
//        List<Accion> acciones = accionDAO.findAllNamedBy("Accion.findAllByUsuario", usuario);
//        List<String> nombres = new ArrayList<>();
//        for (Accion accion : acciones) {
//            nombres.add(accion.getNombre());
//        }
//        return nombres;
//    }
//
//    //---------------FILTROS--------------------
//    public List<Oficina> getoficinasHabilitadas() {
//        List<OficinaUsuario> oficinasUsuario = oficinaUsuarioDAO.findAllNamedBy("OficinaUsuario.findByUsuario", getCurrentUser());
//        List<Oficina> oficinasHabilitadas = new ArrayList<>();
//        for (OficinaUsuario ou : oficinasUsuario) {
//            oficinasHabilitadas.add(ou.getOficina());
//        }
//        return oficinasHabilitadas;
//    }

    public void showInactivos() {
        if (getDataModel().getPredicados() == null) {
            Map<String, Object> predicados = new HashMap<>();
            getDataModel().setPredicados(predicados);
        }
        if (verInactivos) {
            getDataModel().getPredicados().remove("estado");
            getDataModel().getPredicados().put("estado", "B");
        } else {
            getDataModel().getPredicados().remove("estado");
            getDataModel().getPredicados().put("estado", "A");
        }
    }

//    public GenericDAO<Configuracion> getConfiguracionDAO() {
//        return configuracionDAO;
//    }
//
//    public void setConfiguracionDAO(GenericDAO<Configuracion> configuracionDAO) {
//        this.configuracionDAO = configuracionDAO;
//    }
//
//    public UsuarioDAO getUsuarioDAO() {
//        return usuarioDAO;
//    }
//
//    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
//        this.usuarioDAO = usuarioDAO;
//    }
//
//    public EmpleadoDAO getEmpleadoDAO() {
//        return empleadoDAO;
//    }
//
//    public void setEmpleadoDAO(EmpleadoDAO empleadoDAO) {
//        this.empleadoDAO = empleadoDAO;
//    }
//
//    public GenericDAO<OficinaUsuario> getOficinaUsuarioDAO() {
//        return oficinaUsuarioDAO;
//    }
//
//    public void setOficinaUsuarioDAO(GenericDAO<OficinaUsuario> oficinaUsuarioDAO) {
//        this.oficinaUsuarioDAO = oficinaUsuarioDAO;
//    }
//
//    public GenericDAO<Accion> getAccionDAO() {
//        return accionDAO;
//    }
//
//    public void setAccionDAO(GenericDAO<Accion> accionDAO) {
//        this.accionDAO = accionDAO;
//    }

//    public static Logger getLogger() {
//        return logger;
//    }

//    public static void setLogger(Logger logger) {
//        AbstractBacking.logger = logger;
//    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public String getContextoAnterior() {
        return contextoAnterior;
    }

    public void setContextoAnterior(String contextoAnterior) {
        this.contextoAnterior = contextoAnterior;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean canExecute(String actionName) {
        return !this.getAcciones().contains(actionName) || this.getAccionesHabilitadas().contains(actionName);
    }

    public String getEntityClassNameLowerCase() {
        return this.getEntityClassNameLowerCase(this.getEntity());
    }

    public String getEntityClassNameLowerCase(AbstractEntity entity) {
        return entity.getClass().getSimpleName().toLowerCase();
    }

    public int getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(int activeTab) {
        this.activeTab = activeTab;
    }

    public boolean canExecute(AbstractEntity entity, String metodo) {
        String page = this.getEntityClassNameLowerCase(entity) + "." + metodo;
        if (this.canExecute(page)) {
            try {
                String metodocan = "can" + metodo.substring(0, 1).toUpperCase() + metodo.substring(1);
                Method method = entity.getClass().getMethod(metodocan);
                return (Boolean) method.invoke(entity);
            } catch (Exception var6) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean canEdit(PersistentEntity entity, String field) {
//        String modo = this.getUsuarioDAO().isNew(entity) ? "new" : "edit";
        return this.canExecute(entity.getClass().getSimpleName() + "." + field + ".");
    }

    public boolean isContextoForm() {
        return this.contexto.equals("index") ? false : this.canExecute(this.getEntity(), this.contexto);
    }

    public boolean isContextoIndex() {
        return !this.contexto.equals("index") ? false : this.canExecute(this.getEntityClassNameLowerCase() + "." + this.contexto);
    }

    public void listar() {
        this.contexto = "index";
    }

    public void nuevo() {
        this.reset();
        this.contexto = "new";
    }

    public void editar(AbstractEntity entity, String contextoAnterior) {
        if (this.canExecute(entity, "edit")) {
            this.contextoAnterior = contextoAnterior;
            this.reset();
            this.setEntity(entity);
            this.contexto = "edit";
            this.reload();
        }

    }

    public void ver(AbstractEntity entity) {
        if (this.canExecute(entity, "show")) {
            this.reset();
            this.setEntity(entity);
            this.contexto = "show";
            this.reload();
        }

    }

    public abstract void newEntity();

    public void reset() {
        this.newEntity();
//        PrimeFaces.current().resetInputs(new String[]{"contextoform"});
    }

    public void reload() {
    }

//    public void rowSelect(SelectEvent event) {
//        AbstractEntity baseEntity = (AbstractEntity) event.getObject();
//        String destino = (String) event.getComponent().getAttributes().get("destino");
//        if (null != destino) {
//            String metodo = (String) event.getComponent().getAttributes().get("metodo");
//            Long id = baseEntity.getId();
//            if (null != metodo) {
//                try {
//                    String metodoget = "get" + metodo.substring(0, 1).toUpperCase() + metodo.substring(1);
//                    Method method = baseEntity.getClass().getMethod(metodoget);
//                    AbstractEntity newEntity = (AbstractEntity) method.invoke(baseEntity);
//                    id = newEntity.getId();
//                } catch (Exception var9) {
//                }
//            }
//
//            this.redirect(destino + "/show.xhtml?id=" + id);
//        } else {
//            this.ver(baseEntity);
//        }
//    }

    /**
     * Redirección genérica de items en tablas Por defecto redirecciona al show
     * del mismo tipo de entity, pero si tiene destino seteado como atributo lo
     * redirecciona al show del destino, y si tiene metodo seteado toma el id del metodo
     * SIN VIEWS
     *
//     * @param event
     */
//    public void onTableRowSelect(SelectEvent event) {
//        AbstractEntity baseEntity = (AbstractEntity) event.getObject();
//        Long id = baseEntity.getId();
//        String path = "show.xhtml?id=" + id;
//        String destino = (String) event.getComponent().getAttributes().get("destino");
//        if (null != destino) {
//            String metodo = (String) event.getComponent().getAttributes().get("metodo");
//            if (null != metodo) {
//                try {
//                    String metodoget = "get" + metodo.substring(0, 1).toUpperCase() + metodo.substring(1);
//                    Method method = baseEntity.getClass().getMethod(metodoget);
//                    AbstractEntity newEntity = (AbstractEntity) method.invoke(baseEntity);
//                    id = newEntity.getId();
//                } catch (Exception e) {
//                }
//            }
//            path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/admin/" + destino + "/show.xhtml?id=" + id;
//        }
//        try {
//            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
//        } catch (IOException ex) {
//        }
//    }


    protected void redirect(String view) {
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(path + view);
        } catch (IOException var3) {
        }
    }

    public String create() {
        String result = this.crear();
        if (result != null) {
            this.contexto = "index";
            this.reset();
        }
        return result;
    }

    public String createAndShow() {
        String result = this.crear();
        if (result != null) {
            this.contexto = "show";
        }

        return result;
    }

    public String createAndNext() {
        String result = this.crear();
        if (result != null) {
            this.contexto = "new";
            this.informarMensaje(this.entity, "crear", "crear", "creado");
            this.reset();
        }

        return result;
    }

    public String update() {
        String result = this.actualizar();
        if (result != null) {
            this.contexto = this.contextoAnterior;
            if ("index".equals(this.contexto)) {
                this.reset();
            }
        }

        return result;
    }

    public void activate(AbstractEntity entity) {
        this.contexto = "index";
        String nombre = entity.toString();

        try {
            this.getEntityDAO().activate(entity);
        } catch (Exception var4) {
            this.informarError(this.getEntity(), "activar", nombre, var4.getLocalizedMessage());
        }

    }

    public void deactivate(AbstractEntity entity) {
        this.contexto = "index";
        String nombre = entity.toString();

        try {
            this.getEntityDAO().deactivate(entity);
        } catch (Exception var4) {
            this.informarError(this.getEntity(), "desactivar", nombre, var4.getLocalizedMessage());
        }

    }

    public void delete(AbstractEntity entity) {
        this.contexto = "index";
        String nombre = entity.toString();

        try {
            this.getEntityDAO().destroy(entity);
        } catch (Exception var4) {
            this.informarError(this.getEntity(), "borrar", nombre, "error_borrar");
        }

    }

    public String getDefaultCommand() {
        String var1 = this.contexto;
        byte var2 = -1;
        switch (var1.hashCode()) {
            case 108960:
                if (var1.equals("new")) {
                    var2 = 1;
                }
                break;
            case 3108362:
                if (var1.equals("edit")) {
                    var2 = 2;
                }
                break;
            case 3529469:
                if (var1.equals("show")) {
                    var2 = 0;
                }
        }

        switch (var2) {
            case 0:
                return "botoneditar";
            case 1:
                return "botoncreateandshow";
            case 2:
                return "botonupdate";
            default:
                return "botonnuevo";
        }
    }

    public void filtrarInactivos(boolean value) {
        this.setInactivos(value);
        this.filtrarInactivos();
    }

    public void filtrarInactivos() {
        Map<String, Object> predicados = this.getDataModel().getPredicados();
        if (predicados == null) {
            predicados = new HashMap();
        }

        ((Map) predicados).remove("estado");

        if (this.isInactivos()) {
            ((Map) predicados).put("estado", "B");
        } else {
            ((Map) predicados).put("estado", "A");
        }

        this.getDataModel().setPredicados((Map) predicados);
    }

//    public void onToggle(ToggleEvent e) {
//        this.columnasVisibles.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
//    }

    //------------------------FLASH-------------------------------

    public String show(AbstractEntity entity) {
        String view = "/views/admin/" + getEntityClassNameLowerCase(entity) + "/view";
        return flash(view, "show", entity);
    }

    public String show(AbstractEntity entity, Integer tab) {
        String view = "/views/admin/" + getEntityClassNameLowerCase(entity) + "/view";
        return flash(view, "show", entity, tab);
    }

    public String show(AbstractEntity entity, String from) {
        String view = "/views/admin/" + getEntityClassNameLowerCase(entity) + "/view";
        return flash(view, "show", from, entity);
    }

    public String edit(AbstractEntity entity) {
        String view = "/views/admin/" + getEntityClassNameLowerCase(entity) + "/view";
        return flash(view, "edit", entity);
    }

    public String edit(AbstractEntity entity, String from) {
        String view = "/views/admin/" + getEntityClassNameLowerCase(entity) + "/view";
        return flash(view, "edit", from, entity);
    }

    public String list(String vista, String data) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(false);
        flash.put("data", data);
        return vista + "?faces-redirect=true";
    }

    public String flash(String vista, String contexto) {
        return flash(vista, contexto, null);
    }

    public String flash(String vista, AbstractEntity entity) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(false);
        flash.put("entity", entity);
        return vista + "?faces-redirect=true";
    }

    public String flash(String vista, String contexto, AbstractEntity entity) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(false);
        flash.put("contexto", contexto);
        flash.put("entity", entity);
        return vista + "?faces-redirect=true";
    }

    public String flash(String vista, String contexto, AbstractEntity entity, Integer tab) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(false);
        flash.put("contexto", contexto);
        flash.put("entity", entity);
        flash.put("tab", tab);
        return vista + "?faces-redirect=true";
    }

    public String flash(String vista, String contexto, String from, AbstractEntity entity) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(false);
        flash.put("contexto", contexto);
        flash.put("from", from);
        flash.put("entity", entity);
        return vista + "?faces-redirect=true";
    }

    public String flash(String vista, String contexto, Long id, String clase) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(false);
        flash.put("contexto", contexto);
        flash.put("id", id);
        flash.put("clase", clase);
        return vista + "?faces-redirect=true";
    }

    public String flash(String vista, AbstractEntity entity, Map<String, String> params) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(false);
        flash.put("entity", entity);
        String result = vista + "?faces-redirect=true";
        Iterator<String> claves = params.keySet().iterator();
        while (claves.hasNext()) {
            String clave = claves.next();
            result += "&" + clave + "=" + params.get(clave);
        }
        return result;
    }

    public String flash(String vista, Map<String, Object> params) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(false);
        String result = vista + "?faces-redirect=true";
        Iterator<String> claves = params.keySet().iterator();
        while (claves.hasNext()) {
            String clave = claves.next();
            flash.put(clave, params.get(clave));
        }
        return result;
    }

    public void recuperarFlash() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        Object contexto = flash.get("contexto");
        if ("new".equals(contexto)) {
            setContexto(contexto.toString());
            return;
        }
        AbstractEntity entity = null;
        if (flash.get("id") != null) {
            try {
                entity = (AbstractEntity) getEntityDAO().findById(Class.forName(flash.get("clase").toString()), Long.parseLong(flash.get("id").toString()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            entity = (AbstractEntity) flash.get("entity");
        }
        if (entity != null && getEntityClassNameLowerCase().equals(getEntityClassNameLowerCase(entity))) {
            setEntity(entity);
            Object from = flash.get("from");
            if (from != null) {
                setFrom((String) from);
            }
            if (contexto != null) {
                setContexto((String) contexto);
                if ("show".equals(contexto))
                    ver(entity);
                if ("edit".equals(contexto))
                    editar(entity, getFrom());
            }
            Object tab = flash.get("tab");
            if (tab != null) {
                setActiveTab(((Integer) tab).intValue());
            }
        }
    }

    public String getFechaSinEspacios() {
        return util.Util.format(new Date(), "yyyy-MM-dd-HHmmss");
    }

    //----------------------FILTRO TABLA----------------------------
    public void clearMultiViewState(String name) {
//        PrimeFaces.current().clearTableStates();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String outcome = "/admin/" + name + "/index.xhtml?faces-redirect=true";
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
    }

//    public GenericDAO<Oficina> getOficinaDAO() {
//        return oficinaDAO;
//    }
//
//    public GenericDAO<Area> getAreaDAO() {
//        return areaDAO;
//    }
//
//    public List<String> obtenerListaString(String lista) {
//        String aux = configuracionDAO.getConfiguracion(lista);
//        if (aux != null) {
//            return util.getList(aux, ",");
//        }
//        return new ArrayList<>();
//    }

    public void copyFile(String destino, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(destino));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
    }

    public void removeFile(String absolutePath) {
        try {
            Files.deleteIfExists(Paths.get(absolutePath));
        } catch (IOException ex) {
//            logger.error(logMessage(ex.getMessage()));
        }
    }

    public void filtrarPorFecha(String campoFecha) {
        Map<String, Object> predicados = getDataModel().getPredicados();
        if (predicados == null) {
            predicados = new HashMap<>();
        }
        predicados.put("campoFechaMenor", campoFecha);
        predicados.put("campoFechaMayor", campoFecha);
        predicados.put("valorFechaMenor", getDesde());
        predicados.put("valorFechaMayor", getHasta());
        getDataModel().setPredicados(predicados);
    }

    //Dependencias----------------
//    public Dependencia getCurrentDependencia() {
//        if (getSessionMap().get("currentDependencia") == null) {
//            return null;
//        }
//        return (Dependencia) getSessionMap().get("currentDependencia");
//    }
//
//    public void setCurrentDependencia(Dependencia dependencia) {
//        getSessionMap().remove("currentDependencia");
//        if (null != dependencia) {
//            getSessionMap().put("currentDependencia", dependencia);
//        }
//    }
//
//    //----------------XLS-------------------------
//    public void postProcessXLS(Object document) {
//        HSSFWorkbook wb = (HSSFWorkbook) document;
//        HSSFSheet sheet = wb.getSheetAt(0);
//        int filas = sheet.getLastRowNum();
//        HSSFRow header = sheet.getRow(0);
//        CellStyle doubleCellStyle = wb.createCellStyle();
//        CellStyle longCellStyle = wb.createCellStyle();
//        doubleCellStyle.setDataFormat(wb.createDataFormat().getFormat("#0.00"));
//        longCellStyle.setDataFormat(wb.createDataFormat().getFormat("#"));
//        CellStyle dateCellStyle = wb.createCellStyle();
//        dateCellStyle.setDataFormat(wb.createDataFormat().getFormat("m/d/yy"));
//        for(int f=1; f<= filas; f++) {
//            int cantidadColumnas = sheet.getRow(f).getPhysicalNumberOfCells();
//            for (int i = 0; i < cantidadColumnas; i++) {
//                Cell currentCell = sheet.getRow(f).getCell(i);
//                CellStyle currentCellStyle = currentCell.getCellStyle();
//                String data = currentCell.getStringCellValue().replaceAll("\\s", " ").replaceAll("<br/>", " ");
//                try { //trata de pasarlo a date
//                    Date fecha = util.parseDateWithEx(currentCell.getStringCellValue());
//                    currentCell.setCellValue(fecha);
//                    currentCell.setCellStyle(dateCellStyle);
//                }
//                catch (Exception e) {
//                    try { //trata de pasarlo a number
//                        boolean money = data.contains("$");
//                        double valor = Double.parseDouble(data.replace(".","").replace(",",".").replace("$", "").replace(String.valueOf((char) 160),"").trim());
//                        currentCell.setCellValue(valor);
//                        currentCell.setCellStyle(doubleCellStyle);
//                        if (!money && valor % 1 == 0) currentCell.setCellStyle(longCellStyle); //es entero
//                    }
//                    catch (Exception e2) {
//                        currentCell.setCellValue(data);
//                        currentCell.setCellStyle(currentCellStyle);
//                    }
//                }
//
//            }
//        }
//    }
}
