package com.app.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Empresa;
import com.app.entity.EmpresaUsuarios;
import com.app.entity.Liquidacion;
import com.app.entity.PdfLiquidacion;
import com.app.entity.Usuario;
import com.app.service.IEmpresaService;
import com.app.service.IEmpresaUsuariosService;
import com.app.service.ILiquidacionService;
import com.app.service.IPdfLiquidacionService;
import com.app.service.IUsuarioService;
import com.app.vo.Response;

@Controller
public class LiquidacionController {

	@Autowired
	private IEmpresaService empresaService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ILiquidacionService liquidacionService;

	@Autowired
	private IPdfLiquidacionService pdfLiquidacion;
	
	@Autowired
	private IEmpresaUsuariosService empresaUsuarioService;

	@GetMapping("/cargarLiquidaciones")
	public String cargarLiquidaciones(Map<String, Object> model, HttpServletRequest request) {

		Usuario usuarioLogin = (Usuario) request.getSession().getAttribute("usuarioLogin");
		List<Empresa> listadoEmpresas = empresaService.findAllById(usuarioLogin.getId());

		model.put("titulo", "Cargar Liquidaciones");
		model.put("listadoEmpresas", listadoEmpresas);
		model.put("hoy", LocalDate.now());

		return "cargarLiquidacion";
	}

	@PostMapping(value = "/cargarLiquidaciones", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> cargarLiquidacion(@RequestParam("file") MultipartFile archivo,
			@RequestParam("empresaId") Long empresaId,
			@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM") YearMonth fecha) {

		Map<String, Object> response = new HashMap<>();

		LocalDate primerDia = fecha.atDay(1);

		if (empresaId == null || empresaId <= 0) {
			response.put("status", "error");
			response.put("message", "Debe seleccionar una empresa válida.");
			return response;
		}

		if (primerDia == null) {
			response.put("status", "error");
			response.put("message", "Debe seleccionar una fecha válida.");
			return response;
		}

		if (!archivo.isEmpty()) {
			System.out.println("Subido: " + archivo.getOriginalFilename());
			
			boolean flag = false;

			try {
				String aux[] = archivo.getOriginalFilename().split("\\.");
				Usuario usuario = usuarioService.findByRut(aux[0].trim());
				Empresa empresa = empresaService.findById(empresaId);
				EmpresaUsuarios eu = empresaUsuarioService.findByEmpresaAndUsuario(empresa,usuario);
				
				
				if(eu != null) {
					flag = true;
					Liquidacion liquidacion = new Liquidacion(usuario, Date.valueOf(primerDia),archivo.getOriginalFilename(),empresa);
					liquidacionService.save(liquidacion, archivo);					
				}
				
				if(flag) {
					response.put("status", "ok");
					response.put("message", "Archivo " + archivo.getOriginalFilename() + " cargado correctamente!");				
				}else {
					response.put("status", "error");
					response.put("message", "No existe relacion de usuario con empresa seleccionada!");
				}
				
			} catch (Exception e) {
				System.out.println(e);
				response.put("status", "error");
				response.put("message", "Error inesperado: " + e.getMessage());
			}
			
		} else {
			response.put("status", "error");
			response.put("message", "Archivo vacío");
		}
		return response;
	}

	@GetMapping("/usuarioLiquidaciones")
	public String usuarioLiquidaciones(
	        @RequestParam(value = "empresaId", required = false) Long idEmpresa,
	        @RequestParam(value = "fecha", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth fecha,
	        Map<String, Object> model,
	        HttpServletRequest request) {

		LocalDate primerDia = null;
		if(fecha != null) {
			primerDia = fecha.atDay(1);
		}
			
			
		Usuario usuarioLogin = (Usuario) request.getSession().getAttribute("usuarioLogin");
		List<Empresa> listadoEmpresas = empresaService.findAllById(usuarioLogin.getId());
		List<Liquidacion> listadoLiquidacion = liquidacionService.findLiquidacionByFiltro(usuarioLogin.getId(),idEmpresa,primerDia);

		model.put("titulo", "Liquidaciones Usuarios");
		model.put("listadoEmpresas", listadoEmpresas);
		model.put("listadoLiquidacion", listadoLiquidacion);
//		model.put("hoy", LocalDate.now());
		model.put("empresaId", idEmpresa);
	    model.put("fecha", fecha);

		return "usuarioLiquidacion";
	}

	@GetMapping("/verLiquidacionPdf/{id}")
	@ResponseBody
	public ResponseEntity<?> verLiquidacionPdf(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {
		Response resp = new Response();
		try {

			PdfLiquidacion liq = pdfLiquidacion.findById(id);
			String pdf = Base64.getEncoder().encodeToString(liq.getFileBlob());
			resp.setEstado("OK");
			resp.setPdf(pdf);
		} catch (Exception e) {
			resp.setEstado("NOK");
			resp.setMensaje(e.getMessage());
		}
		return new ResponseEntity<Response>(resp, HttpStatus.OK);
	}
	
	@Secured("ROLE_CONTADOR")
	@RequestMapping(value = "/eliminarLiquidacion/{id}")
	public String eliminarLiquidacion(@PathVariable(value = "id") Long id,Map<String, Object> model,RedirectAttributes flash,HttpServletRequest request) {	
		
		if(id > 0) {
			
			Liquidacion liquidacion = liquidacionService.findById(id);
			
//			if (liquidacion == null) {
//				model.put("msjLayout", "error;Sin Privilegios;No puede eliminar usuarios que no ha creado");
//				return "listadoUsuariosEmpresa";
//			}
			
			if (liquidacion == null) {
				model.put("msjLayout", "error;No existe registro;no existe registro de liquidacion en bd");
				return "usuarioLiquidacion";
			}
			
			liquidacionService.delete(liquidacion);
			flash.addFlashAttribute("msjLayout","success;Exito!;Liquidacion elimianda correctamente");
		}
		
		return "redirect:/usuarioLiquidaciones";
	}

}
