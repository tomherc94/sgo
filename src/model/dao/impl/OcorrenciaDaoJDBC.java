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
import model.dao.EquipamentoDao;
import model.dao.LivroDao;
import model.dao.OcorrenciaDao;
import model.entities.Equipamento;
import model.entities.Livro;
import model.entities.Ocorrencia;
import model.entities.enums.Estado;

public class OcorrenciaDaoJDBC implements OcorrenciaDao {

	private Connection conn;

	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	public OcorrenciaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	LivroDao livroDao = DaoFactory.createLivroDao();
	Livro livroAberto = livroDao.findLivroAberto();

	@Override
	public void insert(Ocorrencia obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO ocorrencia (estadoOcorrencia, dataHora, descricao, equipamento_idEquip, livro_idLivro) "
							+ "VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getEstado().toString());
			st.setString(2, sdf.format(obj.getDataHora()));
			st.setString(3, obj.getDescricao().toString());
			st.setInt(4, obj.getEquipamento().getId());
			st.setInt(5, livroAberto.getId());

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
	
	public void updateEquipamento() {
		List<Ocorrencia> list = findByLivroAberto();
		EquipamentoDao equipamentoDao = DaoFactory.createEquipamentoDao();
		
		for(Ocorrencia ocorrencia : list) {
			Equipamento equipamento = equipamentoDao.findById(ocorrencia.getEquipamento().getId());
			equipamento.setEstadoAtual(ocorrencia.getEstado());
			equipamentoDao.update(equipamento);
		}
	}

	@Override
	public void update(Ocorrencia obj) {
		
		PreparedStatement st = null;
		try {
			if(livroAberto != null && obj.getLivro().getId() == livroAberto.getId()) {
				st = conn.prepareStatement("UPDATE ocorrencia SET estadoOcorrencia = ?, dataHora = ?, "
						+ "descricao = ?, equipamento_idEquip = ? WHERE idOcor = ?");

				st.setString(1, obj.getEstado().toString());
				st.setString(2, sdf.format(obj.getDataHora()));
				st.setString(3, obj.getDescricao().toString());
				st.setInt(4, obj.getEquipamento().getId());
				st.setInt(5, obj.getId());

				st.executeUpdate();
			}
			

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
			st = conn.prepareStatement("DELETE FROM ocorrencia WHERE idOcor = ?");

			st.setInt(1, id);
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}
	
	@Override
	public void deleteByIdLivro(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM ocorrencia WHERE livro_idLivro = ?");

			st.setInt(1, id);
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Ocorrencia findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM ocorrencia WHERE idOcor = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Ocorrencia obj = instanciateOcorrencia(rs);

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
	public List<Ocorrencia> findByData(Date inicio, Date fim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ocorrencia> findByEquipamento(Equipamento equipamento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ocorrencia> findByLivro(Livro livro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ocorrencia> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Ocorrencia> findByLivroAberto() {
		// TODO Auto-generated method stub
		return null;
	}

	private Ocorrencia instanciateOcorrencia(ResultSet rs) throws SQLException, ParseException {
		EquipamentoDao equipamentoDao = DaoFactory.createEquipamentoDao();
		LivroDao livroDao = DaoFactory.createLivroDao();

		StringBuilder sb = new StringBuilder();

		Ocorrencia obj = new Ocorrencia();
		obj.setId(rs.getInt("idOcor"));
		obj.setEstado(Estado.valueOf(rs.getString("estadoOcorrencia")));
		obj.setDataHora(sdf.parse(rs.getString("dataHora")));
		sb.append(rs.getString("descricao"));
		obj.setDescricao(sb);
		obj.setEquipamento(equipamentoDao.findById(rs.getInt("equipamento_idEquip")));
		obj.setLivro(livroDao.findById(rs.getInt("livro_idLivro")));
		return obj;
	}

	

	

}
