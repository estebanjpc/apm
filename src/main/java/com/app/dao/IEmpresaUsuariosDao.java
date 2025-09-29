package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Empresa;
import com.app.entity.EmpresaUsuarios;
import com.app.entity.Usuario;

public interface IEmpresaUsuariosDao extends CrudRepository<EmpresaUsuarios, Long>{

	@Query("SELECT DISTINCT eu.empresa FROM EmpresaUsuarios eu JOIN FETCH eu.empresa.usuariosAsociados ua JOIN FETCH ua.usuario WHERE eu.usuario.id = ?1")
	public List<EmpresaUsuarios> findAllUsuarioEmpresa(Long id);

	@Query("SELECT eu FROM EmpresaUsuarios eu WHERE eu.empresa = ?1 and eu.usuario = ?2")
	public EmpresaUsuarios findByEmpresaAndUsuario(Empresa empresa, Usuario usuario);

	public boolean existsByEmpresaAndUsuario(Empresa empresa, Usuario usuario);
	
	@Query("SELECT eu FROM EmpresaUsuarios eu JOIN FETCH eu.usuario u JOIN FETCH eu.empresa e WHERE e.usuario.id = ?1 AND (?2 IS NULL OR e.id = ?2)")
	public List<EmpresaUsuarios> findUsuariosEmpresa(Long id, Long empresaId);

	@Query("SELECT eu FROM EmpresaUsuarios eu where usuario = ?1")
	public List<EmpresaUsuarios> findByUsuario(Usuario usuario);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM empresa_usuarios WHERE usuario_id = ?1 and empresa_id = ?2 ", nativeQuery = true)
	public void deleteByUsuarioAndEmpresa(Long id, Long idEmpresa);
	
}
