function cerrarSesion() {
    console.log("se entro al metodo cerrar sesion de detallesJS")
    let boton = document.querySelector('.boton-cerrar-sesion2');
    console.log(boton)
    boton.click();

}


function mostrarMensajeConfirmacion() {
    // Mostrar el div de mensaje de confirmación
    document.querySelector(".mensaje-confirmacion").style.display = 'block';

    // Ocultar el mensaje después de segundos
    setTimeout(function() {
        document.querySelector(".mensaje-confirmacion").style.display = 'none';
    }, 10000); //
}



function mostrarInput() {
    let btnEditar = document.querySelector('.btn-editar-pasos');
    let outputPaso = document.querySelector('.paso-texto');
    let btnCancelarPaso = document.querySelector('.btn-cancelar-paso');
    let btnGuardarIconPaso = document.querySelector('.btn-guardar-paso');
    let pasosRecetaNuevo = document.querySelector('.div-pasos-receta');
    let pasosPanel = document.getElementById('pasosPanel');

    // Ocultar el botón de edición y mostrar los botones de cancelar y guardar
    btnEditar.style.display = 'none';
    pasosRecetaNuevo.style.display = 'flex';
    pasosRecetaNuevo.style.width = '100%';
    outputPaso.style.display = 'none';
    btnCancelarPaso.style.display = 'inline-block';
    btnGuardarIconPaso.style.display = 'inline-block';

    // Cargar los pasos en inputs
    let pasosTexto = document.querySelectorAll('.paso-item');
    pasosPanel.innerHTML = ''; // Limpiar panel

    pasosTexto.forEach((paso, index) => {
        let pasoNumero = paso.querySelector('.numero-paso').innerText;
        let pasoTexto = paso.querySelector('.texto-paso').innerText;

        // Crear el nuevo campo de entrada
        let divPaso = document.createElement('div');
        divPaso.classList.add('paso');
        divPaso.innerHTML = `<label for="paso${index + 1}" class="label-pasos">Paso ${index + 1}:</label>
                             <textarea id="paso${index + 1}" class="text-area-pasos-nuevos" placeholder="Agregar paso...">${pasoTexto}</textarea>`;

        pasosPanel.appendChild(divPaso);
    });

    // Mostrar el botón para agregar nuevos pasos
    document.querySelector('.btn-agregar-paso').style.display = 'block';

    // Ajustar tamaño de los textareas y agregar el evento de entrada
    document.querySelectorAll('.text-area-pasos-nuevos').forEach(textarea => {
        autoResizeTextArea(textarea);
        textarea.addEventListener('input', function () {
            autoResizeTextArea(this);
        });
    });
}

function agregarPaso() {
    let pasosPanel = document.querySelector('#pasosPanel');
    let pasoCounter = pasosPanel.querySelectorAll('.paso').length + 1;
    let nuevoPaso = document.createElement("div");
    nuevoPaso.classList.add("paso");
    nuevoPaso.innerHTML = `<label for="paso${pasoCounter}" class="label-pasos">Paso ${pasoCounter}:</label>
                           <textarea id="paso${pasoCounter}" class="text-area-pasos-nuevos" placeholder="Agregar paso..."></textarea>`;

    // Agregar evento de cambio al nuevo textarea
    let nuevoTextArea = nuevoPaso.querySelector('.text-area-pasos-nuevos');
    nuevoTextArea.addEventListener('input', function () {
        autoResizeTextArea(this);
        actualizarPasos();
    });

    pasosPanel.appendChild(nuevoPaso);
}

// Agregar evento de clic al botón "Agregar paso"
let agregarPasoButton = document.querySelector('.btn-agregar-paso');
if (agregarPasoButton) {
    agregarPasoButton.addEventListener("click", function (event) {
        event.preventDefault(); // Evitar comportamiento predeterminado del botón
        agregarPaso(); // Llamar a la función para agregar un nuevo paso
    });
}

function actualizarPasos() {
    // Obtener todos los textareas de pasos
    const pasosNuevos = document.querySelectorAll('.text-area-pasos-nuevos');
    // Obtener el valor de cada textarea y almacenarlo en un array
    const pasosValue = Array.from(pasosNuevos).map(textArea => textArea.value);
    // Actualizar el campo oculto con los pasos
    document.getElementById("inputOcultoDetalles").value = pasosValue.join('\n');
}

