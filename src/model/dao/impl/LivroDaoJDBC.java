package model.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.LivroDao;
import model.dao.OcorrenciaDao;
import model.dao.SupervisorDao;
import model.dao.TurnoDao;
import model.entities.Livro;
import model.entities.Ocorrencia;
import model.entities.Supervisor;
import model.entities.enums.StatusLivro;

public class LivroDaoJDBC implements LivroDao{

	private Connection conn;

	public LivroDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Livro obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Livro obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByStatus(StatusLivro status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Livro findByStatus(StatusLivro status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Livro> findByDataHoraAbertura(Date inicio, Date fim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Livro> findBySupervisor(Supervisor supervisor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Livro> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Livro findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	private Livro instanciateLivro(ResultSet rs) throws SQLException {
		SupervisorDao supervisorDao = DaoFactory.createSupervisorDao();
		TurnoDao turnoDao = DaoFactory.createTurnoDao();
		OcorrenciaDao ocorrenciaDao = DaoFactory.createOcorrenciaDao();
		
		Livro obj = new Livro();
		obj.setId(rs.getInt("idLivro"));
		obj.setDataHoraAbertura(rs.getDate("dataHoraAbertura"));
		obj.setDataHoraFechamento(rs.getDate("dataHoraFechamento"));
		obj.setStatus(StatusLivro.valueOf(rs.getString("status")));
		obj.setSupervisor(supervisorDao.findById(rs.getInt("supervisor_idSup")));
		obj.setTurno(turnoDao.findById(rs.getInt("turno_idTurno")));
		
		List<Ocorrencia> lista = ocorrenciaDao.findByLivro(obj);
		
		for(Ocorrencia ocorrencia : lista) {
			obj.adicionarOcorrencia(ocorrencia);
		}
		return obj;
	}

	
}
