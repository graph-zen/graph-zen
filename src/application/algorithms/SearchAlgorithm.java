package application.algorithms;

import java.util.ArrayList;
import application.drawable.Images;
import application.elements.Node;

public abstract class SearchAlgorithm {
	protected final static int NOT_VISITED = 0;
	protected final static int VISITED = 1;
	protected final static int CURRENT = 2;
	protected final static int NEXT = 1;
	protected final static int BACK = 2;
	
	protected ArrayList<Node> starts;
	protected ArrayList<Node> ends;
	protected ArrayList<Node> nodes;
	private ArrayList<Node> cleanUp;
	
	//protected static boolean isPaused;
	protected static int count;
	protected Images image;
	private int speed;
	
	public SearchAlgorithm(){
		starts = new ArrayList<Node>();
		ends = new ArrayList<Node>();
		nodes = new ArrayList<Node>();
		image = new Images();
		speed = 1000;
	}
	
	public void removeAllStarts(){
		starts.clear();
	}
	
	public void removeStart(Node start){
		starts.remove(start);
	}
	
	public void addStart(Node start){
		starts.add(start);
	}
	
	public void addEnd(Node end){
		ends.add(end);
	}
	
	public void removeEnd(Node end){
		ends.remove(end);
	}
	
	public void setSpeed(int time){
		speed = time;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public void reset(boolean flag){
		if(starts.size() > 0){
			cleanUp = new ArrayList<Node>();
			cleanUp.add(starts.get(0));
			
			Node parent;
			for(int i = 0; i < cleanUp.size(); i++){
				parent = cleanUp.get(i);
				parent.setImage(image.notVisitedIcon());
				parent.setState(NOT_VISITED);
				for(int child = 0; child < parent.branchingFactor(); child++){
					if(!cleanUp.contains(parent.get(child))){
						cleanUp.add(parent.get(child));
						parent.get(child).setImage(image.notVisitedIcon());
						parent.get(child).setState(NOT_VISITED);
					}
				}
			}
			cleanUp.clear();
			nodes.clear();
			
			if(flag){
				count = 0;
				//isPaused = false;
			}
		}
	}
	
	
	public abstract void play(boolean pausePushed);
	public abstract void step(int flag);
}
