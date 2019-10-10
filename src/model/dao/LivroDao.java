package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Livro;
import model.entities.Supervisor;
import model.entities.enums.StatusLivro;

public interface LivroDao {

	void insert(Livro obj);

	void update(Livro obj);

	void deleteByStatus(StatusLivro status);

	Livro findByStatus(StatusLivro status);
	
	List<Livro> findByDataHoraAbertura(Date inicio, Date fim);
	
	List<Livro> findBySupervisor(Supervisor supervisor);

	List<Livro> findAll();
}
