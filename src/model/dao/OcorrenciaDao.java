package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Equipamento;
import model.entities.Livro;
import model.entities.Ocorrencia;

public interface OcorrenciaDao {

	void insert(Ocorrencia obj);

	void update(Ocorrencia obj);

	void deleteById(Integer id);
	
	Ocorrencia findById(Integer id);
	
	List<Ocorrencia> findByData(Date inicio, Date fim);
	
	List<Ocorrencia> findByEquipamento(Equipamento equipamento);
	
	List<Ocorrencia> findByLivro(Livro livro);

	List<Ocorrencia> findAll();
}
