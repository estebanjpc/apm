package com.app.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Liquidacion;

public interface ILiquidacionService {

	public void save(Liquidacion liquidacion, MultipartFile archivo);

	public List<Liquidacion> findLiquidacionByFiltro(Long id, Long idEmpresa, LocalDate fecha);
	public List<Liquidacion> findLiquidacionByUsuario(Long id, Long idEmpresa, LocalDate fecha);

	public Liquidacion findById(Long id);

	public void delete(Liquidacion liquidacion);

	public void deleteByUsuarioEmpresa(Long id, Long idEmpresa);

}
