// Agregar script para ocultar el contenedor después de 10 segundos
function ocultarMensajeDeError() {
    setTimeout(function () {
        document.getElementById('error-container').style.display = 'none';
    }, 10000) // 10 segs
}


