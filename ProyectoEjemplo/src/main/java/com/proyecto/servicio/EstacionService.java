package com.proyecto.servicio;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import com.proyecto.modelo.Estacion;

public interface EstacionService {
	public Estacion insertarEstacion(Estacion estacion);
	
	public Estacion verEstacion(int id);
	
	public List<Estacion> eliminarEstacion(int id);
	
	public List<Estacion> listaEstaciones();
}
