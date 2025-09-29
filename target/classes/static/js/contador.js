function eliminarContador(id) {
	const url = `${location.origin}/deleteContador/${id}`;
	Swal.fire({
		title: 'Eliminar Contador?',
		text: "Esta seguro de eliminar Contador, se perdera toda la información",
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