package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.LivroDao;
import model.dao.SupervisorDao;
import model.dao.TurnoDao;
import model.entities.Livro;
import model.entities.Supervisor;
import model.entities.Turno;
import model.entities.enums.StatusLivro;

public class Program5 {

	public static void main(String[] args) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

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
		//Livro livro = livroDao.findById(3);
		//System.out.println(livro);

		System.out.println("\n=== TEST 3: Livro update ===");
		//livro = livroDao.findById(17);
		//livro.setDataHoraFechamento(new Date());
		//livro.setStatus(StatusLivro.FECHADO);
		//livroDao.update(livro);
		System.out.println("Update complete!");

		System.out.println("\n=== TEST 4: Livro delete ===");
		//livroDao.deleteByStatusAberto();
		System.out.println("Delete complete!");

		System.out.println("\n=== TEST 5: Livro findAll ===");
		List<Livro> list = livroDao.findAll();

		for (Livro obj : list) {
			System.out.println(obj);
		}

		System.out.println("\n=== TEST 6: Livro findBySupervisor ===");

		List<Livro> list2 = livroDao.findBySupervisor(supervisor);

		for (Livro obj : list2) {
			System.out.println(obj);
		}

		System.out.println("\n=== TEST 7: Livro findByDataHoraAbertura ===");

		List<Livro> list3 = livroDao.findByDataHoraAbertura(sdf.parse("10/10/2019 - 00:00:00"),
				sdf.parse("11/10/2019 - 00:00:00"));

		for (Livro obj : list3) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST 8: Livro fecharLivro ===");
		//livroDao.fecharLivro();
		

	}
}
