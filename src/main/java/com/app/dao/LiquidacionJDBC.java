package com.app.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.app.entity.Empresa;
import com.app.entity.Liquidacion;
import com.app.entity.Usuario;

@Repository
public class LiquidacionJDBC implements ILiquidacionJDBC {
	
	@Autowired
    private JdbcTemplate jdbc;

	@SuppressWarnings("deprecation")
	@Override
	public List<Liquidacion> findLiquidacionByFiltro(String sql) {
		return jdbc.query(sql,new Object[]{},
				(rs, rowNum) ->
		new Liquidacion(
					   rs.getLong("idLiq"),
					   new Usuario(rs.getLong("idUsuario"),
							   rs.getString("nombre"),
							   rs.getString("apellido"),
							   rs.getString("email"),
							   rs.getString("rut")),
					   rs.getDate("fecha"),
					   rs.getDate("fecha_carga"),
					   rs.getString("nombre_archivo"),
					   new Empresa(rs.getLong("idEmpresa"),
							   	   rs.getString("nombre_empresa"),
							   	   rs.getString("razon_social")
							   	   )
					   )
	);
	}

	@Override
	public void deleteByUsuarioEmpresa(Long id, Long idEmpresa) {
		jdbc.update("delete from liquidaciones where id_usuario = ? and id_empresa = ?",id,idEmpresa);
	}

	
	
	
}
