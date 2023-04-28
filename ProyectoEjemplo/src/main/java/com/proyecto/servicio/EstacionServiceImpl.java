package com.proyecto.servicio;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyecto.modelo.Estacion;
import com.proyecto.modelo.MensajeError;
import com.proyecto.persistencia.EstacionRepo;

@Service
public class EstacionServiceImpl implements EstacionService {

	@Autowired
	private EstacionRepo estacionRepo;

	@Override
	public Estacion insertarEstacion(Estacion estacion) {
		return estacionRepo.save(estacion);
	}

	@Override
	public Optional<Estacion> verEstacion(int id) {
		return estacionRepo.findById(id);
	}

	@Override
	public void eliminarEstacion(int id) throws NoSuchElementException {
		if (estacionRepo.findById(id).get() != null) {
			estacionRepo.deleteById(id);
		}
	}

	@Override
	public List<Estacion> listaEstaciones() {
		return estacionRepo.findAll();
	}

	@Override
	public Estacion actualizarEstacion(Estacion estacion) {
		return estacionRepo.save(estacion);
	}

}
