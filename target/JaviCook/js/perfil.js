// Agregar script para ocultar el contenedor después de 10 segundos
function cerrarSesion() {
    console.log("se entro al metodo cerrar sesion de perfil")
    let boton = document.querySelector('.boton-cerrar-sesion3');
    boton.click();

}



function mostrarRecetasUsuario(event) {
    event.preventDefault();
    let botonRecetas = document.querySelector('.recetas-del-usuario');
    if (botonRecetas.style.display === 'none' || botonRecetas.style.display === '') {
        botonRecetas.style.display = 'block';
    } else {
        botonRecetas.style.display = 'none';
    }
}



function previewImage(event) {
    const fileInput = event.target;
    const preview = document.getElementById('imagePreview');
    const file = fileInput.files[0];
    const reader = new FileReader();

    reader.onloadend = function () {
        preview.src = reader.result;
    }

    if (file) {
        reader.readAsDataURL(file);
    } else {
        preview.src = "#{usuarioBacking.usuario.imagenPerfil}";
    }

    // Habilitar el botón de guardar imagen
    document.querySelector('.boton-guardar-imagen').classList.add('visible');
}

//Edicion nombre de usuario
function mostrarInput() {
    let btnEditar = document.querySelector('.btn-editar-user');
    let outputNombre = document.querySelector('.output-nombre-usuario');
    let inputNombre = document.querySelector('.input-nuevo-nombre');
    let btnCancelar = document.querySelector('.btn-cancelar-user');
    let btnGuardarIcon = document.querySelector('.btn-guardar-icon');

    // Ocultar el botón de edición y mostrar los botones de cancelar y guardar
    btnEditar.style.display = 'none';

    outputNombre.style.display = 'none';
    inputNombre.style.display = 'block';
    btnCancelar.style.display = 'inline-block';
    btnGuardarIcon.style.display = 'inline-block';
}

function cancelarInput() {
    let btnEditar = document.querySelector('.btn-editar-user');
    let outputNombre = document.querySelector('.output-nombre-usuario');
    let inputNombre = document.querySelector('.input-nuevo-nombre');
    let btnCancelar = document.querySelector('.btn-cancelar-user');
    let btnGuardarIcon = document.querySelector('.btn-guardar-icon');

    // Mostrar el botón de edición y ocultar los botones de cancelar y guardar
    btnEditar.style.display =('block');

    outputNombre.style.display = 'block';
    inputNombre.style.display = 'none';
    btnCancelar.style.display = 'none';
    btnGuardarIcon.style.display = 'none';
}

function guardarInput() {
    let btnGuardarUser = document.querySelector('.btn-guardar-user');
    btnGuardarUser.click();
}




//Edicion email de usuario
function mostrarInputEmail() {
    let btnEditar = document.querySelector('.btn-editar-email');
    let outputEmail = document.querySelector('.output-email-usuario');
    let inputEmail = document.querySelector('.input-nuevo-email');
    let btnCancelarEmail = document.querySelector('.btn-cancelar-email');
    let btnGuardarIconEmail = document.querySelector('.btn-guardar-icon-email');

    // Ocultar el botón de edición y mostrar los botones de cancelar y guardar
    btnEditar.style.display = 'none';

    outputEmail.style.display = 'none';
    inputEmail.style.display = 'block';
    btnCancelarEmail.style.display = 'inline-block';
    btnGuardarIconEmail.style.display = 'inline-block';
}

function cancelarInputEmail() {
    let btnEditar = document.querySelector('.btn-editar-email');
    let outputEmail = document.querySelector('.output-email-usuario');
    let inputEmail = document.querySelector('.input-nuevo-email');
    let btnCancelarEmail = document.querySelector('.btn-cancelar-email');
    let btnGuardarIconEmail = document.querySelector('.btn-guardar-icon-email');

    // Mostrar el botón de edición y ocultar los botones de cancelar y guardar
    btnEditar.style.display =('block');

    outputEmail.style.display = 'block';
    inputEmail.style.display = 'none';
    btnCancelarEmail.style.display = 'none';
    btnGuardarIconEmail.style.display = 'none';
}

function guardarInputEmail() {
    let btnGuardarEmail = document.querySelector('.btn-guardar-email');
    btnGuardarEmail.click();
}