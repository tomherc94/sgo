package model.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.EquipamentoDao;
import model.dao.LivroDao;
import model.dao.OcorrenciaDao;
import model.entities.Equipamento;
import model.entities.Livro;
import model.entities.Ocorrencia;
import model.entities.enums.Estado;
import model.entities.enums.StatusLivro;

public class OcorrenciaDaoJDBC implements OcorrenciaDao{
	
	private Connection conn;

	public OcorrenciaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Ocorrencia obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Ocorrencia obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Ocorrencia findById(Integer id) {
		// TODO Auto-generated method stub
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

	private Ocorrencia instanciateOcorrencia(ResultSet rs) throws SQLException {
		EquipamentoDao equipamentoDao = DaoFactory.createEquipamentoDao();
		LivroDao livroDao = DaoFactory.createLivroDao();
		
		StringBuilder sb = new StringBuilder();
		
		Ocorrencia obj = new Ocorrencia();
		obj.setId(rs.getInt("idOcor"));
		obj.setEstado(Estado.valueOf(rs.getString("estadoOcorrencia")));
		obj.setData(rs.getDate("data"));
		obj.setHora(rs.getTime("hora"));
		sb.append(rs.getString("descricao"));
		obj.setDescricao(sb);
		obj.setEquipamento(equipamentoDao.findById(rs.getInt("equipamento_idEquip")));
		obj.setLivro(livroDao.findById(rs.getInt("livro_idLivro")));
		return obj;
	}
	
}
