/**
 * 
 */
package com.trivadis.dsg.ressources;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.trivadis.dsg.model.Column;
import com.trivadis.dsg.model.DataStructure;
import com.trivadis.dsg.model.Table;

/**
 * @author jallogger.debuglogger.debug
 * 
 *         manage JDBC connection with databases
 * 
 */
public class DatasourceJDBCConnector implements IDatasourceConnector {

	private final static Logger logger = Logger
			.getLogger(DatasourceJDBCConnector.class);

	// private String catalog;

	/**
	 * constructor
	 */
	public DatasourceJDBCConnector() {
	}

	private DataStructure loadDataStructure(Connection connection)
			throws SQLException {
		DataStructure structure = new DataStructure();
		DatabaseMetaData meta = connection.getMetaData();
		structure.setTables(getTables(connection.getCatalog(), meta));

		return structure;
	}

	/**
	 * get results data and display in console
	 * 
	 * @param schema
	 * @param meta
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private List<Table> getTables(String catalog, DatabaseMetaData meta) {

		List<Table> tables = new ArrayList<Table>();
		// todo: set the connection info inside a property file
		// String schema = "FNF_DEV";
		String schema = "jpetstore";

		ResultSet res = null;
		try {
			res = meta.getTables(null, schema, null, new String[] { "TABLE" });

			while (res.next()) {
				// add the table in the ArrayList
				Table table = new Table();
				// table name
				table.setName(res.getString("TABLE_NAME"));
				tables.add(table);

				// retrieve and set foreign keys
				Map<String,Column> fk = getForeignKeys(catalog, meta, table.getName());

				// retrieve and set primary keys
				Map<String,Column> pk = getPrimaryKeys(catalog, meta, table.getName());
				
				// retrieve other columns
				List<Column> columns = getColumns(catalog, meta, table.getName());
				
				for (Column column : columns) {
					if (pk.containsKey(column.getColumnName())) {
						column.setPrimaryKey(true);
					}
					if (fk.containsKey(column.getColumnName())) {
						column.setForeignKey(true);
						Column fkColumn = fk.get(column.getColumnName());
						column.setFkTableName(fkColumn.getFkTableName());
						column.setFkColumnName(fkColumn.getFkColumnName());
					}
				}
				
				table.setColumns(columns);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (res != null) {
				// close ResultSet
				try {
					res.close();
				} catch (SQLException e) {
					// Do nothing
				}
			}
		}
		logger.debug("Database tables loaded:" + tables.size());
		return tables;
	}

	private Map<String,Column> getForeignKeys(String catalog, DatabaseMetaData meta,
			String table) {
		Map<String, Column> fk = new HashMap<String, Column>();
		ResultSet rs = null;
		try {
			rs = meta.getImportedKeys(catalog, null, table);
			while (rs.next()) {

				// Current table and column
				String fkColumnName = rs.getString("FKCOLUMN_NAME");
				// String fkTableName = rs.getString("FKTABLE_NAME");

				// Foreign table and column
				String pkColumnName = rs.getString("PKCOLUMN_NAME");
				String pkTableName = rs.getString("PKTABLE_NAME");

				Column column = new Column(fkColumnName, pkTableName,
						pkColumnName);
				
				column.setForeignKey(true);

				fk.put(column.getColumnName(), column);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// do something...
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fk;
	}

	private Map<String, Column> getPrimaryKeys(String catalog, DatabaseMetaData meta,
			String table) {
		Map<String, Column> pk = new HashMap<String, Column>();
		ResultSet rs = null;
		try {
			rs = meta.getPrimaryKeys(catalog, null, table);
			while (rs.next()) {
				String pkColumnName = rs.getString("COLUMN_NAME");
				Column column = new Column();
				column.setColumnName(pkColumnName);
				column.setPrimaryKey(true); // primary key
				pk.put(column.getColumnName(), column);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// do something...
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		return pk;
	}

	private List<Column> getColumns(String catalog, DatabaseMetaData meta,
			String table) {
		List<Column> columns = new ArrayList<Column>();
		ResultSet rs = null;
		try {
			rs = meta.getColumns(catalog, "", table, "");
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				Column column = new Column();
				column.setColumnName(columnName);
				columns.add(column);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// do something...
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return columns;
	}

	@Override
	public DataStructure getDataStructure() {

		Connection connection = null;
		try {
			connection = createConnection();
			// catalog = connection.getCatalog();
			return loadDataStructure(connection);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Problem", e);
		} finally {
			closeConnection(connection);
		}

	}

	private Connection createConnection() throws Exception {
		final String url = "jdbc:mysql://localhost/jpetstore";
		// final String url = "jdbc:mysql://localhost/deeboxstream";
		// final String url =
		// "jdbc:oracle:thin:@lcm0114.cloud.trivadis.com:1521/xe";

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		// Class.forName("oracle.jdbc.OracleDriver").newInstance();
		Connection connection = DriverManager.getConnection(url, "jal",
				"jal1234");
		// Connection connection = DriverManager.getConnection(url, "FNF_DEV",
		// "FNF_DEV");
		if (logger.isDebugEnabled()) {
			logger.debug("Connection successfuly created "
					+ connection.toString());
		}

		return connection;

	}

	private void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// Do nothing
			}
		}

	}
}
