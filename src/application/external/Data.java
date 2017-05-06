package application.external;


// structure for keeping coordinates within internal class
public class Data {

	private int minX;
	private int maxX;
	private int minY;
	private int maxY;

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	public int getCenterX(){
		return (minX + maxX)/2;
	}
	
	public int getCenterY(){
		return (minY + maxY)/2;
	}
}