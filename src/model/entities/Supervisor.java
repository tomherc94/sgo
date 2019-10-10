package model.entities;

import java.io.Serializable;

import model.entities.enums.PostoGrad;

public class Supervisor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String identidade;
	private PostoGrad postoGrad;
	private String celular;
	private String login;
	private String senha;

	public Supervisor() {

	}

	public Supervisor(Integer id, String nome, String identidade, PostoGrad postoGrad, String celular, String login,
			String senha) {
		this.id = id;
		this.nome = nome;
		this.identidade = identidade;
		this.postoGrad = postoGrad;
		this.celular = celular;
		this.login = login;
		this.senha = senha;
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

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		this.identidade = identidade;
	}

	public PostoGrad getPostoGrad() {
		return postoGrad;
	}

	public void setPostoGrad(PostoGrad postoGrad) {
		this.postoGrad = postoGrad;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Supervisor [id=" + id + ", nome=" + nome + ", identidade=" + identidade + ", postoGrad=" + postoGrad
				+ ", celular=" + celular + ", login=" + login + ", senha=" + senha + "]";
	}

}
