package controller;

import dao.UsuarioDAO;
import datamodel.GenericDataModel;
import model.PasswordUtil;
import model.Receta;
import model.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@ManagedBean(name="usuarioBacking")
@SessionScoped
public class UsuarioBacking  extends AbstractBacking<Usuario>{

    @EJB
    UsuarioDAO usuarioDAO;

    private GenericDataModel<Usuario> dataModel;
    private Usuario usuario;
    private List<String> erroresLogin = new ArrayList<>();
    private List<String> erroresRegistro = new ArrayList<>();
    private List<String> exitoRegistro = new ArrayList<>();

    private List<Receta> recetasPorUsuario;

    private Part nuevaImagen;

    private String uniqueImageParam;


    private String token; // Propiedad para almacenar el token

    private String nuevaContrasenia;
    private String confirmarContrasenia;

    private String nuevoNombre;
    private String nuevoEmail;




    public UsuarioBacking() {
        usuario = new Usuario();
    }
    @PostConstruct
    @Override
    public void init() {
        newEntity();
        setDataModel(new GenericDataModel<>(getEntityDAO(), getEntity()));
        setInactivos(false);
        filtrarInactivos();
        erroresLogin = new ArrayList<>();
        obtenerRecetasDeUsuario();
        generateUniqueImageParam();

        // Obtener el token desde los parámetros de la URL al iniciar el bean
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        token = params.get("token");

        if (token == null || token.isEmpty()) {
            System.out.println("Token no proporcionado.");
        }



        obtenerFavoritas();

    }



    public String iniciarSesion() {

        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatoArgentino = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("es", "AR"));
        String fechaYHora = fechaActual.format(formatoArgentino);
        System.out.println(fechaYHora);

        erroresLogin.clear();
        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();

        if (usuario.getNombre() == null || usuario.getNombre().isEmpty() || usuario.getContrasenia() == null || usuario.getContrasenia().isEmpty()) {
            erroresLogin.add("Debe ingresar un usuario y una contraseña");
            FacesMessage errorMessage = new FacesMessage("Debe ingresar un usuario y una contraseña");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            flash.put("erroresLogin", erroresLogin); // Almacenar mensajes en flash
            usuario.setNombre("");
            usuario.setContrasenia("");

            return "login.xhtml?faces-redirect=true";
        }

