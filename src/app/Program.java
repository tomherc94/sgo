package app;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SupervisorDao;
import model.entities.Supervisor;
import model.entities.enums.PostoGrad;

public class Program {

	public static void main(String[] args) {

		SupervisorDao supervisorDao = DaoFactory.createSupervisorDao();

		System.out.println("=== TEST 1: supervisor findById ===");
		Supervisor supervisor = supervisorDao.findById(3);
		System.out.println(supervisor);

		System.out.println("\n=== TEST 2: supervisor insert ===");
		Supervisor newSupervisor = new Supervisor(null, "Brunelli", "345678", PostoGrad._1T, "11943322334", "brunellijcb",
				"1234567");
		supervisorDao.insert(newSupervisor);
		System.out.println("Inserted! New id = " + newSupervisor.getId());

		System.out.println("\n=== TEST 3: supervisor update ===");
		//supervisor = supervisorDao.findById(1);
		//supervisor.setNome("Tomas Herculano");
		//supervisorDao.update(supervisor);
		System.out.println("Update complete!");

		System.out.println("\n=== TEST 4: supervisor delete ===");
		// supervisorDao.deleteById(6);
		System.out.println("Delete complete!");

		System.out.println("\n=== TEST 5: supervisor findAll ===");
		List<Supervisor> list = supervisorDao.findAll();

		for (Supervisor obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("=== TEST 6: supervisor findByIdentidade ===");
		supervisor = supervisorDao.findByIdentidade("345678");
		System.out.println(supervisor);
	}
}
