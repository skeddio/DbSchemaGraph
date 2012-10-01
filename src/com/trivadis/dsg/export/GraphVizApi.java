package com.trivadis.dsg.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.trivadis.dsg.model.Column;
import com.trivadis.dsg.model.Table;

public class GraphVizApi
{

	private final static Logger logger = Logger
			.getLogger(GraphVizApi.class);
	
	// the output directory where files are generated
	private static String OUTPUT_DIR = "C:/DbSchemaGraphDir/";

	// the GraphViz location software, for external calling
	private static String DOT_PROGRAM_PATH = "C:/Program Files (x86)/Graphviz 2.28/bin/dot.exe";

	// a StringBuilder object is used as the source of the graph in DOT language
	private StringBuilder graphStringBuilder = new StringBuilder();

	/**
	 * Constructor
	 * 
	 */	
	public GraphVizApi() {
	}

	/**
	 * Returns the graph's source description in DOT language
	 * @return String - source of the graph in DOT language
	 */	
	public String getDotSource() {
		return graphStringBuilder.toString();
	}
	

	/**
	 * Adds a string to the graph's source
	 */
	public void addString(String str) {
		graphStringBuilder.append(str + "\n");
	}

	private String drawColumn(Column column, int index) {
		List<String> prefixes = new ArrayList<String>(2);
		if (column.isPrimaryKey()) prefixes.add("PK");
		if (column.isForeignKey()) prefixes.add("FK");
		final String prefix = StringUtils.join(prefixes,",");
		return "<f"+ index +"> "+ prefix + " "+ column.getColumnName() + "|";		
	}
	
	/**
	 * Adds a table +fields to the graph's source
	 * 
	 * @param Table table
	 * @param ArrayList<Column> pk
	 * @param ArrayList<Column> fk
	 * @param int counter
	 */
	public void addTable(Table table, int counter) {
		//draw table first
		String str = "\"node" +counter+ "\" [ label = \"<f0> "+ table.getName() + "|";
		//set nodeId for the current table
		table.setNodeId("\"node" +counter+ "\"");
		int c = 0;
		//draw primary keys (if any)
		for(Column column : table.getColumns()){
			str += drawColumn(column,++c);
			column.setNodeId("f"+ c);
		}	
		
		/*
		//draw foreign keys (if any)
		for(Column col : fk){
			c++;
			str += "<f"+ c +"> "+ "FK: " +col.getFkColumnName().toString() + "|";
			col.setNodeId("f"+ c);
		}
		//draw other columns (if any)
		for(Column col : columns){
			c++;
			str += "<f"+ c +"> " +col.getColumnName().toString() + "|";
			col.setNodeId("f"+ c);
		}*/
		//tail
		str += "\" shape = \"record\" ]; \n";
		graphStringBuilder.append(str);
	}
	

	
	/**
	 * Draw links between tables
	 * 
	 *  @param Table table
	 *  @param List<Table> tables
	 *  @param ArrayList<Column> fk
	 *  @param int counter
	 *    
	 */
	public void addlink(Table table,  List<Table> tables, int counter) {

		List<Column> fkColumns = table.getForeignKeys();
		String str2 = "";
		for(Column col : fkColumns){
			String sourceNodeId = table.getNodeId();
			String targetNodeId = retrieveNodeId(col.getFkTableName(), tables);
			if(targetNodeId!="") {
				str2 = sourceNodeId + ":"+ col.getNodeId() +" -> " + targetNodeId + ":f0 [ id = 0 ]; \n";
				graphStringBuilder.append(str2);
			}
			
		}
	}
	
	/**
	 * Retrieve the nodeId for the target linked table
	 * 
	 *  @param String tableName
	 *  @param List<Table> tables
	 *  @return String nodeId
	 *  
	 */
	private String retrieveNodeId(String tableName, List<Table> tables){
		String nodeId = "";
		for(Table t : tables){
			if(t.getName().equals(tableName)){
				nodeId = t.getNodeId();
				break;
			}
		}
		return nodeId;
	}


	/**
	 * Returns the graph as an image in binary format
	 * @param String dot_source: source of the graph to be drawn
	 * @param String type: type of the output image to be produced
	 * @return byte[] : a byte array containing the image of the graph
	 */
	public byte[] getGraph(String dot_source, String type)
	{
		File dot;
		byte[] img_stream = null;

		try {
			dot = writeDotSourceToFile(dot_source);
			if (dot != null)
			{
				img_stream = getImgStream(dot, type);
				if (dot.delete() == false) 
					logger.debug("Warning: " + dot.getAbsolutePath() + " could not be deleted!");
				return img_stream;
			}
			return null;
		} catch (java.io.IOException ioe) { return null; }
	}

	/**
	 * Writes the graph's image in a file
	 * @param byte[] img: a byte array containing the image of the graph
	 * @param File outputFile: a File object to where we want to write
	 * @return Success: 1, Failure: -1
	 */
	public int writeGraphToFile(byte[] img, File outputFile)
	{
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(img);
			fos.close();
		} catch (java.io.IOException ioe) { return -1; }
		return 1;
	}

	/**
	 * It will call the external dot program, and return the image in binary format.
	 * @param dot Source of the graph (in dot language).
	 * @param type Type of the output image to be produced, e.g.: gif, dot, fig, pdf, ps, svg, png.
	 * @return The image of the graph in .gif format.
	 */
	private byte[] getImgStream(File dot, String type)
	{
		File img;
		byte[] img_stream = null;

		try {
			img = File.createTempFile("graph_", "."+type, new File(GraphVizApi.OUTPUT_DIR));
			Runtime rt = Runtime.getRuntime();

			String[] args = {DOT_PROGRAM_PATH, "-T"+type, dot.getAbsolutePath(), "-o", img.getAbsolutePath()};
			Process p = rt.exec(args);

			p.waitFor();

			FileInputStream in = new FileInputStream(img.getAbsolutePath());
			img_stream = new byte[in.available()];
			in.read(img_stream);
			// Close it if we need to
			if( in != null ) in.close();

			if (img.delete() == false) 
				logger.debug("Warning: " + img.getAbsolutePath() + " could not be deleted!");
		}
		catch (java.io.IOException ioe) {
			logger.debug("Error:    in I/O processing of tempfile in dir " + GraphVizApi.OUTPUT_DIR+"\n");
			logger.debug("       or in calling external command");
			ioe.printStackTrace();
		}
		catch (java.lang.InterruptedException ie) {
			logger.debug("Error: the execution of the external program was interrupted");
			ie.printStackTrace();
		}

		return img_stream;
	}

	/**
	 * Writes the source of the graph in a file, and returns the written file
	 * as a File object.
	 * @param str Source of the graph (in dot language).
	 * @return The file (as a File object) that contains the source of the graph.
	 */
	private File writeDotSourceToFile(String str) throws java.io.IOException
	{
		File temp;
		try {
			temp = File.createTempFile("graph_", ".dot.tmp", new File(GraphVizApi.OUTPUT_DIR));
			FileWriter fout = new FileWriter(temp);
			fout.write(str);
			fout.close();
		}
		catch (Exception e) {
			logger.debug("Error: I/O error while writing the dot source to temp file!");
			return null;
		}
		return temp;
	}


} 

