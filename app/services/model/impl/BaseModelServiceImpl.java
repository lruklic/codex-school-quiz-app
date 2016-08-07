package services.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import services.model.BaseModelService;
import models.BaseModel;
import play.db.jpa.JPA;

/**
 * <p>Base model service that implements methods for persisting/removing generic objects from database.</p>
 * 
 * <p>Implements generic models for finding all instances of class, finding instance by id, counting, saving
 * and deleting persisted objects from MySQL database.</p>
 * 
 * @author Luka Ruklic
 *
 * @param <T> class that is persisted; must implement BaseModel because of id
 */

public abstract class BaseModelServiceImpl<T extends BaseModel> implements BaseModelService<T> {

	protected Class<T> clazz;
	
	protected BaseModelServiceImpl(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public T findById(Long id) {
		return JPA.em().find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return (List<T>) JPA.em().createQuery("from " + clazz.getName() + " order by id").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllSorted(String sortOrderBy, String sortOrder) {
		return (List<T>) JPA.em().createQuery("from " + clazz.getName() + " order by " + sortOrderBy + " " + sortOrder).getResultList();
	}

	@Override
	public long count() {
		return (long) JPA.em().createQuery("select count(*) from " + clazz.getName()).getSingleResult();
	}

	@Override
	public T save(T instance) {
		instance = JPA.em().merge(instance);
		JPA.em().flush();
		return instance;
	}

	@Override
	public T delete(T instance) {
		JPA.em().remove(instance);
		JPA.em().flush();
		return instance;
	}
	
	protected List<T> findAllOrEmpty(List<T> findAllList) {
		if (findAllList == null) {
			return new ArrayList<T>();
		} else {
			return findAllList;
		}
	}
	
	/**
	 * Method that checks if query method getSingleResult will cast NoResultException or not.
	 * If no result is found, exception casting is prevented and method returns null value.
	 * 
	 * @param q query that is being executed
	 * @return query result or <i>null</i> if no result is given
	 */
	protected Object singleResultOrNull(Query q) {
		Object o;
		try {
			o = q.getSingleResult();
		} catch (NoResultException e) {
			o = null;
		}
		
		return o;
	}

}
