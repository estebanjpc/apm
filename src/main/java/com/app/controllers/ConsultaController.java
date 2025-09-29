package com.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Empresa;
import com.app.entity.Liquidacion;
import com.app.entity.PdfLiquidacion;
import com.app.entity.Usuario;
import com.app.service.IEmailService;
import com.app.service.IEmpresaService;
import com.app.service.ILiquidacionService;
import com.app.service.IPdfLiquidacionService;


@Controller
public class ConsultaController {
	
	@Autowired
	private ILiquidacionService liquidacionService;
	
	@Autowired
	private IEmpresaService empresaService;
	
	@Autowired
	private IPdfLiquidacionService pdfLiquidacion;
	
	@Autowired
	private IEmailService emailService;

	@GetMapping("/consulta")
	public String crear(Map<String, Object> model,
						@RequestParam(value = "empresaId", required = false) Long empresaId,
						HttpServletRequest request) {
		
		model.put("titulo", "Consulta Rut");
		
		Usuario usuarioLogin = (Usuario) request.getSession().getAttribute("usuarioLogin");
		List<Empresa> listadoEmpresas = empresaService.findAllById(usuarioLogin.getId());
		List<Liquidacion> listadoLiquidacion = liquidacionService.findLiquidacionByUsuario(usuarioLogin.getId(),empresaId,null);
		
		model.put("listadoEmpresas", listadoEmpresas);
		model.put("listadoLiquidacion", listadoLiquidacion);
		model.put("empresaId", empresaId);
		
		return "consulta";
	}
	
	
	@RequestMapping(value = {"/enviarPdfEmail/{id}"})
	public String enviarPdfEmail(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash,Authentication authentication,HttpServletRequest request) {
		
		
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogin");
		PdfLiquidacion liq = pdfLiquidacion.findById(id);
		byte[] pdfBytes = liq.getFileBlob();
		
		Executors.newSingleThreadExecutor().execute(() -> emailService.enviarPdfConAdjunto(usuario, pdfBytes));
		
	    flash.addFlashAttribute("success", "Se envió la liquidación al correo: " + usuario.getEmail());
		
	    return "redirect:/consulta";
	}
	
	

	
}
