package model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.entities.enums.StatusLivro;

public class Livro implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Date dataHoraAbertura;
	private Date dataHoraFechamento;
	private StatusLivro status;
	private Supervisor supervisor;
	private Turno turno;
	private List<Ocorrencia> listaOcor;

	public Livro() {
		this.listaOcor = new ArrayList<>();
	}

	

	public Livro(Integer id, Date dataHoraAbertura, StatusLivro status, Supervisor supervisor, Turno turno) {
		this.id = id;
		this.dataHoraAbertura = dataHoraAbertura;
		this.status = status;
		this.supervisor = supervisor;
		this.turno = turno;
		this.listaOcor = new ArrayList<>();
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataHoraAbertura() {
		return dataHoraAbertura;
	}

	public void setDataHoraAbertura(Date dataHoraAbertura) {
		this.dataHoraAbertura = dataHoraAbertura;
	}

	public Date getDataHoraFechamento() {
		return dataHoraFechamento;
	}

	public void setDataHoraFechamento(Date dataHoraFechamento) {
		this.dataHoraFechamento = dataHoraFechamento;
	}

	public StatusLivro getStatus() {
		return status;
	}

	public void setStatus(StatusLivro status) {
		this.status = status;
	}

	public Supervisor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}

	public List<Ocorrencia> getListaOcor() {
		return listaOcor;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public void adicionarOcorrencia(Ocorrencia ocorrencia) {
		this.listaOcor.add(ocorrencia);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Livro other = (Livro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Livro [id=" + id + ", dataHoraAbertura=" + dataHoraAbertura + ", dataHoraFechamento="
				+ dataHoraFechamento + ", status=" + status + ", supervisor=" + supervisor + "]";
	}

}
