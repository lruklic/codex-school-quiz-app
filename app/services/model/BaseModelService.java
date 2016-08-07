package services.model;

import java.util.List;

/**
 * Base model service interface that defines basic operations that every service must implement.
 * 
 * @author Luka Ruklic
 *
 * @param <T> parameter that defines class
 */

public interface BaseModelService<T> {
	
	/**
	 * Finds object T by id.
	 * 
	 * @param id identificator
	 * @return object T if found
	 */
	public T findById(Long id);
	/**
	 * Finds all objects of type T.
	 * @return list of all objects T
	 */
	public List<T> findAll();
	/**
	 * Finds all objects of type T and sorts them.
	 * @param sortOrderBy name of field by which the list is sorted
	 * @param sortOrder ascending or descending (valid inputs are asc or desc)
	 * @return sorted list of all objects T
	 */
	public List<T> findAllSorted(String sortOrderBy, String sortOrder);
	
	public long count();
	
	public T save(T instance);
	
	public T delete(T instance);
	
}
