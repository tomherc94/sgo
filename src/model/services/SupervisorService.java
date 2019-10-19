package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Supervisor;
import model.entities.enums.PostoGrad;

public class SupervisorService {

	public List<Supervisor> finAll(){
		List<Supervisor> list = new ArrayList<>();
		list.add(new Supervisor(1, "Tomas Herculano", "987654", PostoGrad._3S, "011987877665", "herculanothcc", "1234567"));
		list.add(new Supervisor(2, "Thaina Silva", "122334", PostoGrad._1S, "011934344545", "tahinasp", "1234567"));
		list.add(new Supervisor(3, "Rodrigo Oliveira", "454532", PostoGrad._1T, "011932332212", "rodrigoro", "1234567"));
		
		return list;
	}
}
