/**
 * 
 */
package com.trivadis.dsg.export;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.trivadis.dsg.model.Column;
import com.trivadis.dsg.model.DataStructure;
import com.trivadis.dsg.model.Table;

/**
 * @author jal
 *
 */
public class GraphGenerator {

	private final static Logger logger = Logger
			.getLogger(GraphGenerator.class);

	/**
	 * Constructor
	 */
	public GraphGenerator() {
	}


	/**
	 * Construct a DOT graph in memory, convert it to image and store the image 
	 * 
	 * @param List<Table>
	 */
	public void generate(DataStructure structure)
	{
		//GraphVizApi instance is used to create the output file and manage DOT file creation 
		GraphVizApi graphVizObject = new GraphVizApi();

		//start graph: write the beginning of the DOT 
		graphVizObject.addString("digraph g { graph [ rankdir = \"LR\" ];  node [ fontsize = \"16\" shape = \"ellipse\" ];");

		//draw all tables with PKs, FKs and columns
		int counter = 0;

		List<Table> tables = structure.getTables();
		for(Table t : tables){
			graphVizObject.addTable(t, counter++);
		}

		//draw links between tables
		counter = 0;
		for(Table t : tables){
			graphVizObject.addlink(t, tables, counter++);
		}

		//end graph: write the end of the DOT file
		graphVizObject.addString("}");
		
		//display the result for checking
		logger.debug(graphVizObject.getDotSource());

		// set output file and format type (png/jpg/pdf..)
		String type = "png";
		File file = new File("DbSchemaGraphDir/out." + type);
		
		//generate and save the final output file
		int result = graphVizObject.writeGraphToFile( graphVizObject.getGraph( graphVizObject.getDotSource(), type ), file );

		//display the result for checking
		logger.debug("Graph created: " +result);

		
	}

}
