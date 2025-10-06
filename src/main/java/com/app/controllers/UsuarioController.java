package com.app.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Empresa;
import com.app.entity.EmpresaUsuarios;
import com.app.entity.Role;
import com.app.entity.Usuario;
import com.app.service.IEmailService;
import com.app.service.IEmpresaService;
import com.app.service.IEmpresaUsuariosService;
import com.app.service.ILiquidacionService;
import com.app.service.IRoleService;
import com.app.service.IUsuarioService;
import com.app.util.Util;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private BCryptPasswordEncoder passEncoder;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IEmpresaService empresaService;

	@Autowired
	private IEmpresaUsuariosService empresaUsuarioService;
	
	@Autowired
	private ILiquidacionService liquidacionService;

	@RequestMapping(value = { "/recuperacion" })
	public String recuperacion(Model model, RedirectAttributes flash, Authentication authentication,
			HttpServletRequest request) {

		model.addAttribute("titulo", "Recuperar Clave");

		return "recuperarClave";
	}

	@PostMapping("/recuperarClave")
	public String recuperarClave(@RequestParam("email") String email, Map<String, Object> model,
			RedirectAttributes flash, SessionStatus status) {

		model.put("titulo", "Recuperar Clave");
		Usuario usuario = usuarioService.findByEmail(email);

		if (null == usuario) {
			flash.addFlashAttribute("msjLogin", "error;Usuario no existe;Usuario no existe con los datos ingresados");
		} else {
			String pass = Util.generatePassword(10);
			usuario.setPassword(passEncoder.encode(pass));
			usuario.setEstado("0");
			usuarioService.save(usuario);
			status.setComplete();
			usuario.setPassAux(pass);
			Executors.newSingleThreadExecutor().execute(() -> emailService.creacionUsuario(usuario));
			flash.addFlashAttribute("msjLogin",
					"success;Exito;Se envio un correo con una nueva contraseña para su ingreso");
		}

		return "redirect:/login";
	}

	@RequestMapping(value = { "/actualizarPass" })
	public String actualizar(Model model, RedirectAttributes flash, Authentication authentication,
			HttpServletRequest request) {

		model.addAttribute("titulo", "Actualizar Clave");

		return "actualizar";
	}

	@PostMapping("/actualizarPass")
	public String actualizarPass(@RequestParam("password1") String password1,
			@RequestParam("password2") String password2, Map<String, Object> model, RedirectAttributes flash,
			SessionStatus status, Principal principal) {

		model.put("titulo", "Actualizar Clave");

		if (!password1.equalsIgnoreCase(password2)) {
			flash.addFlashAttribute("msjLayout", "error;Error Password;Password deben ser iguales");
			return "redirect:/actualizarPass";
		}

		Usuario usuario = usuarioService.findByEmail(principal.getName());

		Empresa empresa = new Empresa();

		usuario.setPassword(passEncoder.encode(password1));
		usuario.setEstado("1");
		usuario.setEmpresa(empresa);
		usuarioService.save(usuario);
		status.setComplete();
		flash.addFlashAttribute("msjLayout", "success;Exito;Se realizo corrextamente el cambio de password");

		return "redirect:/login";
	}

	@Secured("ROLE_CONTADOR")
	@RequestMapping(value = { "/listadoUsuariosEmpresa" })
	public String listadoUsuariosEmpresa(@RequestParam(value = "empresaId", required = false) Long empresaId,
			Model model, RedirectAttributes flash, Authentication authentication, HttpServletRequest request) {

		Usuario usuarioLogin = (Usuario) request.getSession().getAttribute("usuarioLogin");
//		List<Usuario> listadoUsuariosEmpresa =  usuarioService.findUsuariosEmpresa(usuarioLogin.getId(),empresaId);
		List<EmpresaUsuarios> listadoUsuariosEmpresa = empresaUsuarioService.findUsuariosEmpresa(usuarioLogin.getId(),
				empresaId);
		List<Empresa> listadoEmpresas = empresaService.findAllById(usuarioLogin.getId());

		model.addAttribute("titulo", "Mantenedor Usuarios");
		model.addAttribute("listadoUsuarios", listadoUsuariosEmpresa);
		model.addAttribute("listadoEmpresas", listadoEmpresas);
		model.addAttribute("empresaIdSeleccionada", empresaId);

		return "listadoUsuariosEmpresa";
	}

	@Secured("ROLE_CONTADOR")
	@RequestMapping(value = { "/editarUsuarioEmpresa/{id}" })
	public String editarUsuarioEmpresa(@PathVariable(value = "id") Long id,
			@RequestParam(value = "empresaId", required = false) Long empresaId, 
			Map<String, Object> model,
			RedirectAttributes flash, 
			Authentication authentication, 
			HttpServletRequest request) {

		Usuario usuario = usuarioService.findById(id);

		if (null == usuario) {
			model.put("msjLayout", "error;Usuario no existe;Usuario no existe en BD");
			return "listadoUsuariosEmpresa";
		}

		Usuario usuarioLogin = (Usuario) request.getSession().getAttribute("usuarioLogin");
		Usuario aux = usuarioService.existeRelacion(usuarioLogin.getId(), usuario.getId());
		if (aux == null) {
			model.put("msjLayout", "error;Sin Privilegios;No puede editar usuarios que no ha creado");
			return "listadoUsuariosEmpresa";
		}

		if (empresaId != null) {
            Empresa empresa = empresaService.findById(empresaId);
            if (empresa == null) {
                model.put("msjLayout", "error;Empresa no válida;La empresa seleccionada no existe");
                return "listadoUsuariosEmpresa";
            }
            usuario.setEmpresa(empresa);
        }
		
		usuario.setEstado((usuario.getEnabled()) ? "1" : "2");
		List<Empresa> listadoEmpresas = empresaService.findAllById(usuarioLogin.getId());

		model.put("titulo", "Mantenedor Usuario");
		model.put("usuario", usuario);
		model.put("listadoEmpresas", listadoEmpresas);
		model.put("empresaId", empresaId);
		model.put("isEditing", true);
		model.put("btn", "Actualizar");
		return "usuarioEmpresa";
	}

	@RequestMapping(value = { "/crearUsuarioEmpresa" })
	public String crearUsuarioEmpresa(Map<String, Object> model, RedirectAttributes flash,
			Authentication authentication, HttpServletRequest request) {

		Usuario usuario = new Usuario();
		usuario.setRoles(Arrays.asList(new Role("Usuario", "ROLE_USER")));
		Usuario usuarioLogin = (Usuario) request.getSession().getAttribute("usuarioLogin");
		List<Empresa> listadoEmpresas = empresaService.findAllById(usuarioLogin.getId());

		model.put("titulo", "Mantenedor Usuario");
		model.put("usuario", usuario);
		model.put("listadoEmpresas", listadoEmpresas);
		model.put("isEditing", false);
		model.put("btn", "Crear");
		return "usuarioEmpresa";
	}

	@PostMapping("/guardarUsuarioEmpresa")
	public String guardarUsuarioEmpresa(@Valid Usuario usuario, BindingResult result, Map<String, Object> model,
			RedirectAttributes flash, SessionStatus status, HttpServletRequest request) {

		model.put("titulo", "Mantenedor Usuario");
		model.put("btn", usuario.getId() == null ? "Crear" : "Actualizar");

		boolean flag = (usuario.getId() == null);
		String msje = flag ? "Creado con Exito" : "Editado con Exito";
		String pass = "";

		Usuario usuarioLogin = (Usuario) request.getSession().getAttribute("usuarioLogin");
		List<Empresa> listadoEmpresas = empresaService.findAllById(usuarioLogin.getId());
		model.put("listadoEmpresas", listadoEmpresas);

		if (result.hasErrors())
			return "usuarioEmpresa";

		if (usuario.getEmpresa() == null) {
			model.put("msjLayout", "error;Validación;Debe seleccionar una empresa para para crear el usuario");
			return "usuarioEmpresa";
		}

		Usuario usuarioExisteEmail = usuarioService.findByEmail(usuario.getEmail());
		Usuario usuarioExisteRut = usuarioService.findByRut(usuario.getRut());
		EmpresaUsuarios euExistente = empresaUsuarioService.findByEmpresaAndUsuario(usuario.getEmpresa(),
				usuarioExisteEmail);

		if (usuarioExisteEmail != null) {
			// Omitir validación si es el mismo usuario actualizando
			if (usuario.getId() != null && usuarioExisteEmail.getId().equals(usuario.getId())) {
				// Es el mismo usuario, continuar con la actualización
			} else {
				if (euExistente != null) {
					// Correo ya existe en la misma empresa
					result.rejectValue("email", "error.usuario",
							"Ya existe un usuario con este correo en la empresa seleccionada");
					return "usuarioEmpresa";
				}

				if (!usuarioExisteEmail.getRut().equals(usuario.getRut())) {
					// Correo usado por otro RUT
					result.rejectValue("email", "error.usuario", "Este correo ya está asociado a otro usuario");
					return "usuarioEmpresa";
				}

				// Mismo RUT y correo, asociar a nueva empresa
				EmpresaUsuarios nuevaRelacion = new EmpresaUsuarios();
				nuevaRelacion.setEmpresa(usuario.getEmpresa());
				nuevaRelacion.setUsuario(usuarioExisteEmail);
				empresaUsuarioService.save(nuevaRelacion);

				flash.addFlashAttribute("msjLayout", "success;Asociado!;Usuario asociado correctamente");
				return "redirect:listadoUsuariosEmpresa";
			}
		}

		if (usuarioExisteRut != null) {
			// Omitir validación si es el mismo usuario actualizando
			if (usuario.getId() != null && usuarioExisteRut.getId().equals(usuario.getId())) {
				// Es el mismo usuario, continuar con la actualización
			} else {
				boolean rutEnMismaEmpresa = empresaUsuarioService.existsByEmpresaAndUsuario(usuario.getEmpresa(),
						usuarioExisteRut);
				if (rutEnMismaEmpresa) {
					result.rejectValue("rut", "error.usuario", "Ya existe un usuario con este RUT en la empresa");
					return "usuarioEmpresa";
				}
			}
		}

		usuario.setEnabled((usuario.getEstado().equalsIgnoreCase("1")) ? true : false);

		if (flag) {
			pass = Util.generatePassword(10);
			usuario.setPassword(passEncoder.encode(pass));
			usuario.setEstado("0");
		}

		usuarioService.save(usuario);

		if (flag) {
			EmpresaUsuarios eu = new EmpresaUsuarios();
			eu.setEmpresa(usuario.getEmpresa());
			eu.setUsuario(usuario);
			empresaUsuarioService.save(eu);
		}
		status.setComplete();
		flash.addFlashAttribute("msjLayout", "success;" + msje + "!;Usuario " + msje);

		if (flag) {
			usuario.setPassAux(pass);
			Executors.newSingleThreadExecutor().execute(() -> emailService.creacionUsuario(usuario));
		}

		return "redirect:listadoUsuariosEmpresa";
	}
	
	@Secured("ROLE_CONTADOR")
	@RequestMapping(value = "/deleteUsuarioEmpresa/{id}/{idEmpresa}")
	public String eliminar(@PathVariable(value = "id") Long id,@PathVariable(value = "idEmpresa") Long idEmpresa,Map<String, Object> model,RedirectAttributes flash,HttpServletRequest request) {	
		
		if(id > 0) {
			Usuario usuario = usuarioService.findById(id);
			Usuario usuarioLogin = (Usuario) request.getSession().getAttribute("usuarioLogin");
			Usuario aux = usuarioService.existeRelacion(usuarioLogin.getId(), usuario.getId());
			
			if (aux == null) {
				model.put("msjLayout", "error;Sin Privilegios;No puede eliminar usuarios que no ha creado");
				return "listadoUsuariosEmpresa";
			}
			
			if(null != usuario) {
				liquidacionService.deleteByUsuarioEmpresa(id,idEmpresa);
				List<EmpresaUsuarios> lista = empresaUsuarioService.findByUsuario(usuario);
				if(lista.size() == 1) usuarioService.delete(usuario);
				else empresaUsuarioService.deleteByUsuarioAndEmpresa(id,idEmpresa);
					
				flash.addFlashAttribute("msjLayout","success;Exito!;Usuario elimiando correctamente");
			}
		}
		
		return "redirect:/listadoUsuariosEmpresa";
	}

	@ModelAttribute("listadoRoles")
	public List<Role> listadoRoles() {
		return roleService.listar();
	}

	@ModelAttribute("estados")
	public Map<String, String> estados() {
		Map<String, String> estados = new HashMap<String, String>();
		estados.put("", "Seleccione");
		estados.put("1", "Activo");
		estados.put("2", "Desactivo");
		return estados;
	}
}
