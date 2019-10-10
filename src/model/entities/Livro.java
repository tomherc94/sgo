package model.entities;

import java.io.Serializable;
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
	private List<Ocorrencia> listaOcor;

	public Livro() {

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

	public void adicionarOcorrencia(Ocorrencia ocorrencia) {
		this.listaOcor.add(ocorrencia);
	}

	@Override
	public String toString() {
		return "Livro [id=" + id + ", dataHoraAbertura=" + dataHoraAbertura + ", dataHoraFechamento="
				+ dataHoraFechamento + ", status=" + status + ", supervisor=" + supervisor + ", listaOcor=" + listaOcor
				+ "]";
	}

}
