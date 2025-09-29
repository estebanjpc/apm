package com.app.service;

import java.util.List;

import com.app.entity.Empresa;
import com.app.entity.EmpresaUsuarios;
import com.app.entity.Usuario;

public interface IEmpresaUsuariosService {

	public void save(EmpresaUsuarios eu);
	public List<EmpresaUsuarios> findAllUsuarioEmpresa(Long id);
	public EmpresaUsuarios findByEmpresaAndUsuario(Empresa empresa, Usuario usuario);
	public boolean existsByEmpresaAndUsuario(Empresa empresa, Usuario usuario);
	public List<EmpresaUsuarios> findUsuariosEmpresa(Long id, Long empresaId);
	public List<EmpresaUsuarios> findByUsuario(Usuario usuario);
	public void deleteByUsuarioAndEmpresa(Long id, Long idEmpresa);

}
