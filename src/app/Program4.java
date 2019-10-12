package app;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.EquipamentoDao;
import model.dao.OcorrenciaDao;
import model.entities.Equipamento;
import model.entities.Ocorrencia;
import model.entities.enums.Estado;

public class Program4 {

	public static void main(String[] args) throws ParseException {

		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

		OcorrenciaDao ocorrenciaDao = DaoFactory.createOcorrenciaDao();
		EquipamentoDao equipamentoDao = DaoFactory.createEquipamentoDao();
		Equipamento equipamento = equipamentoDao.findById(1);

		System.out.println("\n=== TEST 1: Ocorrencia insert ===");
		Ocorrencia newOcorrencia = new Ocorrencia(null, Estado.OPERACIONAL, new Date(),
				new StringBuilder().append("Descricao ..."), equipamentoDao.findByNome("CTR02"), null);
		//ocorrenciaDao.insert(newOcorrencia);
		System.out.println("Inserted! New id = " + newOcorrencia.getId());

		System.out.println("=== TEST 2: Ocorrencia findById ===");
		//Ocorrencia ocorrencia = ocorrenciaDao.findById(3);
		//System.out.println(ocorrencia);

		System.out.println("\n=== TEST 3: Ocorrencia update ===");
		//ocorrencia = ocorrenciaDao.findById(6);
		//ocorrencia.setDataHora(new Date());
		//ocorrencia.setEstado(Estado.DESATIVADO);
		//ocorrenciaDao.update(ocorrencia);
		System.out.println("Update complete!");

		System.out.println("\n=== TEST 4: Ocorrencia delete ==="); //
		//ocorrenciaDao.deleteById(6);
		System.out.println("Delete complete!");

		System.out.println("\n=== TEST 5: Ocorrencia findAll ===");
		List<Ocorrencia> list = ocorrenciaDao.findAll();

		for (Ocorrencia obj : list) {
			System.out.println(obj);
		}
		
		/*System.out.println("\n=== TEST 6: Livro findByLivro ===");
		List<Ocorrencia> list2 = ocorrenciaDao.findByLivro(24);

		for (Ocorrencia obj : list2) {
			System.out.println(obj);
		}


		/*System.out.println("\n=== TEST 6: Livro findByEquipamento ===");

		List<Ocorrencia> list2 = ocorrenciaDao.findByEquipamento(equipamento);

		for (Ocorrencia obj : list2) {
			System.out.println(obj);
		}

		System.out.println("\n=== TEST 7: Livro findByDataHoraAbertura ===");

		List<Livro> list3 = livroDao.findByDataHoraAbertura(sdf.parse("10/10/2019 - 00:00:00"),
				sdf.parse("11/10/2019 - 00:00:00"));

		for (Livro obj : list3) {
			System.out.println(obj);
		}*/

	}
}
