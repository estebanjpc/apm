package com.app.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Usuario;
import com.app.service.IUsuarioService;

@Controller
public class LoginController {

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping({ "/login", "/" })
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash, HttpServletRequest request) {

		model.addAttribute("titulo", "Login");

		if (principal != null) {
			Usuario usuario = usuarioService.findByEmail(principal.getName());
			request.getSession().setAttribute("usuarioLogin", usuario);
			if (usuario.getEstado().equalsIgnoreCase("0"))
				return "redirect:/actualizarPass";

			boolean esUser = usuario.getRoles().stream().anyMatch(r -> "ROLE_USER".equals(r.getAuthority()));

			if (esUser) {
				return "redirect:/consulta";
			} else {
				return "redirect:/listadoEmpresas";
			}

		}

		if (error != null) {
			model.addAttribute("msjLogin",
					"error;Error en el login; nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
		}

		if (logout != null) {
			model.addAttribute("success", "Ha cerrado session con exito");
		}

		return "login";
	}

}
