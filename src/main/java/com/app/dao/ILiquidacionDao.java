package com.app.dao;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.app.entity.Liquidacion;

public interface ILiquidacionDao extends CrudRepository<Liquidacion, Long>{

	@Query("select u from Liquidacion u where u.usuario.id = ?1 and fecha = ?2 and u.empresa.id = ?3")
	public Optional<Liquidacion> findByLiquidacionByIdFecha(Long idUsuario, Date mes, Long empresaId);
	
	
	
	
}
