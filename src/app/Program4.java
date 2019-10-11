package app;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.EquipamentoDao;
import model.dao.OcorrenciaDao;
import model.entities.Equipamento;
import model.entities.Livro;
import model.entities.Ocorrencia;
import model.entities.enums.Estado;

public class Program4 {

	public static void main(String[] args) {

		/*EquipamentoDao equipamentoDao = DaoFactory.createEquipamentoDao();
		Equipamento equipamento = equipamentoDao.findById(7);
		//Livro newLivro - new Livro(null, dataHoraAbertura, status, supervisor, turno);
		
		OcorrenciaDao ocorrenciaDao = DaoFactory.createOcorrenciaDao();

		System.out.println("=== TEST 1: ocorrencia findById ===");
		Ocorrencia ocorrencia = ocorrenciaDao.findById(2);
		System.out.println(ocorrencia);

		System.out.println("\n=== TEST 2: ocorrencia insert ===");
		Ocorrencia newOcorrencia = new Ocorrencia(null, Estado.DESATIVADO, new Date(), new Date().getTime(), new StringBuilder().append("Equipamento desativado para manutencao anual"), equipamento , );
		ocorrenciaDao.insert(newOcorrencia);
		System.out.println("Inserted! New id = " + newOcorrencia.getId());

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
		}*/
	}
}
