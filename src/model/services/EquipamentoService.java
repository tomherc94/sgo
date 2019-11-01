package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EquipamentoDao;
import model.entities.Equipamento;

public class EquipamentoService {

	private EquipamentoDao dao = DaoFactory.createEquipamentoDao();

	public List<Equipamento> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Equipamento obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Equipamento obj) {
		dao.deleteById(obj.getId());
	}
}
