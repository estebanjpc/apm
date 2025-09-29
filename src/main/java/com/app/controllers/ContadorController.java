package com.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Email;
import com.app.entity.Usuario;
import com.app.service.IEmailService;
import com.app.service.IUsuarioService;
import com.app.util.Util;

@Controller
public class ContadorController {

	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private IEmailService emailService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Value("${mail.set.from}")
	public String emailSetFrom;
	
	@RequestMapping(value = {"/listadoContadores"})
	public String mantenedorUsuario(Model model,RedirectAttributes flash,Authentication authentication,HttpServletRequest request) {
		
		List<Usuario> listadoContadores = null; //usuarioService.findAllByRol("ROLE_CONTADOR");
		
		model.addAttribute("titulo", "Mantenedor Contadores");
		model.addAttribute("listadoContadores",listadoContadores);
		return "listadoContadores";
	}
	
	@RequestMapping(value = {"/crearContador"})
	public String crearContador(Map<String, Object> model,RedirectAttributes flash,Authentication authentication,HttpServletRequest request) {
		
		Usuario contador= new Usuario();
		model.put("titulo", "Mantenedor Contador");
		model.put("contador",contador);
		model.put("btn","Crear");
		return "contador";
	}
	
	@PostMapping("/guardarContador")
	public String guardarContador(@Valid Usuario usuario, BindingResult result, Map<String, Object> model,
			RedirectAttributes flash, SessionStatus status) {

		model.put("titulo", "Mantenedor Contador");
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
		
		if (result.hasErrors()) return "contador";
		
		Usuario aux = usuarioService.findByEmail(usuario.getEmail());
		if(aux != null && usuario.getId() != aux.getId()) {
			model.put("msjLayout","error;Contador Duplicado;Ya existe Contador con estos datos");
			return "contador";				
		}
		
		
		usuario.setEnabled((usuario.getEstado().equalsIgnoreCase("1")) ? true : false);
		if(flag) usuario.setEstado("0");
		usuarioService.save(usuario);
		status.setComplete();
		flash.addFlashAttribute("msjLayout","success;"+msje+"!;Contador "+msje);
		
		if(flag) {			
			usuario.setPassAux(pass);
			Email email = new Email();
			email.setEmailFrom(emailSetFrom);
			email.setEmailTo(usuario.getEmail());
			email.setSubject("Creacion de Usuario Contador");
			email.setBody("Se ha creado correctamente el nuevo usuario: "+ usuario.getEmail() + " con pass: " + usuario.getPassAux());
			Executors.newSingleThreadExecutor().execute(() -> emailService.creacionUsuario(email));	
		}
		
		return "redirect:listadoContadores";
	}
	
	@ModelAttribute("estados")
	public Map<String,String>estados(){
		Map<String,String> estados = new HashMap<String,String>();
		estados.put("", "Seleccione");
		estados.put("1", "Activo");
		estados.put("2", "Desactivo");
		return estados;
	}
}
