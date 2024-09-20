function cerrarSesion() {
    console.log("se entro al metodo cerrar sesion del index")
    let boton = document.querySelector('.boton-cerrar-sesion');
    boton.click();

}

let modal = document.getElementById("modalAgregarReceta");
let btnAbrirModal = document.getElementById("btnAbrirModalAgregarReceta");
// let spanCerrarModal = document.getElementsByClassName("close")[0];
let inputOculto = document.getElementById("inputOculto");
let botonGuardarReceta = document.querySelector('.btn-guardar-receta');
let pasosPanel = document.getElementById("pasosPanel");

// Obtener los botones
let btnAgregarTiempo = document.getElementById('btnAgregarTiempo');
let btnQuitarTiempo = document.getElementById('btnQuitarTiempo');



// Función para abrir el modal
function abrirModal() {
    modal.style.display = "block";
}


// Función para cerrar el modal
function cerrarModal() {
    modal.style.display = "none";
}

// Función para cerrar el modal haciendo clic fuera de él
window.addEventListener("click", function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
});


/*
-----------------------------------------------------------------------------------------------------------------------------------------
                                                          MODAL
-----------------------------------------------------------------------------------------------------------------------------------------
 */

// VALIDADOR DE QUE SE COMPLETEN LOS CAMPOS

// Función de validación del formulario
function validarFormulario(event) {
    let inputNombreReceta = document.querySelector('.receta-titulo');
    let mensajeErrorTitulo = document.querySelector('.modal-error-titulo');
    let inputTextPaso = document.querySelector('.text-area-pasos');
    let mensajeErrorPaso = document.querySelector('.modal-error-paso');
    let mensajeErrorDificultad = document.querySelector('.modal-error-dificultad');
    let selectorDificultad = document.querySelector('.menu-dificultad')
    let selectorCategoria = document.querySelector('.menu-categoria')
    let mensajeErrorCategoria = document.querySelector('.modal-error-categoria')
    let inputTiempoPreparacion = document.querySelector('.input-tiempo-preparacion');
    let mensajeErrorTiempoPreparacion = document.querySelector('.modal-error-tiempo-preparacion');
    let inputTextIngredientes = document.querySelector('.input-ingredientes');
    let mensajeErrorIngredientes = document.querySelector('.modal-error-ingredientes');

    let inputImagen = document.querySelector('.input-imagen');  // Selecciona el campo de entrada de imagen
    let mensajeErrorImagen = document.querySelector('.modal-error-imagen');  // Mensaje de error para la imagen

    // Verificar si el campo titulo está vacío
    if (inputNombreReceta.value.trim() === '') {
        mensajeErrorTitulo.textContent = 'Por favor, ingrese el nombre de la receta.';
        inputNombreReceta.classList.add('modal-error-titulo-dinamico');
        inputNombreReceta.focus();
        event.preventDefault();
    } else {
        // Limpiar mensaje de error si el campo no está vacío
        mensajeErrorTitulo.textContent = '';
        // Remover el resaltado del campo requerido
        inputNombreReceta.classList.remove('modal-error-titulo-dinamico');
    }

    //Verifica campo paso esta vacio (con verificar el primero me parece bien):
    if (inputTextPaso.value.trim() === '') {
        mensajeErrorPaso.textContent = 'Por favor, ingrese pasos para la receta.';
        inputTextPaso.classList.add('modal-error-paso-dinamico');
        inputTextPaso.focus();
        event.preventDefault();
    } else {
        mensajeErrorPaso.textContent = '';
        inputTextPaso.classList.remove('modal-error-paso-dinamico');
    }


    //Verifica selector dificultad esta vacio:
    if (selectorDificultad.value === '') {
        mensajeErrorDificultad.textContent = 'Por favor, seleccione la dificultad';
        selectorDificultad.classList.add('modal-error-dificultad-dinamico');
        selectorDificultad.focus();
        event.preventDefault();
    } else {
        mensajeErrorDificultad.textContent = '';
        selectorDificultad.classList.remove('modal-error-dificultad-dinamico');
    }

    // Verificar si el campo categoria está vacío
    if (selectorCategoria.value.trim() === '') {
        mensajeErrorCategoria.textContent = 'Por favor, seleccione una categoria.';
        selectorCategoria.classList.add('modal-error-categoria-dinamico');
        selectorCategoria.focus();
        event.preventDefault();
    } else {
        // Limpiar mensaje de error si el campo no está vacío
        mensajeErrorCategoria.textContent = '';
        // Remover el resaltado del campo requerido
        selectorCategoria.classList.remove('modal-error-categoria-dinamico');
    }

    // Verificar si el campo tiempo_preparacion está vacío
    if (inputTiempoPreparacion.value.trim() === '') {
        mensajeErrorTiempoPreparacion.textContent = 'Por favor, ingrese el tiempo de preparacion estimado.';
        inputTiempoPreparacion.classList.add('modal-error-tiempo-preparacion-dinamico');
        inputTiempoPreparacion.focus();
        event.preventDefault();
    } else {
        // Verificar si el valor ingresado es un número
        const tiempoPreparacion = inputTiempoPreparacion.value.trim();
        if (!(/^\d+$/.test(tiempoPreparacion))) {
            mensajeErrorTiempoPreparacion.textContent = 'Debe ingresar un valor numérico (minutos)';
            inputTiempoPreparacion.classList.add('modal-error-tiempo-preparacion-dinamico');
            inputTiempoPreparacion.focus();
            event.preventDefault();
        } else {
            // Limpiar mensaje de error si el campo no está vacío
            mensajeErrorTiempoPreparacion.textContent = '';
            // Remover el resaltado del campo requerido
            inputTiempoPreparacion.classList.remove('modal-error-tiempo-preparacion-dinamico');
        }
    }


    //Verifica campo ingrediente esta vacio:
    if (inputTextIngredientes.value.trim() === '') {
        mensajeErrorIngredientes.textContent = 'Por favor, ingrese los ingredientes necesarios (separados por coma).';
        inputTextIngredientes.classList.add('modal-error-ingredientes-dinamico');
        inputTextIngredientes.focus();
        event.preventDefault();
    } else {
        mensajeErrorIngredientes.textContent = '';
        inputTextIngredientes.classList.remove('modal-error-ingredientes-dinamico');
    }


    // Verificar si se ha seleccionado una imagen
    if (inputImagen.files.length === 0) {
        mensajeErrorImagen.textContent = 'Por favor, seleccione una imagen para la receta.';
        inputImagen.classList.add('modal-error-imagen-dinamico');
        inputImagen.focus();
        event.preventDefault();
    } else {
        let file = inputImagen.files[0];
        let validImageTypes = ['image/jpeg', 'image/png', 'image/jpg', 'image/gif'];
        if (!validImageTypes.includes(file.type)) {
            mensajeErrorImagen.textContent = 'El archivo seleccionado no es una imagen válida. Seleccione una imagen en formato JPG, PNG o GIF.';
            inputImagen.classList.add('modal-error-imagen-dinamico');
            inputImagen.focus();
            event.preventDefault();
        } else {
            mensajeErrorImagen.textContent = '';
            inputImagen.classList.remove('modal-error-imagen-dinamico');
        }
    }


}


