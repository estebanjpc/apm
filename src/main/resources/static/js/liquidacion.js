function verPdfsEP(id) {
    const url = `${location.origin}/verLiquidacionPdf/${id}`;
    $.ajax({
        url: url,
        type: 'GET',
        async: false,
        dataType: 'json',
        success: function (data) {
            showDiv(false);
            if (data.estado == 'OK') {
                let binaryString = window.atob(data.pdf);
                let binaryLen = binaryString.length;
                let bytes = new Uint8Array(binaryLen);
                for (let i = 0; i < binaryLen; i++) {
                    let ascii = binaryString.charCodeAt(i);
                    bytes[i] = ascii;
                }

                let blob = new Blob([bytes], { type: "application/pdf" });
                let urlPdf = URL.createObjectURL(blob);
                document.getElementById("idDivModalEP").src = urlPdf;
                $("#viewPdfModalEP").modal('show');
            } else {
                Swal.fire({
                    title: '<h4 class="fw-bold text-info"><i class="fas fa-file-pdf"></i> Sin PDF</h4>',
                    html: `<p class="mb-2">${data.mensaje}</p>`,
                    icon: 'info',
                    iconColor: '#0d6efd',
                    confirmButtonText: '<i class="fas fa-check"></i> Aceptar',
                    confirmButtonColor: '#0d6efd',
                    background: '#f8f9fa',
                    backdrop: `rgba(0,0,0,0.5)`,
                    showClass: {
                        popup: 'animate__animated animate__fadeInDown'
                    },
                    hideClass: {
                        popup: 'animate__animated animate__fadeOutUp'
                    }
                });
            }
        }
    });
}


// Verificación segura para el campo fecha
const fechaInput = document.getElementById('fecha');
if (fechaInput) {
	fechaInput.addEventListener('click', function() {
		this.showPicker(); // Forza la apertura del selector de fecha
	});
}

function eliminarLiquidacion(id) {
    const url = `${location.origin}/eliminarLiquidacion/${id}`;
    Swal.fire({
        title: '<h4 class="fw-bold text-danger"><i class="fas fa-trash-alt"></i> Eliminar Liquidación</h4>',
        html: `<p class="mb-2">¿Estás seguro que deseas eliminar esta liquidación?</p>`,
        icon: 'warning',
        iconColor: '#ffc107',
        showCancelButton: true,
        confirmButtonText: '<i class="fas fa-check"></i> Sí, eliminar',
        cancelButtonText: '<i class="fas fa-times"></i> Cancelar',
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        reverseButtons: true,
        background: '#f8f9fa',
        backdrop: `rgba(0,0,0,0.5)`,
        showClass: {
            popup: 'animate__animated animate__fadeInDown'
        },
        hideClass: {
            popup: 'animate__animated animate__fadeOutUp'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            showDiv(true);
            window.location = url;
        }
    });
}
