package com.app.service;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dao.ILiquidacionDao;
import com.app.dao.ILiquidacionJDBC;
import com.app.dao.IPdfLiquidacionDao;
import com.app.entity.Liquidacion;
import com.app.entity.PdfLiquidacion;

@Service
public class LiquidacionServiceImpl implements ILiquidacionService {

	@Autowired
	private ILiquidacionDao liquidacionDao;

	@Autowired
	private IPdfLiquidacionDao pdfLiquidacion;
	
	@Autowired
	private ILiquidacionJDBC liquidacionJdbc;

	@Override
	public void save(Liquidacion liquidacion, MultipartFile archivo) {

		LocalDate localDate = liquidacion.getFecha().toLocalDate().withDayOfMonth(1);
		Date mes = Date.valueOf(localDate);
		try {
			Optional<Liquidacion> existe = liquidacionDao.findByLiquidacionByIdFecha(liquidacion.getUsuario().getId(), mes,liquidacion.getEmpresa().getId());

			if (existe.isPresent()) {
				Liquidacion liq = existe.get();
				liq.setFechaCarga(liquidacion.getFechaCarga());
				liquidacionDao.save(liq);
				pdfLiquidacion.updateByIdLiquidacion(liq.getId(), archivo.getBytes());
			} else {
				liquidacion.setFecha(mes);
				liquidacionDao.save(liquidacion);
				PdfLiquidacion pdf = new PdfLiquidacion();
				pdf.setIdLiquidacion(liquidacion.getId());
				pdf.setFileBlob(archivo.getBytes());
				pdfLiquidacion.save(pdf);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Liquidacion> findLiquidacionByFiltro(Long id, Long idEmpresa, LocalDate fecha) {
		
		String sql = "select l.id idLiq,l.fecha,l.fecha_carga,l.nombre_archivo,u.id idUsuario,u.nombre,u.apellido,u.email,u.rut,e.id idEmpresa,e.nombre nombre_empresa,e.razon_social from empresas e inner join empresa_usuarios eu on e.id = eu.empresa_id inner join liquidaciones l on eu.usuario_id = l.id_usuario and e.id = l.id_empresa inner join usuarios u on u.id = l.id_usuario where 1=1 and e.usuario_id = " + id;
		if(idEmpresa != null) sql += " and e.id = " + idEmpresa;
		if(fecha != null) sql += " and l.fecha = '" + fecha + "'";
		
		return liquidacionJdbc.findLiquidacionByFiltro(sql);
//		return (List<Liquidacion>) liquidacionDao.findAll();
	}
	
	@Override
	public List<Liquidacion> findLiquidacionByUsuario(Long id, Long idEmpresa, LocalDate fecha) {
		
		String sql = "select l.id idLiq,l.fecha,l.fecha_carga,l.nombre_archivo,u.id idUsuario,u.nombre,u.apellido,u.email,u.rut,e.id idEmpresa,e.nombre nombre_empresa,e.razon_social from empresas e inner join empresa_usuarios eu on e.id = eu.empresa_id inner join liquidaciones l on eu.usuario_id = l.id_usuario and e.id = l.id_empresa inner join usuarios u on u.id = l.id_usuario where 1=1 and u.id = " + id;
		if(idEmpresa != null) sql += " and e.id = " + idEmpresa;
		if(fecha != null) sql += " and l.fecha = '" + fecha + "'";
		
		sql += " order by l.fecha desc";
		
		return liquidacionJdbc.findLiquidacionByFiltro(sql);
//		return (List<Liquidacion>) liquidacionDao.findAll();
	}

	@Override
	public Liquidacion findById(Long id) {
		return liquidacionDao.findById(id).orElse(null);
	}

	@Override
	public void delete(Liquidacion liquidacion) {
		liquidacionDao.delete(liquidacion);
		pdfLiquidacion.deleteById(liquidacion.getId());
	}

	@Override
	public void deleteByUsuarioEmpresa(Long id, Long idEmpresa) {
		pdfLiquidacion.deleteByUsuarioAndEmpresa(id,idEmpresa);
		liquidacionJdbc.deleteByUsuarioEmpresa(id,idEmpresa);
	}

}
