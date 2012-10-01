/**
 * 
 */
package com.trivadis.dsg.model;

/**
 * @author jal
 * 
 */
public class Column {

	// Current column name
	private String columnName;
	
	// Foreign key table name
	private String fkTableName;
	// Foreign key column name
	private String fkColumnName;
	
	private boolean primaryKey = false;
	private boolean foreignKey = false;
	
	private String type;
	
	private String nodeId;

	/**
	 * Constructor
	 * 
	 * @param
	 */
	public Column() {
	}

	public Column(String columnName, String fkTableName, String fkColumnName) {
		setColumnName(columnName);
		setFkTableName(fkTableName);
		setFkColumnName(fkColumnName);
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getFkTableName() {
		return fkTableName;
	}

	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}

	public String getFkColumnName() {
		return fkColumnName;
	}

	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = fkColumnName;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(boolean foreignKey) {
		this.foreignKey = foreignKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Column [columnName=");
		builder.append(columnName);
		builder.append(", fkTableName=");
		builder.append(fkTableName);
		builder.append(", fkColumnName=");
		builder.append(fkColumnName);
		builder.append(", primaryKey=");
		builder.append(primaryKey);
		builder.append(", foreignKey=");
		builder.append(foreignKey);
		builder.append("]");
		return builder.toString();
	}

	

}
