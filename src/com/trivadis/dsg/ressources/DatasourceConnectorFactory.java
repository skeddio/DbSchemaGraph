package com.trivadis.dsg.ressources;



public class DatasourceConnectorFactory {

//	private final static Logger logger = Logger
//			.getLogger(DatasourceConnectorFactory.class);

	public enum DataSourceTypeEnum {
		SQL,
		WS
	};
	
	public static IDatasourceConnector getConnector(DataSourceTypeEnum type) {
		switch (type) {
		case WS:
			//return new DatasourceWSConnector();
			break;
		case SQL:
			return new DatasourceJDBCConnector();
		}
		throw new RuntimeException("Undefined type "+type);
	}
	
}
