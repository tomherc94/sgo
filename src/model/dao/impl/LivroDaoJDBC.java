package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		// Livro livroAberto = findLivroAberto();
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement(
					"INSERT INTO livro (dataHoraAbertura, dataHoraFechamento, status, supervisor_idSup, turno_idTurno) "
							+ "VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, sdf.format(new Date()));
			st.setString(2, null);
			st.setString(3, StatusLivro.ABERTO.toString());
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
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE livro "
					+ "SET dataHoraAbertura = ?, supervisor_idSup = ?, turno_idTurno = ? WHERE idLivro = ?");

			st.setString(1, sdf.format(new Date()));
			st.setInt(2, obj.getSupervisor().getId());
			st.setInt(3, obj.getTurno().getId());
			st.setInt(4, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void fecharLivro() {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE livro SET dataHoraFechamento = ?, status = ? WHERE idLivro = ?");

			st.setString(1, sdf.format(new Date()));
			st.setString(2, StatusLivro.FECHADO.toString());
			st.setInt(3, findLivroAberto().getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteByStatusAberto() {
		Livro livroAberto = findLivroAberto();
		OcorrenciaDao ocorrenciaDao = DaoFactory.createOcorrenciaDao();
		ocorrenciaDao.deleteByIdLivro(livroAberto.getId());
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM livro WHERE status = 'ABERTO'");

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Livro findLivroAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM livro WHERE status = 'ABERTO'");

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

	@Override
	public List<Livro> findByDataHoraAbertura(Date inicio, Date fim) {

		List<Livro> all = findAll();
		List<Livro> result = new ArrayList<>();

		// compara a dataHoraAbertura de todos os livros com a faixa de datas fornecidas
		// como parametro
		for (Livro obj : all) {
			Date dataHoraAbertura = obj.getDataHoraAbertura();
			if (dataHoraAbertura.compareTo(inicio) >= 0 && dataHoraAbertura.compareTo(fim) <= 0) {
				result.add(obj);
			}
		}
		return result;

	}

	@Override
	public List<Livro> findBySupervisor(Supervisor supervisor) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM livro WHERE supervisor_idSup = ? ORDER BY dataHoraAbertura DESC");

			st.setInt(1, supervisor.getId());

			rs = st.executeQuery();

			List<Livro> list = new ArrayList<>();
			while (rs.next()) {
				Livro obj = instanciateLivro(rs);

				list.add(obj);
			}

			return list;
		} catch (

		SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Livro> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM livro ORDER BY dataHoraAbertura DESC");

			rs = st.executeQuery();

			List<Livro> list = new ArrayList<>();
			while (rs.next()) {
				Livro obj = instanciateLivro(rs);

				list.add(obj);
			}

			return list;
		} catch (

		SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
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

		// testa se existe uma dataHoraFechamento
		if (rs.getString("dataHoraFechamento") == null) {
			obj.setDataHoraFechamento(null);
		} else {
			obj.setDataHoraFechamento(sdf.parse(rs.getString("dataHoraFechamento")));
		}

		obj.setStatus(StatusLivro.valueOf(rs.getString("status")));
		obj.setSupervisor(supervisorDao.findById(rs.getInt("supervisor_idSup")));
		obj.setTurno(turnoDao.findById(rs.getInt("turno_idTurno")));

		List<Ocorrencia> lista = ocorrenciaDao.findByLivro(obj.getId());

		if (lista != null) {
			for (Ocorrencia ocorrencia : lista) {
				obj.adicionarOcorrencia(ocorrencia);
			}
		}

		return obj;
	}

}
