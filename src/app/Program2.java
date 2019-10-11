package app;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EquipamentoDao;
import model.entities.Equipamento;
import model.entities.enums.Estado;
import model.entities.enums.Tipo;

public class Program2 {

	public static void main(String[] args) {

		EquipamentoDao equipamentoDao = DaoFactory.createEquipamentoDao();

		System.out.println("=== TEST 1: Equipamento findById ===");
		Equipamento equipamento = equipamentoDao.findById(7);
		System.out.println(equipamento);

		System.out.println("\n=== TEST 2: Equipamento insert ===");
		Equipamento newEquipamento = new Equipamento(null, "GND", Tipo.ACAMS, Estado.OPERACIONAL);
		//equipamentoDao.insert(newEquipamento);
		System.out.println("Inserted! New id = " + newEquipamento.getId());

		System.out.println("\n=== TEST 3: Equipamento update ===");
		equipamento = equipamentoDao.findById(11);
		equipamento.setNome("CLR");
		equipamentoDao.update(equipamento);
		System.out.println("Update complete!");

		System.out.println("\n=== TEST 4: Equipamento delete ===");
		equipamentoDao.deleteById(6);
		System.out.println("Delete complete!");

		System.out.println("\n=== TEST 5: Equipamento findAll ===");
		List<Equipamento> list = equipamentoDao.findAll();

		for (Equipamento obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("=== TEST 6: Equipamento findByNome ===");
		equipamento = equipamentoDao.findByNome("CTR02");
		System.out.println(equipamento);
		
		System.out.println("=== TEST 6: Equipamento findByTipo ===");
		List<Equipamento> list2 = equipamentoDao.findByTipo(Tipo.ACAMS);
		
		for (Equipamento obj : list2) {
			System.out.println(obj);
		}
	}
}
