package rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JSONClass {
	private String welcome;
	
 
	public JSONClass() {
	}

	public JSONClass(String welcome) { 
		this.welcome = welcome;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}
	
	
}
