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
import model.dao.EquipamentoDao;
import model.entities.Equipamento;
import model.entities.enums.Estado;
import model.entities.enums.Tipo;

public class EquipamentoDaoJDBC implements EquipamentoDao {

	private Connection conn;

	public EquipamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Equipamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO equipamento " + "(nome, tipo, estadoAtual) " + "VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getTipo().toString());
			st.setString(3, obj.getEstadoAtual().toString());

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
	public void update(Equipamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE equipamento " + "SET nome = ?, tipo = ?, estadoAtual = ? " + "WHERE idEquip = ?");

			st.setString(1, obj.getNome());
			st.setString(2, obj.getTipo().toString());
			st.setString(3, obj.getEstadoAtual().toString());
			st.setInt(4, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM equipamento WHERE idEquip = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Equipamento findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM equipamento WHERE idEquip = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Equipamento obj = instanciateEquipamento(rs);

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

	private Equipamento instanciateEquipamento(ResultSet rs) throws SQLException {
		Equipamento obj = new Equipamento();
		obj.setId(rs.getInt("idEquip"));
		obj.setNome(rs.getString("nome"));
		obj.setTipo(Tipo.valueOf(rs.getString("tipo")));
		obj.setEstadoAtual(Estado.valueOf(rs.getString("estadoAtual")));
		return obj;
	}

	@Override
	public Equipamento findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM equipamento WHERE nome = ?");
			st.setString(1, nome);
			rs = st.executeQuery();
			if (rs.next()) {

				Equipamento obj = instanciateEquipamento(rs);

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
	public List<Equipamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM equipamento ORDER BY tipo");

			rs = st.executeQuery();

			List<Equipamento> list = new ArrayList<>();
			while (rs.next()) {
				Equipamento obj = instanciateEquipamento(rs);

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

}
