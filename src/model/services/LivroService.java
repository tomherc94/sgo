package model.services;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.LivroDao;
import model.dao.OcorrenciaDao;
import model.entities.Livro;
import model.entities.Supervisor;

public class LivroService {

	private LivroDao dao = DaoFactory.createLivroDao();
	private OcorrenciaDao daoOcorrencia = DaoFactory.createOcorrenciaDao();

	public List<Livro> findAll() {
		return dao.findAll();
	}

	public List<Livro> findBySupervisor(Supervisor obj) {
		return dao.findBySupervisor(obj);
	}
	
	public List<Livro> findByDatas(Date inicio, Date fim){
		return dao.findByDataHoraAbertura(inicio, fim);
	}

	public void saveOrUpdate(Livro obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove() {
		dao.deleteByStatusAberto();
	}

	public boolean confirmaLivroAberto() {
		Livro livroAberto = dao.findLivroAberto();
		if (livroAberto == null) {
			return false;
		} else {
			return true;
		}
	}

	public Livro findLivroAberto() {
		return dao.findLivroAberto();
	}

	public void fecharLivro() {
		daoOcorrencia.updateEquipamento();
		dao.fecharLivro();
	}
}
