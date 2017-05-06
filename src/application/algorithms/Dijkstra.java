package application.algorithms;

import java.util.Collections;
import java.util.Comparator;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import application.elements.Node;

public class Dijkstra extends SearchAlgorithm{

	class NodeCompare implements Comparator<Node> {
	    public int compare(Node n1, Node n2) {
	        return (int) (n1.getTotalCost() - n2.getTotalCost());
	    }
	}

	@Override
	public void play(boolean isPaused) {
		Service<String> service = new Service<String>() {
			
			@Override
			protected Task<String> createTask() {
				return new Task<String>() {
					
					@Override
					protected String call() throws Exception {
						
						setNode();
						for (int next = count; next < nodes.size(); next++) {
							Dijkstra.this.run(nodes.get(next), next);
							
							//
							System.out.println(isPaused);
							if(!isPaused) break;
							
							Thread.sleep(1000);
						}
						return null;
			}};}}; service.start();
		
	}
	
	@Override
	public void step(int flag) {
		
		setNode();
		if (flag == NEXT && count < nodes.size()) {
			run(nodes.get(count), count);
			count++;
		} else if (flag == BACK && count > 0) {
			count--;
			for (int next = 0; next < count; next++)
				run(nodes.get(next), count);
		}
	}

	private void setNode(){
		if (nodes.isEmpty() && !starts.isEmpty())
			nodes.add(starts.get(0));
	}

	private void run(Node parent, int start) {
		parent.setImage(image.currentIcon());
		parent.setState(CURRENT);
		for (int child = 0; child < parent.branchingFactor(); child++) {
			Node baby = parent.get(child);
			if (!nodes.contains(baby)) {
				baby.setTotalCost(parent.getTotalCost() + getDistanceBetween(parent, baby));
				nodes.add(baby);
				baby.setImage(image.visitedIcon());
				baby.setState(VISITED);
			}else{
				updateNodeList(baby, getDistanceBetween(parent, baby));
			}
			
			Collections.sort(nodes.subList(start + 1, nodes.size()), new NodeCompare());
			
		}
	}
	
	private double getDistanceBetween(Node from, Node to){
		return from.getLinks(to).getDistance();
	}
	
	private void updateNodeList(Node baby, double additionalCost){
		for(int index = 0; index < nodes.size(); index++){
			double totalCost = baby.getTotalCost();
			if(nodes.get(index).equals(baby) && (totalCost > totalCost + additionalCost)){
				nodes.get(index).setTotalCost(totalCost + additionalCost);
			}
		}
	}
	

}
