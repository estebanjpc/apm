package com.app.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
    name = "liquidaciones",
    uniqueConstraints = @UniqueConstraint(columnNames = { "id_usuario", "fecha","id_empresa" })
)
public class Liquidacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    private Date fecha;

    @Column(name="fecha_carga")
    private Date fechaCarga;

    @Column(name="nombre_archivo")
    private String nombreArchivo;

    @PrePersist
    protected void onCreate() {
        if (this.fechaCarga == null) {
            this.fechaCarga = new Date(System.currentTimeMillis());
        }
    }

    public Liquidacion() {}

    public Liquidacion(Long id, Usuario usuario, Date fecha, Date fechaCarga, Empresa empresa) {
        this.id = id;
        this.usuario = usuario;
        this.fecha = fecha;
        this.fechaCarga = fechaCarga;
        this.empresa = empresa;
    }

    public Liquidacion(Usuario usuario, Date fecha, String nombre, Empresa empresa) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.nombreArchivo = nombre;
        this.empresa = empresa;
    }
    
    

    public Liquidacion(Long id, Usuario usuario, Date fecha, Date fechaCarga, String nombreArchivo, Empresa empresa) {
		this.id = id;
		this.usuario = usuario;
		this.fecha = fecha;
		this.fechaCarga = fechaCarga;
		this.nombreArchivo = nombreArchivo;
		this.empresa = empresa;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
    
}
