package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Discipline {
	@Override
	public String toString() {
		return abbreviation;
	}

	private String abbreviation;
	private String description;

	public Discipline(String abbreviation, String description) {
		this.abbreviation = abbreviation;
		this.description = description;
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

}