function cancelarInput() {
    let outputPaso = document.querySelector('.paso-texto');
    let pasosRecetaNuevo = document.querySelector('.div-pasos-receta');
    let btnCancelarPaso = document.querySelector('.btn-cancelar-paso');
    let btnGuardarIconPaso = document.querySelector('.btn-guardar-paso');
    let btnEditar = document.querySelector('.btn-editar-pasos');

    outputPaso.style.display = 'block';
    pasosRecetaNuevo.style.display = 'none';
    btnCancelarPaso.style.display = 'none';
    btnGuardarIconPaso.style.display = 'none';
    btnEditar.style.display = 'block';

    // Limpiar el panel de pasos
    document.querySelector('#pasosPanel').innerHTML = '';
}

function guardarInput() {
    // Actualizar los pasos antes de guardar
    actualizarPasos();
    document.querySelector('.btn-guardar-pasos').click();
}

// Función para redimensionar automáticamente el textarea
function autoResizeTextArea(textarea) {
    // Restablecer la altura a 0 para que el scrollHeight pueda medir la altura total del contenido
    textarea.style.height = '0';
    // Ajustar la altura del textarea según el contenido
    textarea.style.height = textarea.scrollHeight + 'px';
}



// Función para eliminar el último paso
function quitarPaso() {
    let pasosPanel = document.querySelector('#pasosPanel');
    let pasos = pasosPanel.querySelectorAll('.paso');
    if (pasos.length > 1) {
        pasos[pasos.length - 1].remove();
        actualizarPasos(); // Actualizar el campo oculto con los pasos
    }
}

// Agregar evento de clic al botón "Quitar paso"
let quitarPasoButton = document.querySelector('.btn-quitar-paso');
if (quitarPasoButton) {
    quitarPasoButton.addEventListener("click", function (event) {
        event.preventDefault(); // Evitar comportamiento predeterminado del botón
        quitarPaso(); // Llamar a la función para quitar el último paso
    });
}



function cambiarTitulo() {
    let btnEditarTitulo = document.querySelector('.btn-editar-titulo');
    let outputTitulo = document.querySelector('.detalles-titulo');
    let inputTitulo = document.querySelector('.nuevo-titulo');
    let btnCancelarTituloNuevo = document.querySelector('.btn-cancelar-titulo');
    let btnGuardarTituloNuevo = document.querySelector('.btn-guardar-titulo');

    let divCancelOk = document.querySelector('.cancel-ok-titulo');

    divCancelOk.style.display = 'flex';
    btnEditarTitulo.style.display = 'none';
    outputTitulo.style.display = 'none';
    inputTitulo.style.display = 'block';

    btnCancelarTituloNuevo.style.display = 'block';
    btnGuardarTituloNuevo.style.display = 'block';
    inputTitulo.focus();

}

function cancelarTitulo() {
    let btnEditarTitulo = document.querySelector('.btn-editar-titulo');
    let btnCancelarTituloNuevo = document.querySelector('.btn-cancelar-titulo');
    let btnGuardarTituloNuevo = document.querySelector('.btn-guardar-titulo');
    let outputTitulo = document.querySelector('.detalles-titulo');
    let inputTitulo = document.querySelector('.nuevo-titulo');

    let divCancelOk = document.querySelector('.cancel-ok-titulo');

    divCancelOk.style.display = 'none';

    btnEditarTitulo.style.display = 'block';
    outputTitulo.style.display = 'block';
    inputTitulo.style.display = 'none';

    btnCancelarTituloNuevo.style.display = 'none';
    btnGuardarTituloNuevo.style.display = 'none';
}


