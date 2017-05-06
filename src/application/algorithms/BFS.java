package application.algorithms;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import application.elements.Node;

public class BFS extends SearchAlgorithm {

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
							BFS.this.run(nodes.get(next));
							
							//
							System.out.println(isPaused);
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
			run(nodes.get(count));
			count++;
		} else if (flag == BACK && count > 0) {
			count--;
			for (int next = 0; next < count; next++)
				run(nodes.get(next));
		}
	}
	
	private void setNode(){
		if (nodes.isEmpty() && !starts.isEmpty())
			nodes.add(starts.get(0));
	}

	private void run(Node parent) {
		parent.setImage(image.currentIcon());
		parent.setState(CURRENT);
		for (int child = 0; child < parent.branchingFactor(); child++) {
			if (!nodes.contains(parent.get(child))) {
				nodes.add(parent.get(child));
				parent.get(child).setImage(image.visitedIcon());
				parent.get(child).setState(VISITED);
			}
		}
	}
	
}
