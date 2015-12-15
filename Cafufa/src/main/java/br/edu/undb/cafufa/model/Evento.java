package br.edu.undb.cafufa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Evento implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String nome;
    private Date data;
    private List<FormaPagamento> formaPagamentos;
    private List<Loja> lojasVendedoras;
    private List<EventoIngressoTipo> tiposLotes;
    private List<EventoIngressoTipo> eventoIngressoTipos;
    private List<EventoIngresso> eventoIngressos;

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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<FormaPagamento> getFormaPagamentos() {
		return formaPagamentos;
	}

	public void setFormaPagamentos(List<FormaPagamento> formaPagamentos) {
		this.formaPagamentos = formaPagamentos;
	}

	public List<Loja> getLojasVendedoras() {
        return lojasVendedoras;
    }

    public void setLojasVendedoras(List<Loja> lojasVendedoras) {
        this.lojasVendedoras = lojasVendedoras;
    }

    public List<EventoIngressoTipo> getTiposLotes() {
        return tiposLotes;
    }

    public void setTiposLotes(List<EventoIngressoTipo> tiposLotes) {
        this.tiposLotes = tiposLotes;
    }

    public List<EventoIngressoTipo> getEventoIngressoTipos() {
		return eventoIngressoTipos;
	}

	public void setEventoIngressoTipos(List<EventoIngressoTipo> eventoIngressoTipos) {
		this.eventoIngressoTipos = eventoIngressoTipos;
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
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Evento other = (Evento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
