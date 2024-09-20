let cambiarContrasenia = document.querySelector('.boton-cambiar-contrasenia');

if (cambiarContrasenia) {
    cambiarContrasenia.addEventListener('click', function (event) {
        if (!validarContrasenias()) {
            event.preventDefault();
        } else {
            // Mostrar el mensaje de confirmación después de la respuesta exitosa del servidor
            mostrarMensajeCambioExitoso();
        }
    });
}


function validarContrasenias() {
    let inputCambiarPass = document.querySelector('.input-cambiar-pass');
    let inputConfirmarPass = document.querySelector('.input-confirmar-pass');
    let errorMensajeCambiarPass = document.querySelector('.error-mensaje-cambiar-pass');

    if (inputCambiarPass && inputConfirmarPass) {
        let contrasenia = inputCambiarPass.value;
        let confirmarContrasenia = inputConfirmarPass.value;

        if ((contrasenia.trim() === '') || (confirmarContrasenia.trim() === '')) {
            errorMensajeCambiarPass.textContent = 'Los campos no pueden estar vacios. Inténtelo de nuevo.';
            inputCambiarPass.classList.add('error-mensaje-cambiar-pass-dinamico');
            inputConfirmarPass.classList.add('error-mensaje-cambiar-pass-dinamico');
            inputCambiarPass.value = '';
            inputConfirmarPass.value = '';
            inputCambiarPass.focus();
            return false; // Las contraseñas no coinciden, no permitir la acción
        }

        if (contrasenia !== confirmarContrasenia) {
            errorMensajeCambiarPass.textContent = 'Las contraseñas no coinciden. Por favor, inténtelo de nuevo.';
            inputCambiarPass.classList.add('error-mensaje-cambiar-pass-dinamico');
            inputConfirmarPass.classList.add('error-mensaje-cambiar-pass-dinamico');
            inputCambiarPass.value = '';
            inputConfirmarPass.value = '';
            inputCambiarPass.focus();
            return false; // Las contraseñas no coinciden, no permitir la acción
        }
        return true; // Las contraseñas coinciden, permitir la acción
    }
}


function mostrarMensajeCambioExitoso() {
    // Mostrar el div de mensaje de confirmación
    document.querySelector(".mensaje-confirmacion-cambiar").style.display = 'block';

    // Ocultar el mensaje después de 10 segundos
    setTimeout(function() {
        document.querySelector(".mensaje-confirmacion-cambiar").style.display = 'none';
    }, 10000);

}