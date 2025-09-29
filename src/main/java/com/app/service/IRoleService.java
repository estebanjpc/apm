package com.app.service;

import java.util.List;

import com.app.entity.Role;


public interface IRoleService {

	
	public List<Role> listar();
	public Role obtenerPorId(Long id);
	public Role obtenerPorRole(String nombre);
	
}
