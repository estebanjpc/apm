package com.app.dao;

import java.util.List;

import com.app.entity.Liquidacion;

public interface ILiquidacionJDBC {

	public List<Liquidacion> findLiquidacionByFiltro(String sql);

	public void deleteByUsuarioEmpresa(Long id, Long idEmpresa);

}
