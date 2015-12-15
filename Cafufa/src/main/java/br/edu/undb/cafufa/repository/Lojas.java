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

public class Lojas implements Serializable {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;

	@Inject
	public Lojas(Connection con) {
		this.con = con;
	}

	// usado no conversor
	public Loja porId(Integer id) throws SQLException {
		Loja loja = null;
		String sql = "select loja.loja_id as id, " 
				+ "usuario.usuario_nome as nome " 
				+ "from usuario inner join loja "
				+ "on usuario.usuario_id=loja.usuario_id " 
				+ "where loja_id=?";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, id);
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			loja = new Loja();
			loja.setId(rs.getInt("id"));
			loja.setNome(rs.getString("nome"));
		}

		return loja;
	}

	// Usado no Filtro Autocomplete
	public List<Loja> queContem(String nome) throws SQLException {
		List<Loja> lojas = new ArrayList<>();
		Loja loja;
		String sql = "select loja.loja_id as id, " 
				+ "usuario.usuario_nome as nome " 
				+ "from usuario inner join loja "
				+ "on usuario.usuario_id=loja.usuario_id " 
				+ "where usuario.usuario_nome like ?";
		pstm = con.prepareStatement(sql);
		pstm.setString(1, "%" + nome + "%");
		rs = pstm.executeQuery();
		while (rs.next()) {
			loja = new Loja();
			loja.setId(rs.getInt("id"));
			loja.setNome(rs.getString("nome"));
			lojas.add(loja);
		}
		return lojas;
	}

	// Usado quando Loja nem Evento é selecionado
	public List<Loja> porPeriodo(Date dataInicial, Date dataFinal) throws SQLException {
		Eventos eventos = new Eventos(con);
		List<Loja> lojas = new ArrayList<>();
		/* aqui ele vai retornar todas as lojas, no periodo desejado */
		Loja loja;
		String sql = "select loja.loja_id as id, usuario.usuario_nome as nome "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id " 
				+ "where venda.created_at between ? and ? "
				+ "group by usuario.usuario_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			loja = new Loja();
			loja.setId(rs.getInt("id"));
			loja.setNome(rs.getString("nome"));
			loja.setEventos(eventos.porPeriodoContabil(loja, dataInicial, dataFinal));
			lojas.add(loja);
		}
		return lojas;
	}

	public List<Loja> porLojaEEventoContabil(Loja loja, Evento evento, Date dataInicial, Date dataFinal)
			throws SQLException {
		Eventos eventos = new Eventos(con);
		FormaPagamentos formaPagamentos = new FormaPagamentos(con);
		List<Loja> lojas = new ArrayList<>();
		/* aqui ele vai retornar todas as lojas, no periodo desejado */
		String sql = "select loja.loja_id as id, usuario.usuario_nome as nome "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id = venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id = evento_ingresso.evento_ingresso_id "
				+ "where venda.created_at between ? and ? and " + "loja.loja_id = ? and "
				+ "evento_ingresso.evento_id = ? " + "group by usuario.usuario_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, loja.getId());
		pstm.setInt(4, evento.getId());

		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			loja = new Loja();
			loja.setId(rs.getInt("id"));
			loja.setNome(rs.getString("nome"));
			loja.setEventos(eventos.porPeriodoContabil(loja, evento, dataInicial, dataFinal));
			loja.setFormaPagamentos(formaPagamentos.porLoja(loja, dataInicial, dataFinal));
			lojas.add(loja);
		}
		return lojas;
	}

	public List<Loja> porLojaEEventoContabil(Loja loja, Date dataInicial, Date dataFinal) throws SQLException {
		Eventos eventos = new Eventos(con);
		FormaPagamentos formaPagamentos = new FormaPagamentos(con);
		List<Loja> lojas = new ArrayList<>();
		/* aqui ele vai retornar todas as lojas, no periodo desejado */
		String sql = "select loja.loja_id as id, usuario.usuario_nome as nome "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id = venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id = evento_ingresso.evento_ingresso_id "
				+ "where venda.created_at between ? and ? and " + "loja.loja_id = ? " + "group by usuario.usuario_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, loja.getId());

		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			loja = new Loja();
			loja.setId(rs.getInt("id"));
			loja.setNome(rs.getString("nome"));
			loja.setEventos(eventos.porPeriodoContabil(loja, dataInicial, dataFinal));
			loja.setFormaPagamentos(formaPagamentos.porLoja(loja, dataInicial, dataFinal));
			lojas.add(loja);
		}
		return lojas;
	}

	public List<Loja> porLojaEEventoContabil(Evento evento, Date dataInicial, Date dataFinal) throws SQLException {
		Eventos eventos = new Eventos(con);
		FormaPagamentos formaPagamentos = new FormaPagamentos(con);
		List<Loja> lojas = new ArrayList<>();
		Loja loja;
		/* aqui ele vai retornar todas as lojas, no periodo desejado */
		String sql = "select loja.loja_id as id, usuario.usuario_nome as nome "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id = venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id = evento_ingresso.evento_ingresso_id "
				+ "where venda.created_at between ? and ? and " + "evento_ingresso.evento_id = ? "
				+ "group by usuario.usuario_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, evento.getId());

		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			loja = new Loja();
			loja.setId(rs.getInt("id"));
			loja.setNome(rs.getString("nome"));
			loja.setEventos(eventos.porPeriodoContabil(loja, evento, dataInicial, dataFinal));
			loja.setFormaPagamentos(formaPagamentos.porLoja(loja, dataInicial, dataFinal));
			lojas.add(loja);
		}
		return lojas;
	}

	public List<Loja> porPeriodoVenda(Loja loja, Evento evento, Date dataInicial, Date dataFinal)
			throws SQLException {
		EventoIngressoTipos eventoIngressoTipos = new EventoIngressoTipos(con);
		List<Loja> lojas = new ArrayList<>();
		/* aqui ele vai retornar todas as lojas, no periodo desejado */
		String sql = "select loja.loja_id as id, usuario.usuario_nome as nome "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id = venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id = evento_ingresso.evento_ingresso_id "
				+ "where venda.created_at between ? and ? and " + "loja.loja_id = ? and "
				+ "evento_ingresso.evento_id = ? " + "group by usuario.usuario_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, loja.getId());
		pstm.setInt(4, evento.getId());

		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			loja = new Loja();
			loja.setId(rs.getInt("id"));
			loja.setNome(rs.getString("nome"));
			loja.setEventoIngressoTipos(eventoIngressoTipos.porEventoLoja(loja, evento, dataInicial, dataFinal));
			lojas.add(loja);
		}
		return lojas;
	}
	
	public List<Loja> porPeriodoVenda(Evento evento, Date dataInicial, Date dataFinal)
			throws SQLException {
		EventoIngressoTipos eventoIngressoTipos = new EventoIngressoTipos(con);
		List<Loja> lojas = new ArrayList<>();
		Loja loja;
		/* aqui ele vai retornar todas as lojas, no periodo desejado */
		String sql = "select loja.loja_id as id, usuario.usuario_nome as nome "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id = venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id = evento_ingresso.evento_ingresso_id "
				+ "where venda.created_at between ? and ? and "
				+ "evento_ingresso.evento_id = ? " + "group by usuario.usuario_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, evento.getId());
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			loja = new Loja();
			loja.setId(rs.getInt("id"));
			loja.setNome(rs.getString("nome"));
			loja.setEventoIngressoTipos(eventoIngressoTipos.porEventoLoja(loja, evento, dataInicial, dataFinal));
			lojas.add(loja);
		}
		return lojas;
	}
	
}