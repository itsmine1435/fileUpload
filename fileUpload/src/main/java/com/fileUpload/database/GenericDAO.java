package com.fileUpload.database;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<E, K extends Serializable> {
	public void save(E entity);
	
	public void saveOrUpdate(E entity);
	
	public E findOne(K key);
	
	public List<E> findAll();
	
	public void delete(E entiry);
	
	
}
