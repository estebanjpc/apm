package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.app.entity.Empresa;

public interface IEmpresaDao extends CrudRepository<Empresa, Long>{
	
	@Query("select u from Empresa u where usuario.id = ?1")
	public List<Empresa> findAllById(Long id);

	@Query("SELECT DISTINCT e FROM EmpresaUsuarios eu JOIN eu.empresa e LEFT JOIN FETCH e.usuariosAsociados ua LEFT JOIN FETCH ua.usuario WHERE e.usuario.id = ?1")
	public List<Empresa> findEmpresaConUsuario(Long id);
	
	
	
}
