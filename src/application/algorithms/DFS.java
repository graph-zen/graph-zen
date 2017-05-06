package application.algorithms;

import java.util.Collections;
import java.util.Comparator;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import application.elements.Node;

public class DFS extends SearchAlgorithm{

	
	class NodeCompare implements Comparator<Node> {
	    public int compare(Node n1, Node n2) {
	        return (int) (n2.getId().compareTo(n1.getId()));
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
							DFS.this.run(nodes.get(nodes.size() - 1), nodes.size() - 1);
							
							
							//System.out.println(isPaused);
							if(!isPaused) break;
							
							Thread.sleep(getSpeed());
						}
						return null;
			}};}}; service.start();
	}

	@Override
	public void step(int flag) {
		
		setNode();
		if (flag == NEXT && count < nodes.size()) {
			run(nodes.get(nodes.size() - 1), nodes.size() - 1);
			count++;
		} else if (flag == BACK && count > 0) {
			count--;
			for (int next = 0; next < count; next++)
				run(nodes.get(nodes.size() - 1), nodes.size() - 1);
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
			if (!nodes.contains(parent.get(child))) {
				nodes.add(parent.get(child));
				parent.get(child).setImage(image.visitedIcon());
				parent.get(child).setState(VISITED);
			}
		}
		
		Collections.sort(nodes.subList(start + 1, nodes.size()), new NodeCompare());
		
	}

}
