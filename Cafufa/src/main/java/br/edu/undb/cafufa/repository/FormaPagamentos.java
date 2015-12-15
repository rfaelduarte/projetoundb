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
import br.edu.undb.cafufa.model.FormaPagamento;
import br.edu.undb.cafufa.model.Loja;

public class FormaPagamentos implements Serializable {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;

	@Inject
	public FormaPagamentos(Connection con) {
		this.con = con;
	}

	// Aqui temos uma lista de Forma de Pagamento onde será retornado id, nome,
	// total.
	public List<FormaPagamento> porLojaEvento(Loja loja, Evento evento, Date dataInicial, Date dataFinal)
			throws SQLException {
		List<FormaPagamento> formaPagamentos = new ArrayList<>();
		FormaPagamento formaPagamento;
		String sql = "select pagamento_tipo.pagamento_tipo_id as id, " + "pagamento_tipo.pagamento_tipo_nome as nome, "
				+ "sum(venda_item_valor * venda_item_quantidade) as total "
				+ "from evento inner join evento_ingresso on evento.evento_id = evento_ingresso.evento_id "
				+ "inner join venda_item on evento_ingresso.evento_ingresso_id = venda_item.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id = venda.venda_id "
				+ "inner join pagamento_tipo on venda.pagamento_tipo_id = pagamento_tipo.pagamento_tipo_id "
				+ "where venda.loja_id = ? and " + "evento.evento_id=? and " + "venda.created_at between ? and ? and "
				+ "venda.venda_status_id = 1 " + "group by pagamento_tipo.pagamento_tipo_id "
				+ "order by pagamento_tipo.pagamento_tipo_id desc;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, loja.getId());
		pstm.setInt(2, evento.getId());
		pstm.setDate(3, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(4, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			formaPagamento = new FormaPagamento();
			formaPagamento.setId(rs.getInt("id"));
			formaPagamento.setNome(rs.getString("nome"));
			formaPagamento.setValor(rs.getFloat("total"));
			formaPagamentos.add(formaPagamento);
		}
		return formaPagamentos;
	}

	public List<FormaPagamento> porPeriodo(Date dataInicial, Date dataFinal) throws SQLException {
		List<FormaPagamento> formaPagamentos = new ArrayList<>();
		FormaPagamento formaPagamento;
		String sql = "select pagamento_tipo.pagamento_tipo_id as id, " 
				+ "pagamento_tipo.pagamento_tipo_nome as nome, "
				+ "sum(venda_item.venda_item_valor*venda_item.venda_item_quantidade) as valor "
				+ "from evento inner join evento_ingresso on evento.evento_id=evento_ingresso.evento_id "
				+ "inner join venda_item on venda_item.evento_ingresso_id=evento_ingresso.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id=venda.venda_id "
				+ "inner join pagamento_tipo on venda.pagamento_tipo_id=pagamento_tipo.pagamento_tipo_id "
				+ "where venda.created_at between ? and ? and " 
				+ "venda.venda_status_id=1 "
				+ "group by pagamento_tipo.pagamento_tipo_id "
				+ "order by pagamento_tipo.pagamento_tipo_id desc;";
		pstm = con.prepareStatement(sql);
		pstm.setDate(1, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(2, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			formaPagamento = new FormaPagamento();
			formaPagamento.setId(rs.getInt("id"));
			formaPagamento.setNome(rs.getString("nome"));
			formaPagamento.setValor(rs.getFloat("valor"));
			formaPagamentos.add(formaPagamento);
		}
		return formaPagamentos;
	}

	public List<FormaPagamento> porEvento(Evento evento, Date dataInicial, Date dataFinal) throws SQLException {
		List<FormaPagamento> formaPagamentos = new ArrayList<>();
		FormaPagamento formaPagamento;
		String sql = "select pagamento_tipo.pagamento_tipo_id as id, " + "pagamento_tipo.pagamento_tipo_nome as nome, "
				+ "sum(venda_item.venda_item_valor*venda_item.venda_item_quantidade) as valor "
				+ "from evento inner join evento_ingresso on evento.evento_id=evento_ingresso.evento_id "
				+ "inner join venda_item on venda_item.evento_ingresso_id=evento_ingresso.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id=venda.venda_id "
				+ "inner join pagamento_tipo on venda.pagamento_tipo_id=pagamento_tipo.pagamento_tipo_id "
				+ "where evento.evento_id=? and " + "venda.created_at between ? and ? and " + "venda.venda_status_id=1 "
				+ "group by pagamento_tipo.pagamento_tipo_id "
				+ "order by pagamento_tipo.pagamento_tipo_id desc;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, evento.getId());
		pstm.setDate(2, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(3, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			formaPagamento = new FormaPagamento();
			formaPagamento.setId(rs.getInt("id"));
			formaPagamento.setNome(rs.getString("nome"));
			formaPagamento.setValor(rs.getFloat("valor"));
			formaPagamentos.add(formaPagamento);
		}
		return formaPagamentos;
	}

	public List<FormaPagamento> porLoja(Loja loja, Date dataInicial, Date dataFinal) throws SQLException {
		List<FormaPagamento> formaPagamentos = new ArrayList<>();
		FormaPagamento formaPagamento;
		String sql = "select pagamento_tipo.pagamento_tipo_id as id, " + "pagamento_tipo.pagamento_tipo_nome as nome, "
				+ "sum(venda_item.venda_item_valor*venda_item.venda_item_quantidade) as valor "
				+ "from evento inner join evento_ingresso on evento.evento_id=evento_ingresso.evento_id "
				+ "inner join venda_item on venda_item.evento_ingresso_id=evento_ingresso.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id=venda.venda_id "
				+ "inner join pagamento_tipo on venda.pagamento_tipo_id=pagamento_tipo.pagamento_tipo_id "
				+ "where venda.loja_id=? and " + "venda.created_at between ? and ? and " + "venda.venda_status_id=1 "
				+ "group by pagamento_tipo.pagamento_tipo_id "
				+ "order by pagamento_tipo.pagamento_tipo_id desc;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, loja.getId());
		pstm.setDate(2, new java.sql.Date(dataInicial.getTime()));
		pstm.setDate(3, new java.sql.Date(dataFinal.getTime()));
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			formaPagamento = new FormaPagamento();
			formaPagamento.setId(rs.getInt("id"));
			formaPagamento.setNome(rs.getString("nome"));
			formaPagamento.setValor(rs.getFloat("valor"));
			formaPagamentos.add(formaPagamento);
			if (formaPagamento.getId() == 4){
				formaPagamentos.add(new Sangrias(con).porPeriodo(loja, dataInicial, dataFinal));
				}
		}
		
		return formaPagamentos;
	}
	
}
