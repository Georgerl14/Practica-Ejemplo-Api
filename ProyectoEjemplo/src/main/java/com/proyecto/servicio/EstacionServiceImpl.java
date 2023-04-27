package com.proyecto.servicio;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Estacion verEstacion(int id) {
		return estacionRepo.findById(id).get();
	}

	@Override
	public List<Estacion> eliminarEstacion(int id) {
		estacionRepo.deleteById(id);
		return estacionRepo.findAll();
	}

	@Override
	public List<Estacion> listaEstaciones() {
		return estacionRepo.findAll();
	}

}
