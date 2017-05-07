package application.elements;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Node {

	//private String id; //id of the current node
	private int state; //not-visited: 0, visited: 1, current: 2
	private double heuristic; //heuristic number
	private ImageView imageView; //image of the Node
	private ArrayList<Link> links; //list of links from other Nodes to current Node
	private ArrayList<Node> adjacentNodes; //list of adjacent Nodes
	private boolean start, goal; //Started or goal node
	private double totalCost;
	private Text text;
	
	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public Node(){	
		//Register and set default image
		imageView = new ImageView();
		//imageView.setImage(images[state]);
		
		adjacentNodes = new ArrayList<Node>();
		links = new ArrayList<Link>();
		
		text = new Text();
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		text.setFill(Color.WHITE);
	}
	
	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Image getImage() {
		return imageView.getImage();
	}

	public void setImage(Image image) {
		imageView.setImage(image);
	}

	public String getId() {
		return imageView.getId();
	}

	public void setId(String id) {
		imageView.setId(id);
		text.setText(id);
	}

	public Node getAdjacentNode(String id) {
		for (Node node : adjacentNodes) {
			if(node.getId().equals(id))
				return node;
		}
		return null;
	}

	public void addAdjacentNode(Node node) {
		adjacentNodes.add(node);
	}
	
	public ArrayList<Link> getLinksList(){
		return links;
	}
	
	public ArrayList<Node> getNodesList(){
		return adjacentNodes;
	}

	public Link getLinks(Node to) {
		for (Link link : links) {
			if(link.getNodeFrom().equals(to) || link.getNodeTo().equals(to)){
				return link;
			}
		}
		return null;
	}

	public void addLink(Link link) {
		links.add(link);
	}
	
	public ImageView getImageView() {
		return imageView;
	}
	
	public DoubleProperty getImagePropertyX(){
		return imageView.xProperty();
	}
	
	public DoubleProperty getImagePropertyY(){
		return imageView.yProperty();
	}
	
	
	public double getImageHalfWidth(){
		return imageView.getBoundsInLocal().getWidth()/2.0;
	}
	
	public double getImageHalfHeight(){
		return imageView.getBoundsInLocal().getHeight()/2.0;
	}
	
	public int branchingFactor(){
		return adjacentNodes.size();
	}
	
	public Node get(int i){
		return adjacentNodes.get(i);
	}
	
	public boolean contains(Node node){
		return adjacentNodes.contains(node);
	}
	
	public boolean contains(Link link){
		return links.contains(link);
	}
	
	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isGoal() {
		return goal;
	}

	public void setGoal(boolean goal) {
		this.goal = goal;
	}
	
	public void setX(double x) {
		imageView.setX(x);
		text.setX(x + getImageHalfWidth() - 5);
		text.toFront();
	}
	
	public void setY(double y) {
		imageView.setY(y);
		text.setY(y + getImageHalfHeight() + 5);
		text.toFront();
	}
	
	public Text getText(){
		return text;
	}

}
