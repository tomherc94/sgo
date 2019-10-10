package model.dao;

import db.DB;
import model.dao.impl.SupervisorDaoJDBC;

public class DaoFactory {

	public static SupervisorDao createSupervisorDao() {
		return new SupervisorDaoJDBC(DB.getConnection());
	}
}
