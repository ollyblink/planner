package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lecturer extends Employee {

	private ArrayList<Course> courses;
}
