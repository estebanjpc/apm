function eliminarEmpresa(id, total) {
    const url = `${location.origin}/eliminarEmpresa/${id}`;

    let mensaje = `<p class="mb-2">¿Estás seguro que deseas eliminar esta empresa?</p>`;
    let icono = "warning";

    if (total > 0) {
        mensaje = `
            <p class="mb-2">⚠️ Esta empresa tiene <strong>${total}</strong> usuario(s) asociado(s).</p>
            <p class="small text-muted">
                Al eliminarla también se borrarán todos los usuarios e información vinculada.
            </p>
        `;
        icono = "error";
    }

    Swal.fire({
        title: '<h4 class="fw-bold text-danger"><i class="fas fa-trash-alt"></i> Eliminar Empresa</h4>',
        html: mensaje,
        icon: icono,
        iconColor: icono === "error" ? "#dc3545" : "#ffc107",
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