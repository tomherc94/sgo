package model.dao;

import java.util.List;

import model.entities.Turno;

public interface TurnoDao {

	void insert(Turno obj);

	void update(Turno obj);

	void deleteById(Integer id);

	Turno findById(Integer id);

	List<Turno> findAll();
}
