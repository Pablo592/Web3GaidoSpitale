package ar.edu.iua.iw3.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length = 30, nullable = false)
	private String nombre;
	
	@Column(length = 30, nullable = false, unique = true)
	private String email;
	
	@Column(length = 30, nullable = false)
	private Date fechaNacimiento;
	
	@Column(length = 30, nullable = true)
	private long cantSeguidores;
	


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idPublicacion")
	private Publicacion idPublicacion;
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
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



	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}



	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}



	public long getCantSeguidores() {
		return cantSeguidores;
	}



	public void setCantSeguidores(long cantSeguidores) {
		this.cantSeguidores = cantSeguidores;
	}



	public Publicacion getIdPublicacion() {
		return idPublicacion;
	}



	public void setIdPublicacion(Publicacion idPublicacion) {
		this.idPublicacion = idPublicacion;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}
