package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Equipamento;
import model.entities.Ocorrencia;

public interface OcorrenciaDao {

	void insert(Ocorrencia obj);

	void update(Ocorrencia obj);

	void deleteById(Integer id);
	
	void deleteByIdLivro(Integer id);
	
	Ocorrencia findById(Integer id);
	
	List<Ocorrencia> findByData(Date inicio, Date fim);
	
	List<Ocorrencia> findByEquipamento(Equipamento equipamento);
	
	List<Ocorrencia> findByLivro(Integer id);

	List<Ocorrencia> findAll();
	
	List<Ocorrencia> findByLivroAberto();
}