// Agregar evento clic al botón "Guardar receta" para validar el formulario
    // Verificar si el botón existe
    if (botonGuardarReceta) {
        // Agregar evento clic al botón
        botonGuardarReceta.addEventListener('click', validarFormulario);
    }


//Script para los pasos dinamicamente:
let pasoCounter = 1;

function agregarPaso() {
    let btnQuitarPaso = document.querySelector('.btn-quitar-paso');
    pasoCounter++;
    let nuevoPaso = document.createElement("div");
    nuevoPaso.classList.add("paso");
    nuevoPaso.innerHTML = '<label for="paso' + pasoCounter + '" class="label-pasos">Paso ' + pasoCounter + ':</label>' +
        '<textarea id="paso' + pasoCounter + '" class="text-area-pasos" placeholder="Agregar paso..."></textarea>';

    // Agregar evento de cambio al nuevo textarea
    let nuevoTextArea = nuevoPaso.querySelector('.text-area-pasos');
    nuevoTextArea.addEventListener('input', actualizarPasos);

    pasosPanel.appendChild(nuevoPaso);

    // Mostrar el botón de eliminar si hay al menos 2 pasos
    if (pasoCounter > 1) {
        btnQuitarPaso.style.display = 'block';
    }

}


// Agregar evento de clic al botón "Agregar paso"
let agregarPasoButton = document.querySelector('.btn-agregar-paso');
agregarPasoButton.addEventListener("click", function(event) {
    event.preventDefault(); // Evitar comportamiento predeterminado del botón
    agregarPaso(); // Llamar a la función para agregar un nuevo paso
});

//Recorre todos los textarea y los almacena en un array
// Adjuntar evento oninput a todos los textareas
let pasosTextArea = document.querySelectorAll('.text-area-pasos');
pasosTextArea.forEach(function(textArea) {
    textArea.addEventListener('input', actualizarPasos);
});





// Función para actualizar los pasos
function actualizarPasos() {
    // Obtener todos los textareas de pasos
    const pasos = document.querySelectorAll('.text-area-pasos');
    // Obtener el valor de cada textarea y almacenarlo en un array
    const pasosValue = Array.from(pasos).map(textArea => textArea.value);
    // Actualizar el campo oculto con los pasos
    inputOculto.value = pasosValue.join('\n');
}

// Agregar evento de cambio a los textareas de pasos
document.addEventListener('input', function(event) {
    if (event.target.matches('.text-area-pasos')) {
        autoResizeTextArea(event.target);
        actualizarPasos();
    }
});






