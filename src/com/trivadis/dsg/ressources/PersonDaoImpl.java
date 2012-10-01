/**
 * 
 */
package com.trivadis.dsg.ressources;

/**
 * @author jal
 *
 */
public class PersonDaoImpl extends GenericDaoImpl<Person> {

	public static void toto() {
		PersonDaoImpl t = new PersonDaoImpl();
		t.save(new Person());
		
		String a = t.getMethod();
	}
}
