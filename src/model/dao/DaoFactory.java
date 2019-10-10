package model.dao;

import db.DB;
import model.dao.impl.EquipamentoDaoJDBC;
import model.dao.impl.SupervisorDaoJDBC;

public class DaoFactory {

	public static SupervisorDao createSupervisorDao() {
		return new SupervisorDaoJDBC(DB.getConnection());
	}
	
	public static EquipamentoDao createEquipamentoDao() {
		return new EquipamentoDaoJDBC(DB.getConnection());
	}
}
