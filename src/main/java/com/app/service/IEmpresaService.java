package com.app.service;

import java.util.List;

import com.app.entity.Empresa;

public interface IEmpresaService {

	public List<Empresa> findAllById(Long id);

	public void save(Empresa empresa);

	public Empresa findById(Long id);

	public void deleteById(Long id);

}