function guardarTitulo() {
    let btnEditarTitulo = document.querySelector('.btn-editar-titulo');
    let btnCancelarTituloNuevo = document.querySelector('.btn-cancelar-titulo');
    let btnGuardarTituloNuevo = document.querySelector('.btn-guardar-titulo');
    let outputTitulo = document.querySelector('.detalles-titulo');
    let inputTitulo = document.querySelector('.nuevo-titulo');

    let divCancelOk = document.querySelector('.cancel-ok-titulo');

    let hiddenCallBacking = document.querySelector('.editar-titulo-backing');

    divCancelOk.style.display = 'none';

    btnEditarTitulo.style.display = 'block';
    outputTitulo.style.display = 'block';
    inputTitulo.style.display = 'none';

    btnCancelarTituloNuevo.style.display = 'none';
    btnGuardarTituloNuevo.style.display = 'none';

    hiddenCallBacking.click();
}



document.addEventListener('DOMContentLoaded', function() {
    const valoracionUsuario = parseInt(document.querySelector('.detalles-valoracion').getAttribute('data-valoracion-usuario') || 0);

    // Inicializa el estado de las estrellas basado en la valoración actual
    resetStars(valoracionUsuario);

    // Resalta las estrellas en el hover
    function highlightStars(element) {
        if (valoracionUsuario === 0) { // Solo aplica el hover si la receta no ha sido valorada
            const value = parseInt(element.getAttribute('data-value'));
            const stars = document.querySelectorAll('.detalles-valoracion i');
            stars.forEach(star => {
                if (parseInt(star.getAttribute('data-value')) <= value) {
                    star.classList.add('highlight');
                } else {
                    star.classList.remove('highlight');
                }
            });
        }
    }

    // Restablece las estrellas al salir del hover
    function resetStars(rating = valoracionUsuario) {
        const stars = document.querySelectorAll('.detalles-valoracion i');
        stars.forEach(star => {
            if (parseInt(star.getAttribute('data-value')) <= rating) {
                star.classList.add('filled');
            } else {
                star.classList.remove('highlight');
            }
        });
    }

    // Maneja el clic en las estrellas
    function submitRating(element) {
        if (valoracionUsuario === 0) { // Solo se puede valorar si la receta no ha sido valorada
            const value = parseInt(element.getAttribute('data-value'));
            // Actualiza la valoración del usuario
            resetStars(value);
            // Aquí puedes hacer una solicitud AJAX para enviar la valoración o usar un formulario
        }
    }

    // Agrega eventos a las estrellas
    document.querySelectorAll('.detalles-valoracion i').forEach(star => {
        star.addEventListener('mouseover', () => highlightStars(star));
        star.addEventListener('mouseout', () => resetStars());
        star.addEventListener('click', () => submitRating(star));
    });
});



document.addEventListener('DOMContentLoaded', function() {
    let mensajeDiv = document.querySelector('.valoracion-mensaje');
    let detallesValoracion = document.querySelector('.detalles-valoracion');
    let yaValorado = detallesValoracion.getAttribute('data-ya-valorado') === 'true';

    if (yaValorado) {
        mensajeDiv.style.display = 'block';  // Muestra el mensaje
        setTimeout(function() {
            mensajeDiv.style.display = 'none';  // Oculta el mensaje después de 3 segundos
        }, 3000);
    }
});



//Parte para editar la cantidad de ingredientes

// Obtener referencias a los elementos
let outputIngredientes = document.querySelector('.valores-cantidad');
let textareaIngredientes = document.querySelector('.text-area-ingredientes');
let editarIngredientesBtn = document.querySelector('.btn-editar-ingredientes');
let guardarIngredientesBtn = document.querySelector('.btn-guardar-ingredientes');
let cancelarEdicionBtn = document.querySelector('.btn-cancelar-ingredientes');
let divGuardarCancelar = document.querySelector('.cancel-ok-ingredientes');

// Función para mostrar el textarea y ocultar el outputText
function editarIngredientes() {
    // Obtener el texto actual del outputText y pasarlo al textarea
    textareaIngredientes.value = outputIngredientes.innerText.replace(/<br\s*[\/]?>/gi, "\n");

    // Ocultar el outputText y el botón de editar, mostrar el textarea y los botones de guardar/cancelar
    outputIngredientes.style.display = 'none';
    editarIngredientesBtn.style.display = 'none';
    textareaIngredientes.style.display = 'block';
    divGuardarCancelar.style.display = 'flex';
    cancelarEdicionBtn.style.display = 'block';
    guardarIngredientesBtn.style.display = 'block';

// Ajustar tamaño de los textareas y agregar el evento de entrada
    document.querySelectorAll('.text-area-ingredientes').forEach(textareaIngredientes => {
        autoResizeTextAreaIngredientes(textareaIngredientes);
        textareaIngredientes.addEventListener('input', function () {
            autoResizeTextAreaIngredientes(this);
        });
    });

}

