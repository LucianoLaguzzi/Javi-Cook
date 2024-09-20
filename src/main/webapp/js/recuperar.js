let botonRecuperarContrasenia = document.querySelector('.boton-recuperar-contrasenia');

if (botonRecuperarContrasenia) {
    botonRecuperarContrasenia.addEventListener('click', function (event) {
        if (!validarUsuario()) {
            event.preventDefault();
        } else {
            // Mostrar el mensaje de confirmación después de la respuesta exitosa del servidor
            mostrarMensajeConfirmacion();
        }
    });
}

function validarUsuario() {
    console.log("Se ingreso al metodo de verificar nombre")
    let inputRecuperarUsuario = document.querySelector('.input-recuperar-usuario');
    let mensajeErrorUsuarioRecuperacion = document.querySelector('.error-usuario-recuperar');
    let entradaRecuperar = document.querySelector('.entrada-recuperar');

    // Verificar si el campo titulo está vacío
    if (inputRecuperarUsuario.value.trim() === '') {
        mensajeErrorUsuarioRecuperacion.textContent = 'Por favor, ingrese un usuario.';
        entradaRecuperar.classList.add('error-usuario-recuperar-dinamico');
        entradaRecuperar.focus();
        return false;

    } else {
        // Limpiar mensaje de error si el campo no está vacío
        mensajeErrorUsuarioRecuperacion.textContent = '';
        // Remover el resaltado del campo requerido
        entradaRecuperar.classList.remove('error-usuario-recuperar-dinamico');
        return true;
    }
}

function mostrarMensajeConfirmacion() {
    console.log("Entro al metodo del mensaje de confirmacion")
    // Mostrar el div de mensaje de confirmación
    document.querySelector(".mensaje-confirmacion-recuperar").style.display = 'block';

    // Ocultar el mensaje después de segundos
    setTimeout(function() {
        document.querySelector(".mensaje-confirmacion-recuperar").style.display = 'none';
    }, 10000); //
}
