package com.proyecto.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.proyecto.modelo.Estacion;

@Repository
public interface EstacionRepo extends JpaRepository<Estacion, Integer> {
	
}
