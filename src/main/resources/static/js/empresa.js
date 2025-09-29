function eliminarEmpresa(id,total) {
	const url = `${location.origin}/eliminarEmpresa/${id}`;
	
	let mensaje = `Esta seguro de eliminar la Empresa?`;
	
	if (total > 0) {
		mensaje = `Esta empresa tiene ${total} usuario(s) asociado(s). 
		Al eliminarla también se eliminarán todos los usuarios e información vinculada`;
	}
	
	Swal.fire({
		title: 'Eliminar Empresa?',
		text: mensaje,
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