// Función para redimensionar automáticamente el textarea
function autoResizeTextArea(textarea) {
    // Restablecer la altura a 0 para que el scrollHeight pueda medir la altura total del contenido
    textarea.style.height = '0';
    // Ajustar la altura del textarea según el contenido
    textarea.style.height = textarea.scrollHeight + 'px';
}

// Evento para los textareas existentes
document.querySelectorAll('.text-area-pasos').forEach(function(textarea) {
    textarea.addEventListener('input', function() {
        autoResizeTextArea(this);
    });
});

// Agregar evento para los nuevos textareas creados dinámicamente

// Agregar evento a los nuevos textareas cuando se agreguen
pasosPanel.addEventListener('input', function(event) {
    if (event.target && event.target.matches('.text-area-pasos')) {
        autoResizeTextArea(event.target);
    }
});



//Vista previa de la imagen
function previewImage(event) {
    let input = event.target;
    let reader = new FileReader();
    reader.onload = function(){
        let preview = document.getElementById('previewImagen');
        preview.src = reader.result;
        preview.style.display = 'block'; // Muestra la imagen
    };
    reader.readAsDataURL(input.files[0]);
}


//Aumentar o decrementar tiempo de preparacion:
function incrementarTiempoPreparacion() {
    let campo = document.querySelector('.input-tiempo-preparacion')
    campo.value = parseInt(campo.value || 0) + 1;
}

function decrementarTiempoPreparacion() {
    let campo = document.querySelector('.input-tiempo-preparacion')
    let valor = parseInt(campo.value || 0);
    if (valor > 0) {
        campo.value = valor - 1;
    }
}
// Asignar eventos a los botones
btnAgregarTiempo.addEventListener('click', incrementarTiempoPreparacion);
btnQuitarTiempo.addEventListener('click', decrementarTiempoPreparacion);



function obtenerIngredientes() {
    var ingredientesInput = document.querySelector('.input-ingredientes').value.trim();
    var ingredientesArray = ingredientesInput.split(',').map(function(ingrediente) {
        return { nombre: ingrediente.trim() }; // Puedes incluir más propiedades aquí según tus necesidades
    });
    console.log(ingredientesArray);
}



// Función para actualizar los ingredientes y cantidades
function actualizarIngredientesCantidades() {
    // Obtener el textarea de ingredientes y cantidades
    const textarea = document.querySelector('.text-area-cantidad-ingrediente');
    const inputOcultoIngredientesCantidades = document.querySelector('.inputOcultoIngredientesCantidades');
    // Obtener el valor del textarea
    const textoIngredientesCantidades = textarea.value;
    // Actualizar el campo oculto con los ingredientes y cantidades
    inputOcultoIngredientesCantidades.value = textoIngredientesCantidades;
}



function valorarReceta(valoracion) {
    const estrellas = document.querySelectorAll('.valoracion i');
    estrellas.forEach((estrella, index) => {
        if (index < valoracion) {
            estrella.classList.remove('far');
            estrella.classList.add('fas');
        } else {
            estrella.classList.remove('fas');
            estrella.classList.add('far');
        }
    });
}


let filtroInput = document.querySelector('.text-filtro');
let botonBuscar = document.querySelector('.boton-filtro');

filtroInput.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // Evitar el comportamiento predeterminado del "Enter"
        botonBuscar.click(); // Activar el evento del botón de búsqueda
    }
});



function guardarComoFavorito(icon) {
    const recetaId = icon.getAttribute('data-receta-id');
    console.log(`Icono clicado con receta ID: ${recetaId}`);

    const boton = icon.nextElementSibling;

    if (boton && boton.classList.contains('boton-add-fav')) {
        console.log(`Encontrado botón con receta ID: ${recetaId}`);
        boton.click();
    } else {
        console.error(`No se encontró el botón con receta ID: ${recetaId}`);
    }
}


setTimeout(function() {
    document.querySelector('.main-content').style.display = 'block';
    document.querySelector('.preloader').style.display = 'none';
}, 3000); // Ajusta el tiempo según sea necesario





// Agregar evento de clic al botón "Quitar paso"
let quitarPasoButton = document.querySelector('.btn-quitar-paso');
quitarPasoButton.addEventListener("click", function(event) {
    event.preventDefault(); // Evitar comportamiento predeterminado del botón
    eliminarPaso(); // Llamar a la función para agregar un nuevo paso
});


function eliminarPaso() {
    let btnQuitarPaso = document.querySelector('.btn-quitar-paso');

    if (pasoCounter > 1) {
        pasosPanel.removeChild(pasosPanel.lastChild);
        pasoCounter--;
    }

    // Ocultar el botón de eliminar si solo queda un paso
    if (pasoCounter <= 1) {
        btnQuitarPaso.style.display = 'none';
    }
    actualizarPasos();


}






