// Obtener CSRF token
let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
let csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

// Array para acumular resultados de cada archivo
let resultados = [];

// Inicializar Dropzone manualmente
let myDropzone = new Dropzone("#dropzoneDiv", {
	url: "/cargarLiquidaciones",
	paramName: "file",
	autoProcessQueue: false,     // No subir automáticamente
	uploadMultiple: false,
	parallelUploads: 1,          // Subir de a uno
	acceptedFiles: ".pdf",
	maxFilesize: 20,             // MB
	addRemoveLinks: true,

	init: function() {
		let dz = this;

		// Agregar CSRF a cada request
		dz.on("sending", function(file, xhr, formData) {
			xhr.setRequestHeader(csrfHeader, csrfToken);

			let empresaId = document.getElementById("empresaId").value;
			let fecha = document.getElementById("fecha").value;

			formData.append("empresaId", empresaId);
			formData.append("fecha", fecha);
		});

		// Success
		dz.on("success", function(file, response) {
			console.log("Archivo subido OK:", file.name, response);

			resultados.push({
				file: file.name,
				status: response.status,
				message: response.message
			});

			if (dz.getQueuedFiles().length > 0) {
				dz.processQueue();
			}
		});

		// Error
		dz.on("error", function(file, errorMessage) {
			console.error("Error al subir:", file.name, errorMessage);

			resultados.push({
				file: file.name,
				status: "error",
				message: (typeof errorMessage === "string" ? errorMessage : "Error desconocido")
			});

			if (dz.getQueuedFiles().length > 0) {
				dz.processQueue();
			}
		});

		// Cuando ya no quedan archivos en cola
		dz.on("queuecomplete", function() {
			let exitos = resultados.filter(r => r.status === "ok");
			let errores = resultados.filter(r => r.status === "error");

			// Caso 1: Todos los archivos se subieron correctamente
			if (exitos.length === resultados.length && exitos.length > 0) {
				Swal.fire({
					title: "¡Éxito!",
					text: `${exitos.length} archivo(s) cargado(s) correctamente.`,
					icon: "success",
					confirmButtonColor: '#0d6efd'
				});
			} else {
				// Caso 2: Hay errores o no se procesaron archivos
				let html = "";

				if (exitos.length > 0) {
					html += "<h6 class='text-success'>✅ Subidos correctamente:</h6><ul>";
					exitos.forEach(r => {
						html += "<li>" + r.file + "</li>";
					});
					html += "</ul>";
				}

				if (errores.length > 0) {
					html += "<h6 class='text-danger mt-3'>❌ Errores en:</h6><ul>";
					errores.forEach(r => {
						html += "<li><strong>" + r.file + "</strong>: " + r.message + "</li>";
					});
					html += "</ul>";
				}

				if (exitos.length === 0 && errores.length === 0) {
					html = "<p class='text-muted'>No se procesaron archivos.</p>";
				}

				// Actualizar título con contador
				let titulo = "📂 Resumen de subida";
				if (exitos.length + errores.length > 0) {
					titulo += ` — ${exitos.length} correctos / ${errores.length} con error`;
				}
				document.getElementById("modalResumenLabel").innerText = titulo;

				// Insertar contenido en el modal
				document.getElementById("resumenContent").innerHTML = html;

				// Mostrar modal
				let modal = new bootstrap.Modal(document.getElementById("modalResumen"));
				modal.show();
			}

			// Limpiar resultados y archivos
			resultados = [];
			dz.removeAllFiles(true);
		});
	}
});

// Botón Aceptar dispara subida de toda la cola
document.getElementById("btnAceptar").addEventListener("click", function() {
	let empresaId = document.getElementById("empresaId").value;
	let fecha = document.getElementById("fecha").value;
	if (!empresaId) {
		alert("⚠️ Debe seleccionar una empresa antes de subir archivos.");
		return;
	}

	if (!fecha || fecha == '') {
		alert("⚠️ Debe seleccionar una fecha antes de subir archivos.");
		return;
	}

	if (myDropzone.getQueuedFiles().length > 0) {
		myDropzone.processQueue(); // Empieza a subir de a uno y se encadena
	} else {
		alert("No hay archivos para subir");
	}
});
