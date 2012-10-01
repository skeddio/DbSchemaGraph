/**
 * 
 */
package com.trivadis.dsg.core;



/**
 * @author jal
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//initialization, database connection + retrieve schema data
		DbSchemaGraph.getInstance().generate();
	}

}
