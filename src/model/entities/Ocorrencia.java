package model.entities;

import java.io.Serializable;
import java.util.Date;

import model.entities.enums.Estado;

public class Ocorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Estado estado;
	private Date data;
	private Date hora;
	private StringBuilder descricao;
	private Equipamento equipamento;
	private Livro livro;

	public Ocorrencia() {

	}

	public Ocorrencia(Integer id, Estado estado, Date data, Date hora, StringBuilder descricao, Equipamento equipamento,
			Livro livro) {
		this.id = id;
		this.estado = estado;
		this.data = data;
		this.hora = hora;
		this.descricao = descricao;
		this.equipamento = equipamento;
		this.livro = livro;
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
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

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	@Override
	public String toString() {
		return "Ocorrencia [id=" + id + ", estado=" + estado + ", data=" + data + ", hora=" + hora + ", descricao="
				+ descricao + ", equipamento=" + equipamento + ", livro=" + livro + "]";
	}

	

}
