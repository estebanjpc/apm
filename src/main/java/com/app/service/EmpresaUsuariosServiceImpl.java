package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.IEmpresaUsuariosDao;
import com.app.entity.Empresa;
import com.app.entity.EmpresaUsuarios;
import com.app.entity.Usuario;

@Service
public class EmpresaUsuariosServiceImpl implements IEmpresaUsuariosService {
	
	@Autowired
	private IEmpresaUsuariosDao empresaUsuariosDao;

	@Override
	public void save(EmpresaUsuarios eu) {
		empresaUsuariosDao.save(eu);
	}

	
	@Override
	public List<EmpresaUsuarios> findAllUsuarioEmpresa(Long id) {
		return empresaUsuariosDao.findAllUsuarioEmpresa(id);
	}


	@Override
	public EmpresaUsuarios findByEmpresaAndUsuario(Empresa empresa, Usuario usuario) {
		return empresaUsuariosDao.findByEmpresaAndUsuario(empresa,usuario);
	}


	@Override
	public boolean existsByEmpresaAndUsuario(Empresa empresa, Usuario usuario) {
		return empresaUsuariosDao.existsByEmpresaAndUsuario(empresa, usuario);
	}


	@Override
	public List<EmpresaUsuarios> findUsuariosEmpresa(Long id, Long empresaId) {
		return empresaUsuariosDao.findUsuariosEmpresa(id, empresaId);
	}


	@Override
	public List<EmpresaUsuarios> findByUsuario(Usuario usuario) {
		return empresaUsuariosDao.findByUsuario(usuario);
	}

	@Override
	public void deleteByUsuarioAndEmpresa(Long id, Long idEmpresa) {
		empresaUsuariosDao.deleteByUsuarioAndEmpresa(id,idEmpresa);
	}
	
}
