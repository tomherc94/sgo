package app;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TurnoDao;
import model.entities.Turno;

public class Program3 {

	public static void main(String[] args) {

		TurnoDao turnoDao = DaoFactory.createTurnoDao();

		System.out.println("=== TEST 1: Turno findById ===");
		Turno turno = turnoDao.findById(2);
		System.out.println(turno);

		System.out.println("\n=== TEST 2: Turno insert ===");
		Turno newTurno = new Turno(null, "120000","200000");
		//turnoDao.insert(newTurno);
		System.out.println("Inserted! New id = " + newTurno.getId());

		System.out.println("\n=== TEST 3: Turno update ===");
		turno = turnoDao.findById(3);
		turno.setHoraFim("233000");
		//turnoDao.update(turno);
		System.out.println("Update complete!");

		System.out.println("\n=== TEST 4: Turno delete ===");
		turnoDao.deleteById(3);
		System.out.println("Delete complete!");

		System.out.println("\n=== TEST 5: Turno findAll ===");
		List<Turno> list = turnoDao.findAll();

		for (Turno obj : list) {
			System.out.println(obj);
		}
	}
}
