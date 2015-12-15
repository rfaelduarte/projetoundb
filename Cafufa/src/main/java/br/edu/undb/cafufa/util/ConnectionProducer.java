package br.edu.undb.cafufa.util;

import java.sql.Connection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class ConnectionProducer {
	private InterfacePool pool;

	public ConnectionProducer() {
		this.pool = new Pool();
	}

	@Produces
	@RequestScoped
	public Connection createConnection() {
		return pool.getConnection();
	}

	public void closeConnection(@Disposes Connection connection) {
		pool.liberarConnection(connection);
	}

}
