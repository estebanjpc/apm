package com.app.service;

import java.util.List;

import com.app.entity.Usuario;

public interface IUsuarioService {

	
	public List<Usuario> findAll();
	public Usuario findByRut(String rut);
	public Usuario findById(Long id);
	public void save(Usuario usuario);
	public Usuario findByEmail(String email);
	public void delete(Usuario usuario);
//	public List<Usuario> findAllUsuarioEmpresa();
//	public List<Usuario> findAllByRol(String rol);
	public List<Usuario> findUsuariosEmpresa(Long id, Long empresaId);
	public Usuario existeRelacion(Long idUsuario, Long idContador);
	
}
