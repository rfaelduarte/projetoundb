package br.edu.undb.cafufa.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;

import br.edu.undb.cafufa.model.Evento;
import br.edu.undb.cafufa.model.EventoIngresso;

public class Cortesias implements Serializable {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;

	@Inject
	public Cortesias(Connection con) {
		this.con = con;
	}
	public EventoIngresso porEvento(Evento evento, Date dataInicial, Date dataFinal) throws SQLException {
		EventoIngresso cortesia = new EventoIngresso();
		cortesia.setId(1);
		cortesia.setNome("Cortesia");
		cortesia.setQuantidade(0);

		String sql = "select count(*) as quantidade from cortesia "
				+ "inner join evento_ingresso_tipo "
				+ "on cortesia.evento_ingresso_tipo_id="
				+ "evento_ingresso_tipo.evento_ingresso_tipo_id "
				+ "where evento_ingresso_tipo.evento_id=? and "
				+ "cortesia.created_at between ? and ? "
				+ "group by evento_id;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, evento.getId());
		pstm.setDate(2, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(3, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			cortesia.setQuantidade(rs.getInt("quantidade"));
		}
		return cortesia;
	}
			
}