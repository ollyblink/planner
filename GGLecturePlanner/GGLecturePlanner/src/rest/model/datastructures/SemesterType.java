package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SemesterType {
	@Override
	public String toString() {
		return "SemesterType [abbreviation=" + abbreviation + ", description=" + description + "]";
	}

	private String abbreviation;
	private String description;

	public SemesterType() {
	}

	public SemesterType(String abbreviation, String description) {
		this.abbreviation = abbreviation;
		this.description = description;
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
