package com.app.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.PdfLiquidacion;

public interface IPdfLiquidacionDao extends CrudRepository<PdfLiquidacion, Long>{

	@Query("select u from PdfLiquidacion u where idLiquidacion = ?1")
	public PdfLiquidacion findByIdLiquidacion(Long id);

	@Modifying
    @Transactional
	@Query("update PdfLiquidacion u set u.fileBlob = ?2 where idLiquidacion = ?1")
	public void updateByIdLiquidacion(Long id, byte[] bs);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM pdf_liquidacion WHERE id_liquidacion IN (SELECT id FROM liquidaciones WHERE id_usuario = ?1 AND id_empresa = ?2) ", nativeQuery = true)
	void deleteByUsuarioAndEmpresa(Long idUsuario,Long idEmpresa);

}
