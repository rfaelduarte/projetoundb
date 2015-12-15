package br.edu.undb.cafufa.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import br.edu.undb.cafufa.model.Tag;

public class Tags implements Serializable {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;

	@Inject
	public Tags(Connection con) {
		this.con = con;
	}

	// Usado no conversor
	public Tag porId(Integer id) throws SQLException {
		Tag tag = null;
		String sql = "select tag_id as id, tag_nome as nome from tag where tag_id=?;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, id);
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			tag = new Tag();
			tag.setId(rs.getInt("id"));
			tag.setNome(rs.getString("nome"));
		}
		return tag;
	}

	public List<Tag> queContem(String nome) throws SQLException {
		List<Tag> tags = new ArrayList<>();
		Tag tag = null;
		String sql = "select tag_id as id, tag_nome as nome from tag where tag_nome like ?;";
		pstm = con.prepareStatement(sql);
		pstm.setString(1, "%" + nome + "%");
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			tag = new Tag();
			tag.setId(rs.getInt("id"));
			tag.setNome(rs.getString("nome"));
			tags.add(tag);
		}
		return tags;
	}

	public Float mediaValor(Tag tag) throws SQLException {
		Float media = null;
		String sql = "select sum(venda_item.venda_item_valor*venda_item.venda_item_quantidade)/"
				+ "sum(venda_item.venda_item_quantidade) as media "
				+ "from tag inner join evento_tag on tag.tag_id=evento_tag.tag_id "
				+ "inner join evento on evento_tag.evento_id=evento.evento_id "
				+ "inner join evento_ingresso on evento.evento_id=evento_ingresso.evento_id "
				+ "inner join venda_item on venda_item.evento_ingresso_id=evento_ingresso.evento_ingresso_id "
				+ "inner join venda on venda_item.venda_id=venda.venda_id "
				+ "where venda.venda_status_id=1 and tag.tag_id=? " + "group by tag.tag_id;";
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, tag.getId());
		System.out.println(pstm.toString());
		rs = pstm.executeQuery();
		while (rs.next()) {
			media = rs.getFloat("media");
		}
		return media;
	}
}