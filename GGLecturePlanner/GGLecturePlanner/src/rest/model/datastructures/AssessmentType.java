package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AssessmentType implements IAbbrDescr{
	@Override
	public String toString() {
		return "AssessmentType [abbreviation=" + abbreviation + ", description=" + description + "]";
	}
	private String abbreviation;
	private String description;
	public AssessmentType(String abbreviation, String description) {
		this.abbreviation = abbreviation;
		this.description = description;
	}
	public AssessmentType() {
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
