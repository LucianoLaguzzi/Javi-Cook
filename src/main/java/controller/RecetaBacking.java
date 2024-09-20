package controller;

import dao.IngredienteDAO;
import dao.RecetaDAO;
import datamodel.GenericDataModel;
import model.Ingrediente;
import model.Receta;
import model.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@ManagedBean(name="recetaBacking")
@SessionScoped
public class RecetaBacking  extends AbstractBacking<Receta>{

    @EJB
    RecetaDAO recetaDAO;

    @EJB
    IngredienteDAO ingredienteDAO;


    private GenericDataModel<Receta> dataModel;
    private Receta receta;

    private List<Receta> listaRecetas;

    private boolean hayRecetas;

    private String pathFinalSnapshot = "";

    private String filePath = "";

    private List<String> ingredientesCantidades = new ArrayList<>();

    private int cantidadRecetasCargadas = 0; // Inicializa la cantidad de recetas cargadas
    private int cantidadRecetasPorLote = 7; // Establece la cantidad de recetas por lote
    private String filtroIngredientes;


    // Nueva lista para almacenar el top 3 de recetas
    private List<Receta> top3Recetas = new ArrayList<>();

    private boolean recetasCargadas = false;


    public RecetaBacking() {
        receta = new Receta();
    }
    @PostConstruct
    @Override
    public void init() {
        if (!recetasCargadas) {
            newEntity();
            setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
            setInactivos(false);
            filtrarInactivos();
            cantidadRecetasCargadas = 0;
            obtenerRecetas();
            cargarTop3Recetas();
            recetasCargadas = true;
        }
    }

    @Override
    public void newEntity() {
        setEntity(new Receta());
    }



//Logica para el backing de recetas:

