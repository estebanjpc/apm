package com.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.editors.RolesEditor;
import com.app.entity.Role;
import com.app.entity.Usuario;
import com.app.service.IEmailService;
import com.app.service.IRoleService;
import com.app.service.IUsuarioService;
import com.app.util.Util;


@Controller
@SessionAttributes("usuario")
public class UsuarioAdminController {

	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private RolesEditor roleEditor;
	
	@Autowired
	private IEmailService emailService;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Role.class, "roles",roleEditor);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = {"/listadoUsuarios"})
	public String mantenedorUsuario(Model model) {
		List<Usuario> listadoUsuarios = usuarioService.findAll();
		model.addAttribute("titulo", "Mantenedor Usuarios");
		model.addAttribute("listadoUsuarios",listadoUsuarios);
		return "listadoUsuarios";
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = {"/editarUsuario/{id}"})
	public String editarUsuario(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Usuario usuario = usuarioService.findById(id);
		if(usuario == null) {
			model.put("msjLayout","error;Usuario no existe;Usuario no existe en BD");
			return "listadoUsuarios";
		}
		usuario.setEstado(usuario.getEnabled() ? "1" : "2");
		model.put("titulo", "Mantenedor Usuario");
		model.put("usuario",usuario);
		model.put("btn","Actualizar");
		return "usuario";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = {"/crearUsuario"})
	public String crearUsuario(Map<String, Object> model) {
		Usuario usuario = new Usuario();
		model.put("titulo", "Mantenedor Usuario");
		model.put("usuario",usuario);
		model.put("btn","Crear");
		return "usuario";
	}
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/guardarUsuario")
	public String guardarUsuario(@Valid Usuario usuario, BindingResult result, Map<String, Object> model,
			RedirectAttributes flash, SessionStatus status) {

		model.put("titulo", "Mantenedor Usuario");
		model.put("btn", "Actualizar");
		
		String msje = "Editado con Exito";
		String pass = new String();
		boolean flag = false;
		
		if(usuario.getId() == null) {
			flag = true;
			pass = Util.generatePassword(10);
			usuario.setPassword(passEncoder.encode(pass));
			msje = "Creado con Exito";
			model.put("btn", "Crear");
		}
		
		if (result.hasErrors()) return "usuario";
		
		Usuario aux = usuarioService.findByEmail(usuario.getEmail());
		if(aux != null && usuario.getId() != aux.getId()) {
			model.put("msjLayout","error;Usuario Duplicado;Ya existe Usuario con estos datos");
			return "usuario";				
		}
		
		
		usuario.setEnabled((usuario.getEstado().equalsIgnoreCase("1")) ? true : false);
		if(flag) usuario.setEstado("0");
		usuarioService.save(usuario);
		status.setComplete();
		flash.addFlashAttribute("msjLayout","success;"+msje+"!;Usuario "+msje);
		
		if(flag) {			
			usuario.setPassAux(pass);
			Executors.newSingleThreadExecutor().execute(() -> emailService.creacionUsuario(usuario));	
		}
		
		return "redirect:listadoUsuarios";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/deleteUsuario/{id}")
	public String eliminar(@PathVariable(value = "id") Long id,RedirectAttributes flash) {	
		
		if(id > 0) {
			Usuario usuario = usuarioService.findById(id);
			if(null != usuario) {
				usuarioService.delete(usuario);
				flash.addFlashAttribute("msjLayout","success;Exito!;Usuario elimiando correctamente");
			}else {
				flash.addFlashAttribute("msjLayout","error;Usuario no existe!;Usuario ingresado no existe en BD");
			}
		}
		
		return "redirect:/listadoUsuarios";
	}
	
	@ModelAttribute("estados")
	public Map<String,String>estados(){
		Map<String,String> estados = new HashMap<String,String>();
		estados.put("", "Seleccione");
		estados.put("1", "Activo");
		estados.put("2", "Desactivo");
		return estados;
	}
	
	@ModelAttribute("listadoRoles")
	public List<Role>listadoRoles(){
		return roleService.listar();
	}
	
}
