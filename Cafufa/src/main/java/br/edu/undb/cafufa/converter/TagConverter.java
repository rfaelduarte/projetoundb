package br.edu.undb.cafufa.converter;

import java.sql.SQLException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import br.edu.undb.cafufa.model.Tag;
import br.edu.undb.cafufa.repository.Tags;

@FacesConverter(forClass = Tag.class, value = "tagConverter")
public class TagConverter implements Converter {

	@Inject // funciona graças ao OmniFaces
	private Tags tags;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Tag retorno = null;
		if (value != null) {
			try {
				retorno = this.tags.porId(new Integer(value));
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
			return ((Tag) value).getId().toString();
		} else {
			return null;
		}
	}

}