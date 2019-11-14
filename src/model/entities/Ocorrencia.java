package model.entities;

import java.io.Serializable;
import java.util.Date;

import model.entities.enums.Estado;

public class Ocorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Estado estado;
	private Date dataHora;
	private StringBuilder descricao;
	private Equipamento equipamento;
	private Integer idLivro;

	public Ocorrencia() {

	}

	public Ocorrencia(Integer id, Estado estado, Date dataHora, StringBuilder descricao, Equipamento equipamento,
			Integer idLivro) {
		super();
		this.id = id;
		this.estado = estado;
		this.dataHora = dataHora;
		this.descricao = descricao;
		this.equipamento = equipamento;
		this.idLivro = idLivro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public StringBuilder getDescricao() {
		return descricao;
	}

	public void setDescricao(StringBuilder descricao) {
		this.descricao = descricao;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public Integer getIdLivro() {
		return idLivro;
	}

	public void setIdLivro(Integer idLivro) {
		this.idLivro = idLivro;
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
		Ocorrencia other = (Ocorrencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ocorrencia [id=" + id + ", estado=" + estado + ", dataHora=" + dataHora + ", descricao=" + descricao
				+ ", equipamento=" + equipamento + ", idLivro=" + idLivro + "]";
	}

}
