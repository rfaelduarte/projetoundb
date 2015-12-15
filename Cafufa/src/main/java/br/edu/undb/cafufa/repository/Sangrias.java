package br.edu.undb.cafufa.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.inject.Inject;

import br.edu.undb.cafufa.model.FormaPagamento;
import br.edu.undb.cafufa.model.Loja;

public class Sangrias implements Serializable {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;

	@Inject
	public Sangrias(Connection con) {
		this.con = con;
	}

	public FormaPagamento porPeriodo(Loja loja, Date dataInicial, Date dataFinal) throws SQLException {
		FormaPagamento formaPagamento = new FormaPagamento();
		formaPagamento.setId(0);
		formaPagamento.setNome("Sangria");
		formaPagamento.setValor(0F);
		String sql = "select sum(sangria_valor) as valor from sangria "
				+ "where loja_id=? and sangria_data_resgate between ? and ? " + "group by loja_id;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, loja.getId());
		pstm.setDate(2, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(3, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			formaPagamento.setValor(rs.getFloat("valor"));
		}
		return formaPagamento;
	}
}