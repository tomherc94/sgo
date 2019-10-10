package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.SupervisorDao;
import model.entities.Supervisor;

public class SupervisorDaoJDBC implements SupervisorDao{

	private Connection conn;

	public SupervisorDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Supervisor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO supervisor " + 
					"(nome, identidade, postoGrad, celular, login, senha) " + 
					"VALUES (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getIdentidade());
			st.setString(3, obj.getPostoGrad().toString());
			st.setString(4, obj.getCelular());
			st.setString(5, obj.getLogin());
			st.setString(6, obj.getSenha());
			
			int rowsAfected = st.executeUpdate();
			
			if(rowsAfected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Supervisor obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Supervisor findByIdentidade(String identidade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Supervisor> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
