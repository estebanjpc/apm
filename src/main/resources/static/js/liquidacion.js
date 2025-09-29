function verPdfsEP(id){
//	showDiv(true);
	const url = `${location.origin}/verLiquidacionPdf/${id}`;
	$.ajax({
				url : url,
				type:'GET',
				async:false,
				dataType : 'json',
				success: function (data){
					showDiv(false);
					if(data.estado == 'OK'){
						let binaryString = window.atob(data.pdf);
						let binaryLen = binaryString.length;
						let bytes = new Uint8Array(binaryLen);
						for (let i = 0; i < binaryLen; i++) {
						    let ascii = binaryString.charCodeAt(i);
						    bytes[i] = ascii;
						}
						
						let blob = new Blob([bytes], {type: "application/pdf"});
						let urlPdf = URL.createObjectURL(blob);
						document.getElementById("idDivModalEP").src = urlPdf;
						$("#viewPdfModalEP").modal('show');
					}else{					
						Swal.fire({title: 'Sin PDF', 
					    		   text: data.mensaje, 
					    		   type: 'info',
					    		   icon: 'info',
					    		   confirmButtonColor: '#0d6efd'});
								
					}
				}
			});
			
}

document.getElementById('fecha').addEventListener('click', function() {
        this.showPicker(); // Forza la apertura del selector de fecha
    });

function eliminarLiquidacion(id) {
	const url = `${location.origin}/eliminarLiquidacion/${id}`;
	Swal.fire({
		title: 'Eliminar Liquidacion?',
		text: "Esta seguro de eliminar Liquidacion",
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