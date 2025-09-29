package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.app.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	@Query("select u from Usuario u where rut = ?1")
	public Usuario findByRut(String rut);
	
	@Query("select u from Usuario u where email = ?1")
	public Usuario findByEmail(String email);

	@Query("SELECT u FROM Usuario u JOIN u.empresasAsociadas eu JOIN eu.empresa e WHERE e.usuario.id = ?1 AND (?2 IS NULL OR e.id = ?2) order by e.id")
//	@Query("SELECT eu FROM EmpresaUsuarios eu JOIN FETCH eu.usuario u JOIN FETCH eu.empresa e WHERE e.usuario.id = ?1 AND (?2 IS NULL OR e.id = ?2)")
	public List<Usuario> findUsuariosEmpresa(Long id,Long empresaId);
	
	@Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.empresasAsociadas eu JOIN FETCH eu.empresa e WHERE e.usuario.id = ?1 and u.id = ?2")
	public Usuario findUsuarioByIdContador(Long idUsuario, Long idContador);

	
	
}
