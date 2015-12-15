package br.edu.undb.cafufa.util;

import java.sql.Connection;

public interface InterfacePool {

	public abstract Connection getConnection();
	public void liberarConnection(Connection con);
}