    public void obtenerRecetas() {
        try {
            if (filtroIngredientes != null && !filtroIngredientes.trim().isEmpty()) {
                listaRecetas = recetaDAO.buscarPorIngredientes(filtroIngredientes,cantidadRecetasCargadas, cantidadRecetasPorLote);
            } else {
                listaRecetas = recetaDAO.findRange(cantidadRecetasCargadas, cantidadRecetasPorLote);
            }

            if (!listaRecetas.isEmpty()) {
                hayRecetas = true;
            } else {
                System.out.println("No hay recetas para mostrar");
                hayRecetas = false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // Método para cargar más recetas cuando se presiona el botón "Ver +"
    public void cargarMasRecetas() {
        cantidadRecetasPorLote += 7; // Incrementa la cantidad de recetas cargadas
        obtenerRecetas(); // Vuelve a obtener las recetas para cargar el siguiente lote
    }


    // Nuevo método para cargar el top 3 de recetas
    public void cargarTop3Recetas() {
        List<Receta> valoradas = recetaDAO.findTop3Recetas().stream()
                .filter(receta -> receta.getValoracion() > 0)
                .sorted((r1, r2) -> Integer.compare(r2.getValoracion(), r1.getValoracion()))
                .collect(Collectors.toList());

        top3Recetas.clear();
        top3Recetas.addAll(valoradas);
        completarConRecetasVacias(top3Recetas);
    }

    private void completarConRecetasVacias(List<Receta> recetas) {
        while (recetas.size() < 3) {
            recetas.add(null); // Usa null como marcador de posición para recetas vacías
        }
    }


    public void buscarPorIngredientes() {
        if (filtroIngredientes == null || filtroIngredientes.trim().isEmpty()) {
            filtroIngredientes = null;
            cantidadRecetasCargadas = 0;
            cantidadRecetasPorLote = 7;
            obtenerRecetas();
        } else {
            cantidadRecetasCargadas = 0;
            cantidadRecetasPorLote = 7;
            listaRecetas = recetaDAO.buscarPorIngredientes(filtroIngredientes,cantidadRecetasCargadas, cantidadRecetasPorLote);
        }
        hayRecetas = !listaRecetas.isEmpty();

        // Redirecciona para evitar reenviar formulario
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//Logica para guardar las recetas
public void registrarReceta() throws Exception {
    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext externalContext = context.getExternalContext();
    HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
    HttpSession session = request.getSession(false);
    Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");

    if (usuarioAutenticado != null) {
        try {
            // Obtener los datos de la receta
            Receta nuevaReceta = obtenerDatosReceta(request, usuarioAutenticado);

            // Guardar la receta y la imagen
            if (nuevaReceta != null) {
                recetaDAO.create(nuevaReceta);
                System.out.println("Receta creada con éxito!");
                guardarIngredientes(request, nuevaReceta);
                recetaDAO.update(nuevaReceta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } finally {
            actualizarListaRecetas();
            externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
        }
    } else {
        System.out.println("No hay usuario activo");
    }
}

    private Receta obtenerDatosReceta(HttpServletRequest request, Usuario usuarioAutenticado) throws IOException, ServletException {
        // Procesar los campos de la receta
        String titulo = receta.getTitulo();
        if (titulo == null || titulo.isEmpty()) {
            return null; // No se puede crear la receta sin título
        }

        // Cantidades e ingredientes
        List<String> cantidades = obtenerParametros(request, "cantidadIngrediente");
        List<String> pasos = obtenerParametros(request, "pasos");

        // Procesar la imagen
        String pathFinal = procesarImagen(request);

        // Obtener fecha y hora
        String fechaYHora = obtenerFechaYHora();

        // Crear y devolver la nueva receta
        return new Receta(titulo, cantidades, usuarioAutenticado, pasos, pathFinal, fechaYHora,
                receta.getCategoria(), receta.getDificultad(), receta.getTiempo_preparacion());
    }

    private List<String> obtenerParametros(HttpServletRequest request, String parametro) {
        String[] parametrosArray = request.getParameterValues(parametro);
        return (parametrosArray != null) ? Arrays.asList(parametrosArray) : new ArrayList<>();
    }

    private String procesarImagen(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("file");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = generarNombreArchivo(receta.getTitulo());
            // Guardar el archivo en el directorio del contenedor Docker
            String pathFinal = guardarArchivo(filePart, fileName);
            // Devuelve la ruta relativa para acceder a la imagen desde la web
            return "/uploads/" + fileName;  // Cambia la ruta aquí para que coincida con el volumen montado
        }
        System.out.println("No se obtuvo ninguna imagen");
        return null;
    }


    private String generarNombreArchivo(String titulo) {
        String fileName = titulo.toLowerCase().replaceAll("\\s+", "-") + "_" +
                DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now()) + "_img.jpg";
        return fileName;
    }

    private String guardarArchivo(Part filePart, String fileName) throws IOException {
        // Usa la ruta del volumen montado en Docker para almacenar archivos
        String path = obtenerRutaAbsolutaArchivo(fileName);

        // Crear el directorio si no existe
        Path pathDir = Paths.get(path).getParent();
        if (!Files.exists(pathDir)) {
            Files.createDirectories(pathDir);
        }

        byte[] fileContent;
        try (InputStream input = filePart.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            fileContent = byteArrayOutputStream.toByteArray();
        }
        try (OutputStream output = Files.newOutputStream(Paths.get(path))) {
            output.write(fileContent);
        }
        return path;
    }



    private String obtenerRutaAbsolutaArchivo(String fileName) {
        // Usa la ruta del volumen montado en Docker
        String absoluteDiskPath = Paths.get("/opt/jboss/wildfly/uploads", fileName).toString();
        return absoluteDiskPath;
    }



    private String obtenerFechaYHora() {
        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatoArgentino = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("es", "AR"));
        return fechaActual.format(formatoArgentino);
    }

    private void guardarIngredientes(HttpServletRequest request, Receta nuevaReceta) throws Exception {
        String ingredientesString = request.getParameter("ingredientes");
        String[] nombresIngredientes = (ingredientesString != null) ? ingredientesString.toLowerCase().split(",") : new String[0];

        List<Ingrediente> ingredientes = new ArrayList<>();
        for (String nombre : nombresIngredientes) {
            Ingrediente ingredienteExistente = ingredienteDAO.findByNombre(nombre);
            if (ingredienteExistente != null) {
                ingredientes.add(ingredienteExistente);
            } else {
                Ingrediente nuevoIngrediente = new Ingrediente(nombre);
                ingredienteDAO.create(nuevoIngrediente);
                ingredientes.add(nuevoIngrediente);
            }
        }
        nuevaReceta.setIngredientes(ingredientes);
    }


    public void actualizarListaRecetas() {
        obtenerRecetas();
    }



    public String verDetallesReceta(int idReceta) {
        // Lógica para cargar los detalles de la receta con el ID proporcionado
        return "detalle_receta?faces-redirect=true&idReceta=" + idReceta;
    }



    //Getters y Setters
    @Override
    public GenericDataModel<Receta> getDataModel() {
        return dataModel;
    }
    @Override
    public void setDataModel(GenericDataModel<Receta> dataModel) {
        this.dataModel = dataModel;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public List<Receta> getListaRecetas() {
        return listaRecetas;
    }

    public void setListaRecetas(List<Receta> listaRecetas) {
        this.listaRecetas = listaRecetas;
    }

    public boolean isHayRecetas() {
        return hayRecetas;
    }

    public void setHayRecetas(boolean hayRecetas) {
        this.hayRecetas = hayRecetas;
    }

    public List<String> getIngredientesCantidades() {
        return ingredientesCantidades;
    }

    public void setIngredientesCantidades(List<String> ingredientesCantidades) {
        this.ingredientesCantidades = ingredientesCantidades;
    }
    public String getFiltroIngredientes() {
        return filtroIngredientes;
    }

    public void setFiltroIngredientes(String filtroIngredientes) {
        this.filtroIngredientes = filtroIngredientes;
    }

    public List<Receta> getTop3Recetas() {
        return top3Recetas;
    }

    public void setTop3Recetas(List<Receta> top3Recetas) {
        this.top3Recetas = top3Recetas;
    }

    public boolean isRecetasCargadas() {
        return recetasCargadas;
    }

    public void setRecetasCargadas(boolean recetasCargadas) {
        this.recetasCargadas = recetasCargadas;
    }
}