        Usuario usuarioEncontrado = usuarioDAO.findByNombre(usuario.getNombre());
        if (usuarioEncontrado != null) {
            // Verificar la contraseña
            if (PasswordUtil.checkPassword(usuario.getContrasenia(), usuarioEncontrado.getContrasenia())) {
                context.getExternalContext().getSessionMap().put("usuario", usuarioEncontrado);
                return "index.xhtml?faces-redirect=true";
            } else {
                erroresLogin.add("Nombre de usuario o contraseña incorrectos");
                FacesMessage errorMessage = new FacesMessage("Nombre de usuario o contraseña incorrectos");
                errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(null, errorMessage);
                flash.put("erroresLogin", erroresLogin); // Almacenar mensajes en flash
                usuario.setNombre("");
                usuario.setContrasenia("");
                return "login.xhtml?faces-redirect=true";
            }
        } else {
            erroresLogin.add("Nombre de usuario o contraseña incorrectos");
            FacesMessage errorMessage = new FacesMessage("Nombre de usuario o contraseña incorrectos");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            flash.put("erroresLogin", erroresLogin); // Almacenar mensajes en flash
            usuario.setNombre("");
            usuario.setContrasenia("");
            return "login.xhtml?faces-redirect=true";
        }
    }













    public String redireccionarACrearUsuario() {
        return "crear_usuario.xhtml?faces-redirect=true";
    }

    public String redireccionarALogin() {
        return "login.xhtml?faces-redirect=true";
    }

    public String redireccionarAlIndex() {
        return "index.xhtml?faces-redirect=true";
    }

    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("Cerrando sesion...");
        return "login.xhtml?faces-redirect=true";
    }


    public String registrarUsuario() throws Exception {
        erroresRegistro.clear();
        exitoRegistro.clear();

        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();

        if (usuario.getNombre() == null || usuario.getNombre().isEmpty() || usuario.getContrasenia() == null || usuario.getContrasenia().isEmpty()) {
            erroresRegistro.add("Usuario y contraseña requeridos para registrarse");
            System.out.println("No se ingreso usuario o contraseña!");

            flash.put("erroresRegistro", erroresRegistro); // Almacenar mensajes en flash
            usuario.setNombre("");
            usuario.setContrasenia("");
            usuario.setEmail("");
            return "crear_usuario.xhtml?faces-redirect=true";
        }

        try {
//Voy a la base de datos para ver si el usuario a crear ya existe
            Usuario verificarUsuario = usuarioDAO.findByNombre(usuario.getNombre());

//Si existe usuario con ese nombre:
            if (verificarUsuario != null) {
                erroresRegistro.clear();
                System.out.println("El usuario a crear ya existe");
                erroresRegistro.add("El usuario ingresado ya existe");
                flash.put("erroresRegistro", erroresRegistro); // Almacenar mensajes en flash
                usuario.setNombre("");
                usuario.setContrasenia("");
                usuario.setEmail("");
                return "crear_usuario.xhtml?faces-redirect=true";
            }
//Si no existe, hashear la contraseña y luego crear el usuario :
            String hashedPassword = PasswordUtil.hashPassword(usuario.getContrasenia());


            Usuario usuarioCreado = new Usuario(usuario.getNombre(),hashedPassword,usuario.getEmail(),"img/default-image.jpg");
            usuarioDAO.create(usuarioCreado);
            exitoRegistro.add("Usuario registrado con éxito");
            System.out.println("Usuario creado!");
            setEntity(new Usuario());

            flash.put("exitoRegistro", exitoRegistro);
            usuario.setNombre("");
            usuario.setContrasenia("");
            usuario.setEmail("");
            return "crear_usuario.xhtml?faces-redirect=true";


        }catch (Exception e){
            erroresRegistro.clear();
            System.out.println("Error al registrar usuario");
            erroresRegistro.add("Se produjo un error al crear el usuario");
            flash.put("erroresRegistro", erroresRegistro); // Almacenar mensajes en flash
        }

        return "crear_usuario.xhtml?faces-redirect=true";
    }



    public String irAPerfil() {
        cargarUsuarioDeSesion();
        obtenerRecetasDeUsuario(); // Asegúrate de que las recetas se cargan
        return "perfil?faces-redirect=true";
    }


    public void cargarUsuarioDeSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
        if (usuario == null) {
            redirigirALogin();
        } else {
            if (usuario.getEmail() == null || usuario.getId() == null) {
                usuario = usuarioDAO.findByIdWithRecetasFavoritas(usuario.getId());
                context.getExternalContext().getSessionMap().put("usuario", usuario);
            }
        }
    }

    public void redirigirALogin() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void obtenerRecetasDeUsuario() {
        if (usuario != null) {
            recetasPorUsuario = usuarioDAO.findRecetasPorUsuario(usuario.getId());
        } else {
            recetasPorUsuario = new ArrayList<>();
        }
    }


    public String verDetallesReceta(int idReceta) {
        // Lógica para cargar los detalles de la receta con el ID proporcionado
        return "detalle_receta?faces-redirect=true&idReceta=" + idReceta;
    }



    public String cambiarImagen() {
        // Ruta absoluta donde se guardarán las imágenes dentro del contenedor Docker
        String uploadDir = "/opt/jboss/wildfly/img";
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        try {
            Part filePart = request.getPart("file");
            if (filePart != null && filePart.getSize() > 0) {
                // Generar el nombre del archivo
                String fileName = usuario.getNombre().toLowerCase()
                        .replaceAll("\\s+$", "")
                        .replaceAll("\\s+", "-") + "_profile.jpg";

                // Crear el directorio si no existe
                File imgDir = new File(uploadDir);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }

                // Ruta completa donde se guardará la imagen
                String filePath = Paths.get(uploadDir, fileName).toString();

                System.out.println("El filePath es:  " + filePath);

                // Guardar la imagen
                try (InputStream input = filePart.getInputStream();
                     OutputStream output = Files.newOutputStream(Paths.get(filePath))) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                }

                // Actualizar la referencia de la imagen en la base de datos
                try {
                    // Almacenar la ruta relativa sin el contexto de la aplicación
                    usuario.setImagenPerfil("/img/" + fileName);
                    usuarioDAO.update(usuario);
                    System.out.println("Imagen de perfil actualizada correctamente");
                    System.out.println("Ruta de la imagen en el objeto usuario: " + usuario.getImagenPerfil());


                    // Generar un nuevo parámetro único para evitar problemas de caché
                    generateUniqueImageParam();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error al actualizar la imagen en la base de datos");
                }
            } else {
                System.out.println("Debe seleccionar una imagen para subir");
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            System.out.println("Error al procesar la imagen: " + e.getMessage());
        }

        return null;
    }






    public void toggleRecetaFavorita(Receta receta) throws Exception {
        if (usuario != null) {
            cargarUsuarioDeSesion();
            usuario = usuarioDAO.findByIdWithRecetasFavoritas(usuario.getId());

            if (usuario.getRecetasFavoritas() == null) {
                usuario.setRecetasFavoritas(new ArrayList<>());
            }

            if (receta != null) {
                if (usuario.getRecetasFavoritas().contains(receta)) {
                    usuario.getRecetasFavoritas().remove(receta);
                } else {
                    usuario.getRecetasFavoritas().add(receta);
                }
                usuarioDAO.update(usuario);
            }
        }
    }


    public boolean esRecetaFavorita(Receta receta) {
        cargarUsuarioDeSesion();
        usuario = usuarioDAO.findByIdWithRecetasFavoritas(usuario.getId());
        if (usuario != null && usuario.getRecetasFavoritas() != null) {
            return usuario.getRecetasFavoritas().contains(receta);
        }
        return false;
    }



    public String redireccionarARecuperarContrasenia() {
        return "recuperarContrasenia.xhtml?faces-redirect=true";
    }


    public String solicitarRecuperacionContrasenia() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();

        Usuario usuarioEncontrado = usuarioDAO.findByNombre(usuario.getNombre());
        if (usuarioEncontrado != null) {
            // Generar token y fecha de expiración
            String token = UUID.randomUUID().toString();
            LocalDateTime fechaExpiracion = LocalDateTime.now().plusHours(1);

            // Almacenar token y fecha de expiración en la base de datos
            usuarioEncontrado.setTokenRecuperacion(token);
            usuarioEncontrado.setFechaExpiracionToken(fechaExpiracion);
            usuarioDAO.update(usuarioEncontrado);

            // Enviar email con el enlace de recuperación
            String linkRecuperacion = "http://localhost:8080/JaviCook/cambiarContrasenia.xhtml?token=" + token;
            String mensaje = "Para recuperar tu cuenta y cambiar el password, haz clic en el siguiente enlace: " + linkRecuperacion;
            enviarEmail(usuarioEncontrado.getEmail(), "Recuperación de contraseña", mensaje);

            flash.put("mensajeRecuperacion", "Se ha enviado un enlace de recuperación a tu email.");
            return "login.xhtml?faces-redirect=true";
        } else {
            erroresLogin.add("Usuario no encontrado");
            FacesMessage errorMessage = new FacesMessage("Usuario no encontrado");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            flash.put("erroresLogin", erroresLogin);
            return "recuperarContrasenia.xhtml?faces-redirect=true";
        }
    }





    // Método para cambiar la contraseña
    public String cambiarContrasenia() throws Exception {
        if (token == null || token.isEmpty()) {
            System.out.println("Token no válido.");
            return null;
        }

        // Buscar el usuario por el token
        usuario = usuarioDAO.findByTokenRecuperacion(token);
        if (usuario == null) {
            System.out.println("Token inválido o expirado.");
            return null;
        }

        // Verificar si el token ha expirado
        LocalDateTime fechaExpiracionToken = usuario.getFechaExpiracionToken();
        if (fechaExpiracionToken == null || LocalDateTime.now().isAfter(fechaExpiracionToken)) {
            System.out.println("Token expirado.");
            return null;
        }


        // Verificar que las nuevas contraseñas no estén vacías
        if (confirmarContrasenia == null || confirmarContrasenia.isEmpty()) {
            System.out.println("Debe ingresar una nueva contraseña.");
            return null;
        }

        // Verificar que las contraseñas ingresadas coincidan
        if (!nuevaContrasenia.equals(confirmarContrasenia)) {
            System.out.println("Las contraseñas no coinciden.");
            return null;
        }

        // Hashear la nueva contraseña ingresada por el usuario
        String hashedNewPassword = PasswordUtil.hashPassword(confirmarContrasenia);

        // Actualizar la contraseña hasheada del usuario en la base de datos
        usuario.setContrasenia(hashedNewPassword);
        usuarioDAO.update(usuario);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Contraseña cambiada con éxito."));
        return "login?faces-redirect=true";
    }

    // Método para agregar mensajes de error
    private void addErrorMessage(String mensaje) {
        FacesMessage errorMessage = new FacesMessage(mensaje);
        errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, errorMessage);
    }






    private void enviarEmail(String emailDestinatario, String asunto, String cuerpo) {
        // Configuración de JavaMail para enviar correo electrónico
        String host = "smtp.gmail.com";
        String username = "javicook.app@gmail.com"; // Tu dirección de Gmail
        String password = "tdhqvpfqpzqhrbys"; // Contraseña de aplicación generada
        int port = 587; // Puerto SMTP para Gmail

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");

        // Habilitar TLS
        props.put("mail.smtp.starttls.enable", "true");

        // Crear la sesión de JavaMail
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Preparar el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Dirección del remitente (tu dirección de Gmail)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario)); // Destinatario del correo
            message.setSubject(MimeUtility.encodeText(asunto, "UTF-8", "B"));
            message.setText(cuerpo);
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");

            // Enviar el correo
            Transport.send(message);

            System.out.println("Correo de confirmación enviado a " + emailDestinatario);

        } catch (SendFailedException e) {
            System.out.println("Error: Dirección de correo no válida o no puede recibir correos: " + emailDestinatario);
            // Aquí puedes manejar el error, por ejemplo, notificando al usuario
        } catch (MessagingException e) {
            System.out.println("Error al enviar correo de confirmación: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }



    //Editar nombre de usuario y Email
    public void editarNombreUsuario() throws Exception {
        if (usuario != null) {
            System.out.println("El nuevo nombre para remplazar el anterior es : " + nuevoNombre);
            // Aquí deberías persistir el cambio en tu base de datos
            usuario.setNombre(nuevoNombre);
            System.out.println(usuario.getNombre());
            try {
                usuarioDAO.update(usuario);
                redirigirAPerfil();
            }catch (Exception e){
                System.out.println(e);
            }

        }
    }


    public void editarNombreEmail() {
        if (usuario != null) {
            System.out.println("El nuevo nombre para remplazar el anterior es : " + nuevoEmail);
            // Aquí deberías persistir el cambio en tu base de datos
            usuario.setEmail(nuevoEmail);
            System.out.println(usuario.getEmail());
            try {
                usuarioDAO.update(usuario);
                redirigirAPerfil();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }





    public void redirigirAPerfil(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("perfil.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void obtenerFavoritas() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            HttpSession session = request.getSession(false);

            // Obtener el usuario actualmente autenticado desde la sesión
            Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");
            if (usuarioAutenticado != null) {
                usuarioAutenticado.setRecetasFavoritas(usuarioDAO.findRecetasFavoritasByUsuario(usuarioAutenticado.getId()));
            }else {
                System.out.println("No hay usuario activo");
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }




    public String getImagenPerfilURL() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + usuario.getImagenPerfil();
    }



    public String getUniqueImageParam() {
        return uniqueImageParam;
    }

    public void generateUniqueImageParam() {
        this.uniqueImageParam = UUID.randomUUID().toString();
    }



    @Override
    public void newEntity() {
        setEntity(new Usuario());
    }


    @Override
    public GenericDataModel<Usuario> getDataModel() {
        return dataModel;
    }

    @Override
    public void setDataModel(GenericDataModel<Usuario> dataModel) {
        this.dataModel = dataModel;
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
            if (usuario == null) {
                redirigirALogin();
            }
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public List<String> getErroresLogin() {
        return erroresLogin;
    }

    public void setErroresLogin(List<String> erroresLogin) {
        this.erroresLogin = erroresLogin;
    }

    public List<String> getErroresRegistro() {
        return erroresRegistro;
    }

    public void setErroresRegistro(List<String> erroresRegistro) {
        this.erroresRegistro = erroresRegistro;
    }

    public List<String> getExitoRegistro() {
        return exitoRegistro;
    }

    public void setExitoRegistro(List<String> exitoRegistro) {
        this.exitoRegistro = exitoRegistro;
    }


    public List<Receta> getRecetasPorUsuario() {
        return recetasPorUsuario;
    }

    public void setRecetasPorUsuario(List<Receta> recetasPorUsuario) {
        this.recetasPorUsuario = recetasPorUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConfirmarContrasenia() {
        return confirmarContrasenia;
    }

    public void setConfirmarContrasenia(String confirmarContrasenia) {
        this.confirmarContrasenia = confirmarContrasenia;
    }

    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }

    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }

    public String getNuevoNombre() {
        return nuevoNombre;
    }

    public void setNuevoNombre(String nuevoNombre) {
        this.nuevoNombre = nuevoNombre;
    }

    public String getNuevoEmail() {
        return nuevoEmail;
    }

    public void setNuevoEmail(String nuevoEmail) {
        this.nuevoEmail = nuevoEmail;
    }



}

