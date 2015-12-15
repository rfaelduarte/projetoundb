package br.edu.undb.cafufa.converter;

import java.sql.SQLException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import br.edu.undb.cafufa.model.Evento;
import br.edu.undb.cafufa.repository.Eventos;

@FacesConverter(forClass = Evento.class, value = "eventoConverter")
public class EventoConverter implements Converter {

	@Inject // funciona graças ao OmniFaces
	private Eventos eventos;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Evento retorno = null;
		if (value != null) {
			try {
				retorno = this.eventos.porId(new Integer(value));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			return ((Evento) value).getId().toString();
		} else {
			return null;
		}
	}

}