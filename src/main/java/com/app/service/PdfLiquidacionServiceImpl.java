package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.IPdfLiquidacionDao;
import com.app.entity.PdfLiquidacion;

@Service
public class PdfLiquidacionServiceImpl implements IPdfLiquidacionService {
	
	@Autowired
	private IPdfLiquidacionDao pdfLiquidacion;

	@Override
	public PdfLiquidacion findById(Long id) {
		return pdfLiquidacion.findByIdLiquidacion(id);
	}

}