// Función para guardar los cambios y volver a la vista normal
function guardarIngredientes() {
    let botonOcultoEditarIngredientes = document.querySelector('.btn-guardar-ingrediente');
    // Obtener el texto del textarea y formatearlo como HTML
    let ingredientesEditados = textareaIngredientes.value.replace(/\n/g, "<br>");
    // Actualizar el valor del outputText
    outputIngredientes.innerHTML = ingredientesEditados;
    // Ocultar el textarea y mostrar el outputText y el botón de editar
    textareaIngredientes.style.display = 'none';
    divGuardarCancelar.style.display = 'none';
    outputIngredientes.style.display = 'block';
    editarIngredientesBtn.style.display = 'inline';

    // Aquí puedes agregar la lógica para guardar los cambios en el servidor si es necesario
    actualizarIngredientesBackend(ingredientesEditados.replace(/<br\s*[\/]?>/gi, "\r\n"));

    botonOcultoEditarIngredientes.click();
}

// Función para enviar los ingredientes actualizados al backend
function actualizarIngredientesBackend(ingredientes) {
    // Asignar directamente el valor de 'ingredientes' al input oculto
    document.getElementById("inputOcultoIngredientes").value = ingredientes;
}

// Función para cancelar la edición y restaurar la vista original
function cancelarEdicion() {
    // Ocultar el textarea y mostrar el outputText y el botón de editar
    textareaIngredientes.style.display = 'none';
    divGuardarCancelar.style.display = 'none';
    outputIngredientes.style.display = 'block';
    editarIngredientesBtn.style.display = 'inline';
}

// Asociar las funciones a los botones correspondientes
editarIngredientesBtn.addEventListener('click', editarIngredientes);
guardarIngredientesBtn.addEventListener('click', guardarIngredientes);
cancelarEdicionBtn.addEventListener('click', cancelarEdicion);


function autoResizeTextAreaIngredientes(textareaIngredientes) {
    // Restablecer la altura a 0 para que el scrollHeight pueda medir la altura total del contenido
    textareaIngredientes.style.height = '0';
    // Ajustar la altura del textarea según el contenido
    textareaIngredientes.style.height = textareaIngredientes.scrollHeight + 'px';
}



function confirmarEliminar(event) {
    event.preventDefault(); // Prevenir el comportamiento predeterminado del enlace
    if (confirm('¿Estás seguro de que quieres eliminar esta receta?')) {
        document.querySelector('.boton-eliminar-receta').click(); // Simular el clic en el botón oculto
    }
}

function manejarResultadoEliminarReceta(event) {
    if (event.status === "success") {
        let resultadoElement = document.querySelector('.resultado-eliminar');
        if (resultadoElement) {
            let resultado = resultadoElement.textContent.trim();
            console.log("Resultado: " + resultado);

            if (resultado === "favoritos") {
                Swal.fire({
                    title: 'Error',
                    text: 'No puedes eliminar esta receta porque está en tu lista de favoritas.',
                    icon: 'error',
                    confirmButtonText: 'Aceptar'
                });
            } else if (resultado === "exito") {
                Swal.fire({
                    title: 'Éxito',
                    text: 'Receta eliminada exitosamente.',
                    icon: 'success',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    window.location.href = 'index.xhtml'; // Redirigir a la página de inicio u otra página
                });
            } else if (resultado === "error") {
                Swal.fire({
                    title: 'Error',
                    text: 'Ocurrió un error al intentar eliminar la receta.',
                    icon: 'error',
                    confirmButtonText: 'Aceptar'
                });
            }
        } else {
            console.error("No se encontró el elemento con la clase 'resultado-eliminar'.");
        }
    }
}
