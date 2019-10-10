package model.entities;

import java.io.Serializable;

import model.entities.enums.Estado;
import model.entities.enums.Tipo;

public class Equipamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private Tipo tipo;
	private Estado estadoAtual;

	public Equipamento() {

	}

	public Equipamento(Integer id, String nome, Tipo tipo, Estado estadoAtual) {
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.estadoAtual = estadoAtual;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Estado getEstadoAtual() {
		return estadoAtual;
	}

	public void setEstadoAtual(Estado estadoAtual) {
		this.estadoAtual = estadoAtual;
	}

	@Override
	public String toString() {
		return "Equipamento [id=" + id + ", nome=" + nome + ", tipo=" + tipo + ", estadoAtual=" + estadoAtual + "]";
	}

}
