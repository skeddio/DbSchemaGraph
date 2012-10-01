package com.trivadis.dsg.ressources;

public interface GenericDao<T> {

	public T save(T o);

	public T update(T o);

	public T delete(T o);

}
