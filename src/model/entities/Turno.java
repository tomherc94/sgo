package model.entities;

import java.io.Serializable;

public class Turno implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String horaInicio;
	private String horaFim;

	public Turno() {

	}

	public Turno(Integer id, String horaInicio, String horaFim) {
		this.id = id;
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}

	@Override
	public String toString() {
		return this.getHoraInicio() + "Z até " + this.getHoraFim() + "Z";
	}

}
