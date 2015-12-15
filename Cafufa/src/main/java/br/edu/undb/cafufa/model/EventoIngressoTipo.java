package br.edu.undb.cafufa.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class EventoIngressoTipo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private Integer Quantidade;
	private List<EventoIngresso> eventoIngressos;

	public Integer getQuantidade() {
		return Quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		Quantidade = quantidade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<EventoIngresso> getEventoIngressos() {
		return eventoIngressos;
	}

	public void setEventoIngressos(List<EventoIngresso> eventoIngressos) {
		this.eventoIngressos = eventoIngressos;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EventoIngressoTipo other = (EventoIngressoTipo) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

}
