package rest.model.datastructures;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Discipline implements Comparable<Discipline>{
	 

	@Override
	public String toString() {
		return "Discipline [abbreviation=" + abbreviation + ", description=" + description + ", moduleTypes=" + moduleTypes + "]";
	}

	private String abbreviation;
	private String description;
	
	private List<ModuleType> moduleTypes;
 
	public Discipline(String abbreviation, String description, List<ModuleType> moduleTypes) {
		this.abbreviation = abbreviation;
		this.description = description;
		this.moduleTypes = moduleTypes;
	}

	public List<ModuleType> getModuleTypes() {
		return moduleTypes;
	}

	public void setModuleTypes(List<ModuleType> moduleTypes) {
		this.moduleTypes = moduleTypes;
	}

	public Discipline() {
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int compareTo(Discipline d) {
		return (abbreviation.compareTo(d.abbreviation)) ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abbreviation == null) ? 0 : abbreviation.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Discipline other = (Discipline) obj;
		if (abbreviation == null) {
			if (other.abbreviation != null)
				return false;
		} else if (!abbreviation.equals(other.abbreviation))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

}
