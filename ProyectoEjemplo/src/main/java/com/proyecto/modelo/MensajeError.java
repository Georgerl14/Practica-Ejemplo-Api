package com.proyecto.modelo;

public class MensajeError {
	private String mensaje;
	
	public MensajeError(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
