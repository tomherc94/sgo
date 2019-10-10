package model.dao;

import java.util.List;

import model.entities.Supervisor;

public interface SupervisorDao {

	void insert(Supervisor obj);

	void update(Supervisor obj);

	void deleteById(Integer id);

	Supervisor findById(Integer id);
	
	Supervisor findByIdentidade(String identidade);

	List<Supervisor> findAll();
}
