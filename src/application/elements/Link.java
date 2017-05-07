package application.elements;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.scene.shape.Line;

public class Link {

	private double distance;
	private String id;
	private Node from;
	private Node to;
	private Line line;
	
	public Link(){
		line = new Line();
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double dist) {
		this.distance = dist;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Node getNodeFrom() {
		return from;
	}
	
	public Node getNodeTo() {
		return to;
	}
	
	public void setNodeFrom(Node node) {
		from = node;
	}
	
	public void setNodeTo(Node node) {
		to = node;
	}
	
	public Line getLink(){
		return line;
	}
	
	public DoubleProperty startXProperty(){
		return line.startXProperty();
	}
	
	public DoubleProperty startYProperty(){
		return line.startYProperty();
	}
	
	public DoubleProperty endYProperty(){
		return line.endYProperty();
	}
	
	public DoubleProperty endXProperty(){
		return line.endXProperty();
	}
	
}
