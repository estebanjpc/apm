package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entity.Role;


@Service
public class RoleServiceImpl implements IRoleService {
	
	private List<Role> roles;
	
	public RoleServiceImpl() {
		this.roles = new ArrayList<Role>();
		this.roles.add(new Role("Administrador","ROLE_ADMIN"));
		this.roles.add(new Role("Contador","ROLE_CONTADOR"));
		this.roles.add(new Role("Usuario","ROLE_USER"));
	}

	@Override
	public List<Role> listar() {
		return roles;
	}

	@Override
	public Role obtenerPorId(Long id) {
		
		Role result = null;
		
		for (Role role : roles) {
			if(role.getId().equals(id)) {
				result = role;
				break;
			}
		}
		return result;
	}

	@Override
	public Role obtenerPorRole(String nombre) {
		Role result = null;
		
		for (Role role : roles) {
			if(role.getAuthority().equalsIgnoreCase(nombre)) {
				result = role;
				break;
			}
		}
		return result;
	}

}
