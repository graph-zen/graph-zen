package application.drawable;

import javafx.scene.image.Image;

public class Images {
	
	private String tag = "application/drawable/";

	public Image notVisitedIcon(){
		return getImage("notvisited.png");
	}
	
	public Image penIcon(){
		return getImage("pen.png");
	}
	
	public Image drawIcon(){
		return getImage("draw.png");
	}
	
	public Image nextIcon(){
		return getImage("next.png");
	}
	
	public Image stepIcon(){
		return getImage("step.png");
	}
	
	public Image backStepIcon(){
		return getImage("step2.png");
	}
	
	public Image backIcon(){
		return getImage("back.png");
	}
	
	public Image playIcon(){
		return getImage("play.png");
	}
	
	public Image resetIcon(){
		return getImage("reset.png");
	}
	
	public Image reset2Icon(){
		return getImage("reset2.png");
	}
	
	public Image currentIcon(){
		return getImage("current.png");
	}
	
	public Image visitedIcon(){
		return getImage("visited.png");
	}
	
	public Image pauseIcon(){
		return getImage("pause.png");
	}
	
	public Image AppIcon(){
		return getImage("tree.png");
	}
	
	private Image getImage(String image) {
		return new Image(getClass().getClassLoader().getResourceAsStream(tag + image));
	}
}
