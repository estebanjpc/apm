package com.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Empresa;
import com.app.entity.EmpresaUsuarios;
import com.app.entity.Usuario;
import com.app.service.IEmpresaService;
import com.app.service.IEmpresaUsuariosService;
import com.app.service.ILiquidacionService;
import com.app.service.IUsuarioService;

@Controller
@SessionAttributes("empresa")
public class EmpresaController {

	@Autowired
	private IEmpresaService empresaService;
	
	@Autowired
	private ILiquidacionService liquidacionService;
	
	@Autowired
	private IEmpresaUsuariosService empresaUsuarioService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	
	@RequestMapping(value = {"/listadoEmpresas"})
	public String listadoEmpresas(Model model,RedirectAttributes flash,Authentication authentication,HttpServletRequest request) {
		
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogin");
		List<Empresa> listadoEmpresas = empresaService.findAllById(usuario.getId());
		
		model.addAttribute("titulo", "Mantenedor Empresas");
		model.addAttribute("listadoEmpresas",listadoEmpresas);
		return "listadoEmpresas";
	}
	
	@RequestMapping(value = {"/crearEmpresa"})
	public String crearEmpresa(Map<String, Object> model,RedirectAttributes flash,Authentication authentication,HttpServletRequest request) {
		
		Empresa empresa= new Empresa();
		model.put("titulo", "Mantenedor Empresa");
		model.put("empresa",empresa);
		model.put("btn","Crear");
		return "empresa";
	}
	
	@PostMapping("/guardarEmpresa")
	public String guardarEmpresa(@Valid Empresa empresa, BindingResult result, Map<String, Object> model,
			RedirectAttributes flash, SessionStatus status,HttpServletRequest request) {

		model.put("titulo", "Mantenedor Empresa");
		model.put("btn", "Actualizar");
		
		String msje = "Editado con Exito";
		
		if(empresa.getId() == null) {
			msje = "Creado con Exito";
			model.put("btn", "Crear");
		}
		
		if (result.hasErrors()) return "empresa";
		
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogin");
		empresa.setUsuario(usuario);
		empresaService.save(empresa);
		status.setComplete();
		flash.addFlashAttribute("msjLayout","success;"+msje+"!;Empresa "+msje);
		
		
		return "redirect:listadoEmpresas";
	}
	
	@ModelAttribute("estados")
	public Map<String,String>estados(){
		Map<String,String> estados = new HashMap<String,String>();
		estados.put("", "Seleccione");
		estados.put("1", "Activo");
		estados.put("2", "Desactivo");
		return estados;
	}
	
	@RequestMapping(value = {"/editarEmpresa/{id}"})
	public String editarEmpresa(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash,Authentication authentication,HttpServletRequest request) {
		
		Empresa empresa = empresaService.findById(id);
		
		if(null == empresa) {
			flash.addFlashAttribute("msjLayout","error;Empresano existe;Empresa no existe en BD");
			return "redirect:/listadoEmpresas";
		}
		
		model.put("titulo", "Mantenedor Empresa");
		model.put("empresa",empresa);
		model.put("btn","Actualizar");
		return "empresa";
	}
	
	@RequestMapping(value = {"/listarUsuariosEmpresa/{id}"})
	public String listarUsuariosEmpresa(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash,Authentication authentication,HttpServletRequest request) {
		
		Empresa empresa = empresaService.findById(id);
		
		if(null == empresa) {
			flash.addFlashAttribute("msjLayout","error;Empresano existe;Empresa no existe en BD");
			return "redirect:/listadoEmpresas";
		}
		
		model.put("titulo", "Mantenedor Empresa");
		model.put("empresa",empresa);
		model.put("btn","Actualizar");
		return "empresa";
	}
	
	@RequestMapping(value = {"/eliminarEmpresa/{id}"})
	public String eliminarEmpresa(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash,Authentication authentication,HttpServletRequest request) {
		
		Empresa empresa = empresaService.findById(id);
		
		if(null == empresa) {
			flash.addFlashAttribute("msjLayout","error;Empresano existe;Empresa no existe en BD");
			return "redirect:/listadoEmpresas";
		}
		
		// DELETE
		if(empresa.getUsuariosAsociados().size() > 0) {
			for (EmpresaUsuarios eu : empresa.getUsuariosAsociados()) {
				liquidacionService.deleteByUsuarioEmpresa(eu.getUsuario().getId(),id);
				List<EmpresaUsuarios> lista = empresaUsuarioService.findByUsuario(eu.getUsuario());
				if(lista.size() == 1) usuarioService.delete(eu.getUsuario());
				else empresaUsuarioService.deleteByUsuarioAndEmpresa(eu.getUsuario().getId(),id);				
			}
		}
		
		empresaService.deleteById(id);
		
		
		
		return "redirect:/listadoEmpresas";
	}
}
