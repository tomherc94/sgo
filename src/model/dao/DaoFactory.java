package model.dao;

import db.DB;
import model.dao.impl.EquipamentoDaoJDBC;
import model.dao.impl.SupervisorDaoJDBC;
import model.dao.impl.TurnoDaoJDBC;

public class DaoFactory {

	public static SupervisorDao createSupervisorDao() {
		return new SupervisorDaoJDBC(DB.getConnection());
	}
	
	public static EquipamentoDao createEquipamentoDao() {
		return new EquipamentoDaoJDBC(DB.getConnection());
	}
	
	public static TurnoDao createTurnoDao() {
		return new TurnoDaoJDBC(DB.getConnection());
	}
}
