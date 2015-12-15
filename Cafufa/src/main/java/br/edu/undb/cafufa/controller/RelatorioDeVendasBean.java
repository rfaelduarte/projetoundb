package br.edu.undb.cafufa.controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.edu.undb.cafufa.model.Evento;
import br.edu.undb.cafufa.model.Loja;
import br.edu.undb.cafufa.repository.Eventos;
import br.edu.undb.cafufa.repository.Lojas;

@Named
@ViewScoped
public class RelatorioDeVendasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lojas lojas;

	@Inject
	private Eventos eventos;

	private Loja lojaSelecionada;
	private List<Evento> eventosPesquisados;
	private Evento eventoSelecionado;
	private Date dataInicial;
	private Date dataFinal;

	@PostConstruct
	public void iniciar() {
		eventosPesquisados = new ArrayList<>();
	}

	public void relatorioAgora() {
		try {
			if (lojaSelecionada == null && eventoSelecionado == null) {
				eventosPesquisados = eventos.porPeriodoVenda(dataInicial, dataFinal);
			} else if (lojaSelecionada != null && eventoSelecionado != null) {
				eventosPesquisados = eventos.porPeriodoVenda(lojaSelecionada, eventoSelecionado, dataInicial, dataFinal);
			} else if (lojaSelecionada != null) {
				eventosPesquisados = eventos.porPeriodoVenda(lojaSelecionada, dataInicial, dataFinal);
			} else {
				eventosPesquisados = eventos.porPeriodoVenda(eventoSelecionado, dataInicial, dataFinal);
			}
		} catch (SQLException e) {

		}
	}
	
	public List<Evento> completeEvento(String nome) {
		try {
			return this.eventos.queContem(nome);
		} catch (SQLException e) {
			return new ArrayList<Evento>();
		}
	}

	public List<Loja> completeLoja(String nome) {
		try {
			return this.lojas.queContem(nome);
		} catch (SQLException e) {
			return new ArrayList<Loja>();
		}
	}

	public Loja getLojaSelecionada() {
		return lojaSelecionada;
	}

	public void setLojaSelecionada(Loja lojaSelecionada) {
		this.lojaSelecionada = lojaSelecionada;
	}

	public List<Evento> getEventosPesquisados() {
		return eventosPesquisados;
	}

	public void setEventosPesquisados(List<Evento> eventosPesquisados) {
		this.eventosPesquisados = eventosPesquisados;
	}

	public Evento getEventoSelecionado() {
		return eventoSelecionado;
	}

	public void setEventoSelecionado(Evento eventoSelecionado) {
		this.eventoSelecionado = eventoSelecionado;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
}
