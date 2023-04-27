package com.proyecto.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.modelo.Estacion;
import com.proyecto.modelo.MensajeError;
import com.proyecto.servicio.EstacionService;

import jakarta.persistence.PersistenceException;

@RestController
@RequestMapping("/estaciones")
public class EstacionController {

	@Autowired
	private EstacionService estacionService;

	@PostMapping("/add")
	public ResponseEntity<?> addEstacion(@RequestBody Estacion estacion) {
		List<Estacion> listaEstaciones = estacionService.listaEstaciones();

		for (Estacion estacion2 : listaEstaciones) {
			if (estacion.getId() == estacion2.getId())
				return new ResponseEntity(new MensajeError("Id ya existente."), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		if (validaNombre(estacion.getNombre()) && validaUbicacion(estacion.getLatitud(), estacion.getLongitud())) {
			estacionService.insertarEstacion(estacion);
			return new ResponseEntity<Estacion>(estacion, HttpStatus.OK);
		} else {
			return new ResponseEntity(new MensajeError("No se pudo insertar la estacion."), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit")
	public ResponseEntity<?> editEstacion(@RequestBody Estacion estacion) {

		if (!idExiste(estacion.getId()))
			return new ResponseEntity(new MensajeError("Id no existente."), HttpStatus.NOT_FOUND);

		if (validaNombre(estacion.getNombre()) && validaUbicacion(estacion.getLatitud(), estacion.getLongitud())) {
			estacionService.insertarEstacion(estacion);
			return new ResponseEntity<Estacion>(estacion, HttpStatus.OK);
		} else
			return new ResponseEntity<Estacion>(estacion, HttpStatus.BAD_REQUEST);
	}

	private boolean idExiste(int id) {
		List<Estacion> listaEstaciones = estacionService.listaEstaciones();
		for (Estacion estacion : listaEstaciones) {
			if (estacion.getId() == id) {
				return true;
			}
		}
		return false;
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity deleteEstacion(@PathVariable("id") int id) {
		
		if(!idExiste(id)) {
			return new ResponseEntity(new MensajeError("Id no existe"), HttpStatus.NOT_FOUND);
		}
		
		List<Estacion> listaEstacion =  estacionService.eliminarEstacion(id);
		return new ResponseEntity(listaEstacion,HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public ResponseEntity listadoEstaciones() {
		List<Estacion> listaEstaciones = estacionService.listaEstaciones();
		return new ResponseEntity(listaEstaciones, HttpStatus.OK);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> verEstacion(@PathVariable("id") int id) {
		try {
			Estacion estacion = estacionService.verEstacion(id);
			return new ResponseEntity<Estacion>(estacion, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return ResponseEntity.badRequest().body(new MensajeError("Estacion no encontrada."));
		}
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<MensajeError> errorFormato(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(new MensajeError("Sintaxios o formato no válido."));
	}

	@ExceptionHandler(PersistenceException.class)
	public ResponseEntity<MensajeError> falloEnum(SQLException ex) {
		if (ex.getErrorCode() == 1265 && ex.getSQLState().equals("01000")) {
			return ResponseEntity.badRequest().body(new MensajeError("Enum inválido"));
		} else
			return null;
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<MensajeError> sinResultados(EmptyResultDataAccessException ex) {
		return ResponseEntity.badRequest().body(new MensajeError("Ninguna estacion con esa id."));
	}

	private boolean validaNombre(String nombre) {
		List<Estacion> listaEstaciones = estacionService.listaEstaciones();

		if (nombre.isBlank() || nombre.matches("\\d+"))
			return false;

		for (Estacion estacion : listaEstaciones) {
			if (estacion.getNombre().equals(nombre)) {
				return false;
			}
		}

		return true;
	}

	private boolean validaUbicacion(double latitud, double longitud) {
		List<Estacion> listaEstaciones = estacionService.listaEstaciones();

		// Comprobar limites de latitud y longitud
		if ((latitud < -90 || latitud > 90) && (longitud < -180 || longitud > 180))
			return false;
	
		// Con elementos en la lista
		for (Estacion estacion : listaEstaciones) {
			if (estacion.getLatitud() == latitud && estacion.getLongitud() == longitud) {
				return false;
			}
		}

		return true;
	}
}
