package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TrueFalseTupel {
	Boolean value;
	String name;

	public TrueFalseTupel(Boolean value, String name) {
		this.value = value;
		this.name = name;
	}

	public TrueFalseTupel() {
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
