/**
 * 
 */
package com.trivadis.dsg.core;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jal
 *
 */
public class DbSchemaGraphTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.trivadis.dsg.core.DbSchemaGraph#getInstance()}.
	 */
	@Test
	public final void testGetInstance() {
		DbSchemaGraph resource1 = DbSchemaGraph.getInstance();
		DbSchemaGraph resource2 = DbSchemaGraph.getInstance();
		
		assertSame("References should be the same",resource1,resource2);		
	}

	/**
	 * Test method for {@link com.trivadis.dsg.core.DbSchemaGraph#generate()}.
	 */
	@Test
	public final void testGenerate() {
		fail("Not yet implemented"); // TODO
	}

}
