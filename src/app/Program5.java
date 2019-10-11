package app;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.LivroDao;
import model.dao.SupervisorDao;
import model.dao.TurnoDao;
import model.entities.Livro;
import model.entities.Supervisor;
import model.entities.Turno;
import model.entities.enums.StatusLivro;

public class Program5 {

	public static void main(String[] args) {

		LivroDao livroDao = DaoFactory.createLivroDao();
		SupervisorDao supervisorDao = DaoFactory.createSupervisorDao();
		TurnoDao turnoDao = DaoFactory.createTurnoDao();
		
		Supervisor supervisor = supervisorDao.findById(3);
		Turno turno = turnoDao.findById(2);

		System.out.println("\n=== TEST 1: Livro insert ===");
		Livro newLivro = new Livro(null, new Date(), StatusLivro.ABERTO, supervisor, turno);
		livroDao.insert(newLivro);
		System.out.println("Inserted! New id = " + newLivro.getId());
		
		System.out.println("=== TEST 2: Livro findById ===");
		Livro livro = livroDao.findById(1);
		System.out.println(livro);

		

		/*System.out.println("\n=== TEST 3: Livro update ===");
		turno = turnoDao.findById(3);
		turno.setHoraFim("233000");
		//turnoDao.update(turno);
		System.out.println("Update complete!");

		System.out.println("\n=== TEST 4: Livro delete ===");
		turnoDao.deleteById(3);
		System.out.println("Delete complete!");

		System.out.println("\n=== TEST 5: Turno findAll ===");
		List<Turno> list = turnoDao.findAll();

		for (Turno obj : list) {
			System.out.println(obj);
		}*/
	}
}
