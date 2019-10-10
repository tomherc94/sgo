package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.SupervisorDao;
import model.entities.Supervisor;
import model.entities.enums.PostoGrad;

public class SupervisorDaoJDBC implements SupervisorDao {

	private Connection conn;

	public SupervisorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Supervisor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO supervisor "
					+ "(nome, identidade, postoGrad, celular, login, senha) " + "VALUES (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getIdentidade());
			st.setString(3, obj.getPostoGrad().toString());
			st.setString(4, obj.getCelular());
			st.setString(5, obj.getLogin());
			st.setString(6, obj.getSenha());

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
	public void update(Supervisor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE supervisor "
					+ "SET nome = ?, identidade = ?, postoGrad = ?, celular = ?, login = ?, senha = ? "
					+ "WHERE idSup = ?");

			st.setString(1, obj.getNome());
			st.setString(2, obj.getIdentidade());
			st.setString(3, obj.getPostoGrad().toString());
			st.setString(4, obj.getCelular());
			st.setString(5, obj.getLogin());
			st.setString(6, obj.getSenha());
			st.setInt(7, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM supervisor WHERE idSup = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Supervisor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM supervisor WHERE idSup = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Supervisor obj = instanciateSupervisor(rs);

				return obj;
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	private Supervisor instanciateSupervisor(ResultSet rs) throws SQLException {
		Supervisor obj = new Supervisor();
		obj.setId(rs.getInt("idSup"));
		obj.setNome(rs.getString("nome"));
		obj.setIdentidade(rs.getString("identidade"));
		obj.setPostoGrad(PostoGrad.valueOf(rs.getString("PostoGrad")));
		obj.setCelular(rs.getString("celular"));
		obj.setLogin(rs.getString("login"));
		obj.setSenha(rs.getString("senha"));
		return obj;
	}

	@Override
	public List<Supervisor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM supervisor ORDER BY nome");

			rs = st.executeQuery();

			List<Supervisor> list = new ArrayList<>();
			while (rs.next()) {
				Supervisor obj = instanciateSupervisor(rs);

				list.add(obj);
			}

			return list;
		} catch (

		SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public Supervisor findByIdentidade(String identidade) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM supervisor WHERE identidade = ?");
			st.setString(1, identidade);
			rs = st.executeQuery();
			if (rs.next()) {

				Supervisor obj = instanciateSupervisor(rs);

				return obj;
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

}
