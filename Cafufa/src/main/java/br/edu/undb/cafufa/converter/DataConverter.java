package br.edu.undb.cafufa.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("dataConverter") 
public class DataConverter implements Converter {
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		df.setLenient(false);
		try {
			return df.parse(value);
		} catch (Exception e) {
			throw new ConverterException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		try {
			df.setLenient(false);
			String d = df.format((Date) value);
			return d;
		} catch (Exception e) {
			return "";
		}
	}
}