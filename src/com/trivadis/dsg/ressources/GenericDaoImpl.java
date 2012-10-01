/**
 * 
 */
package com.trivadis.dsg.ressources;

/**
 * @author jal
 *
 */
public class GenericDaoImpl<T extends Comparable<?>> implements GenericDao<T> {

	/**
	 * 
	 */
	public GenericDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public T save(T o) {
		// TODO Auto-generated method stub
		em.save(o); 
		o.compareTo(addaf);

		return null;
	}

	@Override
	public T update(T o) {
		em.update(o);
		return null;
	}

	@Override
	public T delete(T o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <K> K  getMethod() {
		return (K)new String();
	}

}
