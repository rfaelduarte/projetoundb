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
import br.edu.undb.cafufa.model.EventoIngressoTipo;
import br.edu.undb.cafufa.model.Loja;

public class EventoIngressoTipos implements Serializable {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;

	@Inject
	public EventoIngressoTipos(Connection con) {
		this.con = con;
	}

	public List<EventoIngressoTipo> porEventoLoja(Loja loja, Evento evento, Date dataInicial, Date dataFinal)
			throws SQLException {
		EventoIngressos eventoIngressos = new EventoIngressos(con);
		List<EventoIngressoTipo> eventoIngressoTipos = new ArrayList<>();
		EventoIngressoTipo eventoIngressoTipo;
		String sql = "select evento_ingresso_tipo.evento_ingresso_tipo_id as id, "
				+ "evento_ingresso_tipo.evento_ingresso_tipo_nome as nome "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id=venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id=evento_ingresso.evento_ingresso_id "
				+ "inner join evento_ingresso_tipo "
				+ "on evento_ingresso.evento_ingresso_tipo_id=evento_ingresso_tipo.evento_ingresso_tipo_id "
				+ "where venda.created_at between ? and ? and " + "loja.loja_id = ? "
				+ "and evento_ingresso.evento_id = ? " + "group by usuario.usuario_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, loja.getId());
		pstm.setInt(4, evento.getId());
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			eventoIngressoTipo = new EventoIngressoTipo();
			eventoIngressoTipo.setId(rs.getInt("id"));
			eventoIngressoTipo.setNome(rs.getString("nome"));
			eventoIngressoTipo.setEventoIngressos(
			eventoIngressos.porEventoLoja(loja, eventoIngressoTipo, dataInicial, dataFinal));
			eventoIngressoTipos.add(eventoIngressoTipo);
		}
		return eventoIngressoTipos;
	}

	public List<EventoIngressoTipo> porEvento(Evento evento, Date dataInicial, Date dataFinal) throws SQLException {
		List<EventoIngressoTipo> eventoIngressoTipos = new ArrayList<>();
		EventoIngressoTipo eventoIngressoTipo;
		String sql = "select evento_ingresso_tipo.evento_ingresso_tipo_id as id, "
				+ "evento_ingresso_tipo.evento_ingresso_tipo_nome as nome, "
				+ "sum(venda_item.venda_item_quantidade) as quantidade "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id=venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id=evento_ingresso.evento_ingresso_id "
				+ "inner join evento_ingresso_tipo "
				+ "on evento_ingresso.evento_ingresso_tipo_id=evento_ingresso_tipo.evento_ingresso_tipo_id "
				+ "where venda.created_at between ? and ? and " + "evento_ingresso.evento_id = ? "
				+ "group by usuario.usuario_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, evento.getId());
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			eventoIngressoTipo = new EventoIngressoTipo();
			eventoIngressoTipo.setId(rs.getInt("id"));
			eventoIngressoTipo.setNome(rs.getString("nome"));
			eventoIngressoTipo.setQuantidade(rs.getInt("quantidade"));
			eventoIngressoTipos.add(eventoIngressoTipo);
		}
		return eventoIngressoTipos;
	}

	
}