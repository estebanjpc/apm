package com.app.editors;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.service.IRoleService;

@Component
public class RolesEditor extends PropertyEditorSupport{
	
	@Autowired
	private IRoleService roleService;

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		try {
			setValue(roleService.obtenerPorRole(text));
		}catch(Exception e) {
			setValue(null);
		}
	}

	
	
	
}
