package app;

import model.dao.DaoFactory;
import model.dao.SupervisorDao;
import model.entities.Supervisor;
import model.entities.enums.PostoGrad;

public class Program {

	public static void main(String[] args) {
		
		SupervisorDao supervisorDao = DaoFactory.createSupervisorDao();
		
		System.out.println("\n=== TEST 1: supervisor insert ===");
		Supervisor newSupervisor = new Supervisor(null,"Greg", "3456776", PostoGrad.G3S, "11932334455", "gregds", "1234567");
		supervisorDao.insert(newSupervisor);
		System.out.println("Inserted! New id = " + newSupervisor.getId());

	}

}
