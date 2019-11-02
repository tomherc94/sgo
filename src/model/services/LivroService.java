package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.LivroDao;
import model.entities.Livro;

public class LivroService {

	private LivroDao dao = DaoFactory.createLivroDao();

	public List<Livro> findAll() {
		return dao.findAll();
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
	
	public boolean findLivroAberto() {
		Livro livroAberto = dao.findLivroAberto();
		if(livroAberto == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public void fecharLivro() {
		dao.fecharLivro();
	}
}
