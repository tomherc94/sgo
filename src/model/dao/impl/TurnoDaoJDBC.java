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
import model.dao.TurnoDao;
import model.entities.Turno;

public class TurnoDaoJDBC implements TurnoDao {

	private Connection conn;

	public TurnoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Turno obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO turno (horaInicio, horaFim) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getHoraInicio());
			st.setString(2, obj.getHoraFim());

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
	public void update(Turno obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE turno " + "SET horaInicio = ?, horaFim = ? WHERE idTurno = ?");

			st.setString(1, obj.getHoraInicio());
			st.setString(2, obj.getHoraFim());
			st.setInt(3, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM turno WHERE idTurno = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Turno findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM turno WHERE idTurno = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Turno obj = instanciateTurno(rs);

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

	@Override
	public List<Turno> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM turno ORDER BY idTurno");

			rs = st.executeQuery();

			List<Turno> list = new ArrayList<>();
			while (rs.next()) {
				Turno obj = instanciateTurno(rs);

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

	private Turno instanciateTurno(ResultSet rs) throws SQLException {
		Turno obj = new Turno();
		obj.setId(rs.getInt("idTurno"));
		obj.setHoraInicio(rs.getString("horaInicio"));
		obj.setHoraFim(rs.getString("horaFim"));
		return obj;
	}

}
