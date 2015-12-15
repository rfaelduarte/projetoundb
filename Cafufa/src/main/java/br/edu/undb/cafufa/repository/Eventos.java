package br.edu.undb.cafufa.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.edu.undb.cafufa.model.Evento;
import br.edu.undb.cafufa.model.Loja;

public class Eventos implements Serializable {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;

	@Inject
	public Eventos(Connection con) {
		this.con = con;
	}
	//Usado no Conversor no Relatório Contábil
	public Evento porId(Integer id) throws SQLException {
		Evento evento = null;
		String sql = "select evento.evento_id as id, evento_titulo as nome from evento where evento_id=?;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, id);
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			evento = new Evento();
			evento.setId(rs.getInt("id"));
			evento.setNome(rs.getString("nome"));
		}
		return evento;
	}
	
	//Usado quando evento e loja não é selecionado no Relatório Contábil
	public List<Evento> porPeriodoContabil(Loja loja, Date dataInicial, Date dataFinal) throws SQLException {
		FormaPagamentos formaPagamentos = new FormaPagamentos(con);
		List<Evento> eventos = new ArrayList<>();
		Evento evento;
		String sql = "select evento.evento_id  as id,evento_titulo as nome "
				+ "from evento inner join evento_ingresso on evento.evento_id = evento_ingresso.evento_id inner "
				+ "join venda_item on evento_ingresso.evento_ingresso_id = venda_item.evento_ingresso_id inner "
				+ "join venda on venda_item.venda_id = venda.venda_id "
				+ "where venda.loja_id = ? and venda.created_at "
				+ "between ? and ? and  venda.venda_status_id = 1 group by evento.evento_id;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, loja.getId());
		pstm.setDate(2, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(3, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			evento = new Evento();
			evento.setId(rs.getInt("id"));
			evento.setNome(rs.getString("nome"));
			evento.setFormaPagamentos(formaPagamentos.porLojaEvento(loja, evento, dataInicial, dataFinal));
			eventos.add(evento);
		}
		return eventos;
	}
	
	//Usado quando evento e loja é selecionado no Relatório Contábil
		public List<Evento> porPeriodoContabil(Loja loja, Evento evento, Date dataInicial, Date dataFinal) throws SQLException {
			FormaPagamentos formaPagamentos = new FormaPagamentos(con);
			List<Evento> eventos = new ArrayList<>();
			String sql = "select evento.evento_id  as id,evento_titulo as nome "
					+ "from evento inner join evento_ingresso on evento.evento_id = evento_ingresso.evento_id "
					+ "inner join venda_item on evento_ingresso.evento_ingresso_id = venda_item.evento_ingresso_id "
					+ "inner join venda on venda_item.venda_id = venda.venda_id "
					+ "where venda.loja_id = ? and "
					+ "evento.evento_id = ? and "
					+ "venda.created_at between ? and ? and "
					+ "venda.venda_status_id = 1 "
					+ "group by evento.evento_id;";
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, loja.getId());
			pstm.setInt(2, evento.getId());
			pstm.setDate(3, new java.sql.Date(dataInicial.getTime()));
			pstm.setDate(4, new java.sql.Date(dataFinal.getTime()));
			System.out.println(pstm.toString());
			rs = pstm.executeQuery();
			while (rs.next()) {
				evento = new Evento();
				evento.setId(rs.getInt("id"));
				evento.setNome(rs.getString("nome"));
				evento.setFormaPagamentos(formaPagamentos.porLojaEvento(loja, evento, dataInicial, dataFinal));
				eventos.add(evento);
			}
			return eventos;
		}

		
		
	//Usado Autocomplete
	public List<Evento> queContem(String nome) throws SQLException {
		List<Evento> eventos = new ArrayList<>();
		Evento evento;
		String sql = "select evento_id as id, evento_titulo as nome from evento "
		+ "where evento_titulo like ?;";
		pstm = con.prepareStatement(sql);
		pstm.setString(1, "%" + nome + "%");
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			evento = new Evento();
			evento.setId(rs.getInt("id"));
			evento.setNome(rs.getString("nome"));
			eventos.add(evento);
		}
		return eventos;
	}
	public List<Evento> porPeriodoVenda(Loja loja, Evento evento, Date dataInicial, Date dataFinal) throws SQLException {
		Lojas lojas = new Lojas(con);
		EventoIngressoTipos eventoIngressoTipos = new EventoIngressoTipos(con);
		EventoIngressos eventoIngressos = new EventoIngressos(con);
		List<Evento> eventos = new ArrayList<>();
		String sql = "select evento.evento_id  as id,evento_titulo as nome "
				+ "from evento inner join evento_ingresso on evento.evento_id = evento_ingresso.evento_id "
				+ "inner join venda_item on evento_ingresso.evento_ingresso_id = venda_item.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id = venda.venda_id "
				+ "where venda.loja_id = ? and "
				+ "evento.evento_id = ? and "
				+ "venda.created_at between ? and ? and "
				+ "venda.venda_status_id = 1 "
				+ "group by evento.evento_id;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, loja.getId());
		pstm.setInt(2, evento.getId());
		pstm.setDate(3, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(4, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			evento = new Evento();
			evento.setId(rs.getInt("id"));
			evento.setNome(rs.getString("nome"));
			evento.setLojasVendedoras(lojas.porPeriodoVenda(loja, evento, dataInicial, dataFinal));
			evento.setEventoIngressoTipos(eventoIngressoTipos.porEvento(evento, dataInicial, dataFinal));
			evento.setEventoIngressos(eventoIngressos.porEvento(evento, dataInicial, dataFinal));
			eventos.add(evento);
		}
		return eventos;
	}
	public List<Evento> porPeriodoVenda(Date dataInicial, Date dataFinal) throws SQLException {
		Lojas lojas = new Lojas(con);
		EventoIngressoTipos eventoIngressoTipos = new EventoIngressoTipos(con);
		EventoIngressos eventoIngressos = new EventoIngressos(con);
		List<Evento> eventos = new ArrayList<>();
		Evento evento;
		String sql = "select evento.evento_id  as id,evento_titulo as nome "
				+ "from evento inner join evento_ingresso on evento.evento_id = evento_ingresso.evento_id "
				+ "inner join venda_item on evento_ingresso.evento_ingresso_id = venda_item.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id = venda.venda_id "
				+ "where venda.created_at between ? and ? and "
				+ "venda.venda_status_id = 1 "
				+ "group by evento.evento_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			evento = new Evento();
			evento.setId(rs.getInt("id"));
			evento.setNome(rs.getString("nome"));
			evento.setLojasVendedoras(lojas.porPeriodoVenda(evento, dataInicial, dataFinal));
			evento.setEventoIngressoTipos(eventoIngressoTipos.porEvento(evento, dataInicial, dataFinal));
			evento.setEventoIngressos(eventoIngressos.porEvento(evento, dataInicial, dataFinal));
			eventos.add(evento);
		}
		return eventos;
	}
	public List<Evento> porPeriodoVenda(Loja loja, Date dataInicial, Date dataFinal) throws SQLException {
		Lojas lojas = new Lojas(con);
		EventoIngressoTipos eventoIngressoTipos = new EventoIngressoTipos(con);
		EventoIngressos eventoIngressos = new EventoIngressos(con);
		List<Evento> eventos = new ArrayList<>();
		Evento evento;
		String sql = "select evento.evento_id  as id,evento_titulo as nome "
				+ "from evento inner join evento_ingresso on evento.evento_id = evento_ingresso.evento_id "
				+ "inner join venda_item on evento_ingresso.evento_ingresso_id = venda_item.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id = venda.venda_id "
				+ "where venda.loja_id = ? and "
				+ "venda.created_at between ? and ? and "
				+ "venda.venda_status_id = 1 "
				+ "group by evento.evento_id;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, loja.getId());
		pstm.setDate(2, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(3, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			evento = new Evento();
			evento.setId(rs.getInt("id"));
			evento.setNome(rs.getString("nome"));
			evento.setLojasVendedoras(lojas.porPeriodoVenda(loja, evento, dataInicial, dataFinal));
			evento.setEventoIngressoTipos(eventoIngressoTipos.porEvento(evento, dataInicial, dataFinal));
			evento.setEventoIngressos(eventoIngressos.porEvento(evento, dataInicial, dataFinal));
			eventos.add(evento);
		}
		return eventos;
	}
	
	public List<Evento> porPeriodoVenda(Evento evento, Date dataInicial, Date dataFinal) throws SQLException {
		Lojas lojas = new Lojas(con);
		EventoIngressoTipos eventoIngressoTipos = new EventoIngressoTipos(con);
		EventoIngressos eventoIngressos = new EventoIngressos(con);
		List<Evento> eventos = new ArrayList<>();
		
		String sql = "select evento.evento_id  as id,evento_titulo as nome "
				+ "from evento inner join evento_ingresso on evento.evento_id = evento_ingresso.evento_id "
				+ "inner join venda_item on evento_ingresso.evento_ingresso_id = venda_item.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id = venda.venda_id and "
				+ "evento.evento_id = ? and "
				+ "venda.created_at between ? and ? and "
				+ "venda.venda_status_id = 1 "
				+ "group by evento.evento_id;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, evento.getId());
		pstm.setDate(2, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(3, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			evento = new Evento();
			evento.setId(rs.getInt("id"));
			evento.setNome(rs.getString("nome"));
			evento.setLojasVendedoras(lojas.porPeriodoVenda(evento, dataInicial, dataFinal));
			evento.setEventoIngressoTipos(eventoIngressoTipos.porEvento(evento, dataInicial, dataFinal));
			evento.setEventoIngressos(eventoIngressos.porEvento(evento, dataInicial, dataFinal));
			eventos.add(evento);
		}
		return eventos;
	}
}