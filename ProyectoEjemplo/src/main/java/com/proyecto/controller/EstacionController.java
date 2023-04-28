package com.proyecto.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.proyecto.modelo.Estacion;
import com.proyecto.modelo.MensajeError;
import com.proyecto.servicio.EstacionService;
import com.proyecto.servicio.EstacionServiceImpl;

import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/estaciones")
public class EstacionController {

	@Autowired
	private EstacionServiceImpl estacionServiceImpl;

	@PostMapping("/add")
	public ResponseEntity<?> addEstacion(@RequestBody Estacion estacion) {
		List<Estacion> listaEstaciones = estacionServiceImpl.listaEstaciones();

		for (Estacion estacion2 : listaEstaciones) {
			if (estacion.getId() == estacion2.getId())
				return new ResponseEntity(new MensajeError("Id ya existente."), HttpStatus.NOT_FOUND);
		}

		if (!validaNombre(estacion.getNombre()) || !validaUbicacion(estacion.getLatitud(), estacion.getLongitud())) {
			return new ResponseEntity(new MensajeError("No se pudo insertar la estacion."), HttpStatus.BAD_REQUEST);
		} else {
			estacionServiceImpl.insertarEstacion(estacion);
			return new ResponseEntity<Estacion>(estacion, HttpStatus.OK);
		}
	}

	@PutMapping("/edit")
	public ResponseEntity<?> editarEstacion(@RequestBody Estacion estacion) {
		try {
			Estacion estacionValida = estacionServiceImpl.verEstacion(estacion.getId()).get();
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeError("Estacion no encontrada."));
		}

		if (validaNombre(estacion.getNombre()) && validaUbicacion(estacion.getLatitud(), estacion.getLongitud())) {
			estacionServiceImpl.actualizarEstacion(estacion);
			return new ResponseEntity<Estacion>(estacion, HttpStatus.OK);
		} else
			return new ResponseEntity<Estacion>(estacion, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEstacion(@PathVariable("id") int id) {
		try {
			estacionServiceImpl.eliminarEstacion(id);
		} catch(NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeError("Ninguna estacion con esa id."));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MensajeError("Estacion eliminada correctamente."));
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> verEstacion(@PathVariable("id") int id) {
		try {
			Optional<Estacion> estacion = estacionServiceImpl.verEstacion(id);
			return new ResponseEntity<Optional<Estacion>>(estacion, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeError("Estacion no encontrada."));
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> listadoEstaciones() {
		List<Estacion> listaEstaciones = estacionServiceImpl.listaEstaciones();
		return new ResponseEntity(listaEstaciones, HttpStatus.OK);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<MensajeError> errorFormato(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(new MensajeError("Sintaxios o formato no válido."));
	}

	@ExceptionHandler(PersistenceException.class)
	public ResponseEntity<MensajeError> falloEnum(SQLException ex) {
		if (ex.getErrorCode() == 1265 && ex.getSQLState().equals("01000")) {
			return ResponseEntity.badRequest().body(new MensajeError("Enum no valido"));
		} else
			return null;
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<MensajeError> sinResultados(EmptyResultDataAccessException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeError("Ninguna estacion con esa id."));
	}

	private boolean validaNombre(String nombre) {
		List<Estacion> listaEstaciones = estacionServiceImpl.listaEstaciones();

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
		List<Estacion> listaEstaciones = estacionServiceImpl.listaEstaciones();

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
