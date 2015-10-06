package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModuleType implements IAbbrDescr {
	@Override
	public String toString() {
		return "ModuleType [abbreviation=" + abbreviation + ", description=" + description + "]";
	}
	private String abbreviation;
	private String description;
	public ModuleType(String abbreviation, String description) {
		this.abbreviation = abbreviation;
		this.description = description;
	}
	public ModuleType() {
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
	
	
	 
	
}
