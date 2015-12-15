package br.edu.undb.cafufa.controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.undb.cafufa.model.Tag;
import br.edu.undb.cafufa.repository.Tags;

@Named
@ViewScoped
public class ProspeccaoVendaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Tags tags;

	private Tag tagSelecionada;

	private Float media;

	public void calcularValor() {
		try {
			media = tags.mediaValor(tagSelecionada);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Tag> completeTag(String nome) {
		try {
			return this.tags.queContem(nome);
		} catch (SQLException e) {
			return new ArrayList<Tag>();
		}
	}

	public Tag getTagSelecionada() {
		return tagSelecionada;
	}

	public void setTagSelecionada(Tag tagSelecionada) {
		this.tagSelecionada = tagSelecionada;
	}

	public Float getMedia() {
		return media;
	}

	public void setMedia(Float media) {
		this.media = media;
	}

}