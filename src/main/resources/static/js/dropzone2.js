// Obtener CSRF token
let csrfToken  = document.querySelector("meta[name='_csrf']").getAttribute("content");
let csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

// Inicializar Dropzone manualmente
let myDropzone = new Dropzone("#dropzoneDiv", {
    url: "/cargarLiquidaciones",
    paramName: "file",
    autoProcessQueue: false,     // No subir automáticamente
    uploadMultiple: false,
    parallelUploads: 1,          // Subir de a uno
    acceptedFiles: ".pdf",
    maxFilesize: 20,             // MB
    addRemoveLinks: true,        // Permite quitar archivos antes de subir

    init: function () {
        let dz = this;

        // Agregar CSRF a cada request
        dz.on("sending", function(file, xhr, formData) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        });

        dz.on("success", function(file, response) {
            console.log("Archivo subido OK:", file.name, response);
        });

        dz.on("error", function(file, errorMessage) {
            console.error("Error al subir:", file.name, errorMessage);
        });
    }
});

// Botón Aceptar dispara subida de toda la cola
document.getElementById("btnAceptar").addEventListener("click", function () {
    if (myDropzone.getQueuedFiles().length > 0) {
        myDropzone.processQueue(); // Empieza a subir todos los archivos, de a uno
    } else {
        alert("No hay archivos para subir");
    }
});
