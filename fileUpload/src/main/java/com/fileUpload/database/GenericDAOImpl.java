package com.fileUpload.database;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericDAOImpl<E, K extends Serializable> implements GenericDAO<E, K> {

	@PersistenceContext	
	protected EntityManager entityManager;
	
	protected Class<? extends E> daoType;

	public GenericDAOImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType) t;
		daoType = (Class) parameterizedType.getActualTypeArguments()[0];
	}

	public void save(E entity) {
		entityManager.persist(entity);
	}

	public void saveOrUpdate(E entity) {
		entityManager.merge(entity);
	}

	public E findOne(K key) {
		return entityManager.find(daoType, key);
	}

	public List<E> findAll() {
		String query = "from "+ daoType.getName();
		return entityManager.createQuery(query).getResultList();
	}

	public void delete(E entiry) {
		entityManager.remove(entiry);
	}

}
