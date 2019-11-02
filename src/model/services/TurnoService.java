package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TurnoDao;
import model.entities.Turno;

public class TurnoService {

	private TurnoDao dao = DaoFactory.createTurnoDao();

	public List<Turno> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Turno obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Turno obj) {
		dao.deleteById(obj.getId());
	}
}
