package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.dao.DaoFactory;
import model.dao.EquipamentoDao;
import model.dao.LivroDao;
import model.dao.OcorrenciaDao;
import model.entities.Livro;
import model.entities.Ocorrencia;
import model.entities.enums.Estado;

public class Program4 {

	public static void main(String[] args) throws ParseException {

		//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

		OcorrenciaDao ocorrenciaDao = DaoFactory.createOcorrenciaDao();
		EquipamentoDao equipamentoDao = DaoFactory.createEquipamentoDao();
		LivroDao livroDao = DaoFactory.createLivroDao();
		Livro livroAberto = livroDao.findLivroAberto();

		System.out.println("\n=== TEST 1: Ocorrencia insert ===");
		Ocorrencia newOcorrencia = new Ocorrencia(null, Estado.OPERACIONAL_RESTRICOES, new Date(),
				new StringBuilder().append("Descricao ..."), equipamentoDao.findByNome("CTR03"), livroAberto);
		ocorrenciaDao.insert(newOcorrencia);
		System.out.println("Inserted! New id = " + newOcorrencia.getId());

		/*
		 * System.out.println("=== TEST 2: Livro findById ==="); Livro livro =
		 * livroDao.findById(3); System.out.println(livro);
		 * 
		 * System.out.println("\n=== TEST 3: Livro update ==="); livro =
		 * livroDao.findById(4); livro.setDataHoraFechamento(new Date());
		 * livro.setStatus(StatusLivro.FECHADO); livroDao.update(livro);
		 * System.out.println("Update complete!");
		 * 
		 * System.out.println("\n=== TEST 4: Livro delete ==="); //
		 * livroDao.deleteByStatusAberto(); System.out.println("Delete complete!");
		 * 
		 * System.out.println("\n=== TEST 5: Livro findAll ==="); List<Livro> list =
		 * livroDao.findAll();
		 * 
		 * for (Livro obj : list) { System.out.println(obj); }
		 * 
		 * System.out.println("\n=== TEST 6: Livro findBySupervisor ===");
		 * 
		 * List<Livro> list2 = livroDao.findBySupervisor(supervisor);
		 * 
		 * for (Livro obj : list2) { System.out.println(obj); }
		 * 
		 * System.out.println("\n=== TEST 7: Livro findByDataHoraAbertura ===");
		 * 
		 * List<Livro> list3 =
		 * livroDao.findByDataHoraAbertura(sdf.parse("10/10/2019 - 00:00:00"),
		 * sdf.parse("11/10/2019 - 00:00:00"));
		 * 
		 * for (Livro obj : list3) { System.out.println(obj); }
		 */

	}
}
