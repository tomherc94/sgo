package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.DaoFactory;
import model.dao.LivroDao;
import model.dao.OcorrenciaDao;
import model.dao.SupervisorDao;
import model.dao.TurnoDao;
import model.entities.Livro;
import model.entities.Ocorrencia;
import model.entities.Supervisor;
import model.entities.enums.StatusLivro;

public class LivroDaoJDBC implements LivroDao {

	private Connection conn;

	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	public LivroDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Livro obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO livro (dataHoraAbertura, dataHoraFechamento, status, supervisor_idSup, turno_idTurno) "
							+ "VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, sdf.format(obj.getDataHoraAbertura()));
			st.setString(2, null);
			st.setString(3, obj.getStatus().toString());
			st.setInt(4, obj.getSupervisor().getId());
			st.setInt(5, obj.getTurno().getId());

			int rowsAfected = st.executeUpdate();

			if (rowsAfected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

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
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM livro WHERE idLivro = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Livro obj = instanciateLivro(rs);

				return obj;
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	private Livro instanciateLivro(ResultSet rs) throws SQLException, ParseException {
		SupervisorDao supervisorDao = DaoFactory.createSupervisorDao();
		TurnoDao turnoDao = DaoFactory.createTurnoDao();
		OcorrenciaDao ocorrenciaDao = DaoFactory.createOcorrenciaDao();

		Livro obj = new Livro();
		
		obj.setId(rs.getInt("idLivro"));
		obj.setDataHoraAbertura(sdf.parse(rs.getString("dataHoraAbertura")));
		
		//testa se existe uma dataHoraFechamento
		if(rs.getString("dataHoraFechamento") == null) {
			obj.setDataHoraFechamento(null);
		}else {
			obj.setDataHoraFechamento(sdf.parse(rs.getString("dataHoraFechamento")));
		}
		
		obj.setStatus(StatusLivro.valueOf(rs.getString("status")));
		obj.setSupervisor(supervisorDao.findById(rs.getInt("supervisor_idSup")));
		obj.setTurno(turnoDao.findById(rs.getInt("turno_idTurno")));

		List<Ocorrencia> lista = ocorrenciaDao.findByLivro(obj);

		if(lista != null) {
			for (Ocorrencia ocorrencia : lista) {
				obj.adicionarOcorrencia(ocorrencia);
			}
		}
		
		
		return obj;
	}

}
