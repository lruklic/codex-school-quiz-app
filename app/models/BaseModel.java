package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Basic database entity model that every other entity class extends. Contains ID value and proper equals() method.
 *
 * @author Luka Ruklic
 *
 */

@MappedSuperclass
public class BaseModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 9127819341547937253L;

	@Id
	@GeneratedValue
	@Column(name="id", unique = true, nullable = false)
	public Long id;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseModel other = (BaseModel) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}



}
