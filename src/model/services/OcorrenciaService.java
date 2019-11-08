package model.services;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.OcorrenciaDao;
import model.entities.Ocorrencia;

public class OcorrenciaService {
	
	private OcorrenciaDao dao = DaoFactory.createOcorrenciaDao();

	public List<Ocorrencia> findAll() {
		return dao.findAll();
	}
	
	public List<Ocorrencia> findByLivro(Integer idLivro) {
		return dao.findByLivro(idLivro);
	}
	
	public List<Ocorrencia> findByEquipamento(Ocorrencia obj) {
		return dao.findByEquipamento(obj.getEquipamento());
	}
	
	public List<Ocorrencia> findByDataHora(Date inicio, Date fim) {
		return dao.findByDataHora(inicio, fim);
	}
	
	public List<Ocorrencia> findByLivroAberto() {
		return dao.findByLivroAberto();
	}

	public void saveOrUpdate(Ocorrencia obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Ocorrencia obj) {
		dao.deleteById(obj.getId());
	}
}
