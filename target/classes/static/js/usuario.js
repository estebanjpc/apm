function eliminarUsuarioEmpresa(id,idEmpresa) {
	const url = `${location.origin}/deleteUsuarioEmpresa/${id}/${idEmpresa}`;
	Swal.fire({
		title: 'Eliminar Usuario?',
		text: "Esta seguro de eliminar Usuario, se perdera toda la información",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#dc3545',
		confirmButtonText: 'Eliminar'
	}).then((result) => {
		if (result.isConfirmed) {
			showDiv(true);
			window.location = url;
		}else 
			return false;
	});
}