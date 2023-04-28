package com.proyecto.modelo;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "estaciones", uniqueConstraints = @UniqueConstraint(columnNames = { "latitud", "longitud" }))
@Check(constraints = 
		"((Activa = true AND Estado = 'OPERATIVA') "
		+ "OR (Activa = false AND Estado IN('EN_MANTENIMIENTO','EN_CALIBRACION'))) "
		+ "AND (Longitud BETWEEN -180 AND 180)"
		+ "AND (Latitud BETWEEN -90 AND 90)"
		+ "AND (TRIM(Nombre) <> ' ')")

public class Estacion {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NotNull
	@Column(name = "Nombre", length = 30, unique = true, nullable = false)
	private String nombre;

	@NotNull
	@Column(name = "Latitud")
	private double latitud;

	@NotNull
	@Column(name = "Longitud")
	private double longitud;

	@NotNull
	@Column(name = "Tipo", columnDefinition = "ENUM('INMISION','EMISION')")
	private String tipo;

	@NotNull
	@Column(name = "Estado", columnDefinition = "ENUM('OPERATIVA','EN_CALIBRACION','EN_MANTENIMIENTO')")
	private String estado;

	@NotNull
	@Column(name = "Activa")
	private Boolean activa;

	public Estacion() {
	}

	public Estacion(int id, String nombre, double latitud, double longitud, String tipo, String estado,
			boolean activa) {
		this.id = id;
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.tipo = tipo;
		this.estado = estado;
		this.activa = activa;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		if (estado.equals("OPERATIVA"))
			this.activa = true;
		else
			this.activa = false;
	}

}
