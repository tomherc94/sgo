package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SupervisorDao;
import model.entities.Supervisor;

public class SupervisorService {

	private SupervisorDao dao = DaoFactory.createSupervisorDao();

	public List<Supervisor> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Supervisor obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Supervisor obj) {
		dao.deleteById(obj.getId());
	}
}
