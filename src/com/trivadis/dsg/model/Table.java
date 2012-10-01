/**
 * 
 */
package com.trivadis.dsg.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author jal
 * 
 */
public class Table implements Comparable<Table> {

	private String name;
	private List<Column> columns;
	//private List<Column> primaryKeys;
	//private List<Column> foreignKeys;
	//private List<String> foreignTables;
	private String nodeId;

	/**
	 * Constructor
	 * 
	 * @param
	 */
	public Table() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Column> getForeignKeys() {
		List<Column> fkColumns = new ArrayList<Column>();
		for (Column column : fkColumns) {
			if (column.isForeignKey()) {
				fkColumns.add(column);
			}
		}
		return fkColumns;
	}


	public List<Column> getPrimaryKeys() {
		List<Column> pkColumns = new ArrayList<Column>();
		for (Column column : pkColumns) {
			if (column.isPrimaryKey()) {
				pkColumns.add(column);
			}
		}
		return pkColumns;
	}


	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int compareTo(Table o) {
		if (this.getName() == null)
			return -1;
		return this.getName().compareToIgnoreCase(o.getName());
	}

}
