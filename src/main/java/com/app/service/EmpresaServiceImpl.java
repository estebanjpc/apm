package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.IEmpresaDao;
import com.app.entity.Empresa;

@Service
public class EmpresaServiceImpl implements IEmpresaService {
	
	@Autowired
	private IEmpresaDao empresaDao;
	

	@Override
	public List<Empresa> findAllById(Long id) {
		return (List<Empresa>) empresaDao.findAllById(id);
	}

	@Override
	public void save(Empresa empresa) {
		empresaDao.save(empresa);
	}

	@Override
	@Transactional(readOnly = true)
	public Empresa findById(Long id) {
		return empresaDao.findById(id).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		empresaDao.deleteById(id);		
	}


}
