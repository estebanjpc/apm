package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.IUsuarioDao;
import com.app.entity.Usuario;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioDao usuarioDao;
	

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByRut(String rut) {
		return usuarioDao.findByRut(rut);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findById(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Usuario usuario) {
		usuarioDao.save(usuario);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByEmail(String email) {
		return usuarioDao.findByEmail(email);
	}

	@Override
	public void delete(Usuario usuario) {
		usuarioDao.delete(usuario);
	}

	@Override
	public List<Usuario> findUsuariosEmpresa(Long id,Long idEmpresa) {
		return usuarioDao.findUsuariosEmpresa(id,idEmpresa);
	}

//	@Override
//	public List<Usuario> findAllUsuarioEmpresa() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public List<Usuario> findAllByRol(String rol) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Usuario existeRelacion(Long idUsuario, Long idContador) {
		return usuarioDao.findUsuarioByIdContador(idUsuario,idContador);
		
	}
	
}
