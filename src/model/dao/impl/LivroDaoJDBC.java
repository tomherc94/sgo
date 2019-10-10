package model.dao.impl;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import model.dao.LivroDao;
import model.entities.Livro;
import model.entities.Supervisor;
import model.entities.enums.StatusLivro;

public class LivroDaoJDBC implements LivroDao{

	private Connection conn;

	public LivroDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Livro obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Livro obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByStatus(StatusLivro status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Livro findByStatus(StatusLivro status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Livro> findByDataHoraAbertura(Date inicio, Date fim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Livro> findBySupervisor(Supervisor supervisor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Livro> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
