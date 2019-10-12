package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Livro;
import model.entities.Supervisor;

public interface LivroDao {

	void insert(Livro obj);

	void update(Livro obj);

	void deleteByStatusAberto();

	Livro findById(Integer id);
	
	Livro findLivroAberto();

	List<Livro> findByDataHoraAbertura(Date inicio, Date fim);

	List<Livro> findBySupervisor(Supervisor supervisor);

	List<Livro> findAll();
}
