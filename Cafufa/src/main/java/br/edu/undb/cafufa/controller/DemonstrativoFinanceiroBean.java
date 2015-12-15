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
import br.edu.undb.cafufa.model.FormaPagamento;
import br.edu.undb.cafufa.model.Loja;
import br.edu.undb.cafufa.repository.Eventos;
import br.edu.undb.cafufa.repository.FormaPagamentos;
import br.edu.undb.cafufa.repository.Lojas;

@Named
@ViewScoped
public class DemonstrativoFinanceiroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lojas lojas;

	@Inject
	private Eventos eventos;
	
	@Inject
	private FormaPagamentos formaPagamentos;

	private Loja lojaSelecionada;
	private List<Loja> lojasPesquisadas;
	private Evento eventoSelecionado;
	private Date dataInicial;
	private Date dataFinal;
	private List<FormaPagamento> resumoFinanceiro;

	@PostConstruct
	public void iniciar() {
		lojasPesquisadas = new ArrayList<>();
	}

	public void relatorioAgora() {
		try {
			if (lojaSelecionada == null && eventoSelecionado == null) {
				lojasPesquisadas = lojas.porPeriodo(dataInicial, dataFinal);
				resumoFinanceiro = formaPagamentos.porPeriodo(dataInicial, dataFinal);
			} else if (lojaSelecionada != null && eventoSelecionado != null) {
				lojasPesquisadas = lojas.porLojaEEventoContabil(lojaSelecionada, eventoSelecionado, dataInicial, dataFinal);
				resumoFinanceiro = formaPagamentos.porLojaEvento(lojaSelecionada, eventoSelecionado, dataInicial, dataFinal);
			} else if (lojaSelecionada != null) {
				lojasPesquisadas = lojas.porLojaEEventoContabil(lojaSelecionada, dataInicial, dataFinal);
				resumoFinanceiro = formaPagamentos.porLoja(lojaSelecionada, dataInicial, dataFinal);
			} else {
				lojasPesquisadas = lojas.porLojaEEventoContabil(eventoSelecionado, dataInicial, dataFinal);
				resumoFinanceiro = formaPagamentos.porEvento(eventoSelecionado, dataInicial, dataFinal);
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

	public List<Loja> getLojasPesquisadas() {
		return lojasPesquisadas;
	}

	public void setLojasPesquisadas(List<Loja> lojasPesquisadas) {
		this.lojasPesquisadas = lojasPesquisadas;
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

	public List<FormaPagamento> getResumoFinanceiro() {
		return resumoFinanceiro;
	}

	public void setResumoFinanceiro(List<FormaPagamento> resumoFinanceiro) {
		this.resumoFinanceiro = resumoFinanceiro;
	}
	
}
