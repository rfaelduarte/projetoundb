package br.edu.undb.cafufa.converter;

import java.sql.SQLException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import br.edu.undb.cafufa.model.Loja;
import br.edu.undb.cafufa.repository.Lojas;

@FacesConverter(forClass = Loja.class, value = "lojaConverter")
public class LojaConverter implements Converter {

	@Inject // funciona graças ao OmniFaces
	private Lojas lojas;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Loja retorno = null;
		if (value != null) {
			try {
				retorno = this.lojas.porId(new Integer(value));
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
			return ((Loja) value).getId().toString();
		} else {
			return null;
		}
	}

}