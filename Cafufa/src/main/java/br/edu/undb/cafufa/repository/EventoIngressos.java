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
import br.edu.undb.cafufa.model.EventoIngresso;
import br.edu.undb.cafufa.model.EventoIngressoTipo;
import br.edu.undb.cafufa.model.Loja;

public class EventoIngressos implements Serializable {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;

	@Inject
	public EventoIngressos(Connection con) {
		this.con = con;
	}

	public List<EventoIngresso> porEventoLoja(Loja loja, EventoIngressoTipo eventoIngressoTipo, Date dataInicial,
			Date dataFinal) throws SQLException {
		List<EventoIngresso> eventoIngressos = new ArrayList<>();
		EventoIngresso eventoIngresso;
		String sql = "select evento_ingresso.evento_ingresso_id as id, "
				+ "evento_ingresso.evento_ingresso_lote as nome, "
				+ "sum(venda_item.venda_item_quantidade) as quantidade "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id=venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id=evento_ingresso.evento_ingresso_id "
				+ "where venda.created_at between ? and ? and " + "loja.loja_id = ? and "
				+ "evento_ingresso.evento_ingresso_tipo_id = ? " + "group by evento_ingresso.evento_ingresso_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, loja.getId());
		pstm.setInt(4, eventoIngressoTipo.getId());
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			eventoIngresso = new EventoIngresso();
			eventoIngresso.setId(rs.getInt("id"));
			eventoIngresso.setNome(rs.getString("nome"));
			eventoIngresso.setQuantidade(rs.getInt("quantidade"));
			eventoIngressos.add(eventoIngresso);
		}
		return eventoIngressos;
	}

	public List<EventoIngresso> porEvento(Evento evento, Date dataInicial, Date dataFinal) throws SQLException {
		List<EventoIngresso> eventoIngressos = new ArrayList<>();
		EventoIngresso eventoIngresso;
		String sql = "select evento_ingresso.evento_ingresso_id as id, "
				+ "evento_ingresso.evento_ingresso_lote as nome, "
				+ "sum(venda_item.venda_item_quantidade) as quantidade "
				+ "from usuario inner join loja on usuario.usuario_id=loja.usuario_id "
				+ "inner join venda on loja.loja_id=venda.loja_id "
				+ "inner join venda_item on venda.venda_id=venda_item.venda_id "
				+ "inner join evento_ingresso on venda_item.evento_ingresso_id=evento_ingresso.evento_ingresso_id "
				+ "where venda.created_at between ? and ? and " + "evento_ingresso.evento_id = ? "
				+ "group by evento_ingresso.evento_ingresso_id;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		pstm.setInt(3, evento.getId());
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			eventoIngresso = new EventoIngresso();
			eventoIngresso.setId(rs.getInt("id"));
			eventoIngresso.setNome(rs.getString("nome"));
			eventoIngresso.setQuantidade(rs.getInt("quantidade"));
			eventoIngressos.add(eventoIngresso);
		}
		eventoIngressos.add(new Cortesias(con).porEvento(evento, dataInicial, dataFinal));
		return eventoIngressos;
	}
}