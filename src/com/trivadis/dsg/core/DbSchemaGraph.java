/**
 * 
 */
package com.trivadis.dsg.core;

import org.apache.log4j.Logger;

import com.trivadis.dsg.export.GraphGenerator;
import com.trivadis.dsg.model.DataStructure;
import com.trivadis.dsg.ressources.DatasourceConnectorFactory;
import com.trivadis.dsg.ressources.DatasourceConnectorFactory.DataSourceTypeEnum;
import com.trivadis.dsg.ressources.IDatasourceConnector;

/**
 * @author jal
 * 
 */
public class DbSchemaGraph {

	private final static Logger logger = Logger.getLogger(DbSchemaGraph.class);

	// Singleton
	private static DbSchemaGraph instance = null;

	private IDatasourceConnector datasourceConnection = DatasourceConnectorFactory
			.getConnector(DataSourceTypeEnum.SQL);

	private DbSchemaGraph() {
	}

	public static DbSchemaGraph getInstance() {
		if (instance == null) {
			instance = new DbSchemaGraph();
		}
		return instance;
	}

	public void generate() {
		//retrieve data structure
		DataStructure structure = datasourceConnection.getDataStructure();

		//generate graph and save output file
		GraphGenerator generator = new GraphGenerator();
		generator.generate(structure);


	}

}
