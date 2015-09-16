package rest.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;

public enum DBConnectionProvider {
	instance;

	private AbstractDBConnector dataSource;

	private String host = "localhost";
	private String port = "5432";
	private String database = "gglectureplanner";
	private String user = "postgres";
	private String password = "32qjivkd";

	private DBConnectionProvider() {
		this.dataSource = new PGDBConnector(host, port, database, user, password); 
	}

	public AbstractDBConnector getDataSource() {
		return dataSource;
	}

	public void setDataSource(AbstractDBConnector dataSource) {
		this.dataSource = dataSource;
	}

	public   PreparedStatement prepareStatement(String sql) throws SQLException {
		return getDataSource()
				.getConnection()
				.prepareStatement(sql);
	}
	
	
}
