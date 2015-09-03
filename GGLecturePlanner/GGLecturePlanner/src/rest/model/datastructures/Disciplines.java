package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Disciplines {
	private String abbreviation;
	private String description;

	public Disciplines(String abbreviation, String description) {
		this.abbreviation = abbreviation;
		this.description = description;
	}

	public Disciplines() {
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
