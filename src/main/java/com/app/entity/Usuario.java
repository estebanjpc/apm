package com.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30)
	@NotEmpty
	private String nombre;

	@Column(length = 30)
	@NotEmpty
	private String apellido;

	@Column(length = 30)
	@NotEmpty
	private String email;

	@Column(length = 100)
	private String password;

	private Boolean enabled;

	@NotEmpty
	private String estado;

	private String rut;

	@Transient
	private String passAux;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	@NotEmpty
	private List<Role> roles;

	@Transient
	private Empresa empresa;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmpresaUsuarios> empresasAsociadas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public Usuario() {
		this.roles = new ArrayList<Role>();
	}

	public String getPassAux() {
		return passAux;
	}

	public void setPassAux(String passAux) {
		this.passAux = passAux;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<EmpresaUsuarios> getEmpresasAsociadas() {
		return empresasAsociadas;
	}

	public void setEmpresasAsociadas(List<EmpresaUsuarios> empresasAsociadas) {
		this.empresasAsociadas = empresasAsociadas;
	}

	public Usuario(Long id,String nombre,String apellido,String email, String rut) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.rut = rut;
	}

	
	
}
