package application.external;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageRecognizer {
	private static final int BLACK = 0;
	private static final int WHITE = -1;
	private static final int SOME_COLOR = 1;

	private BufferedImage bimg;
	private int[][] bitMap;

	private ArrayList<Data> nodes;
	private ArrayList<Data> lines;

	public ImageRecognizer(String path) throws IOException {
		bimg = ImageIO.read(new File(path));
		bitMap = new int[bimg.getHeight()][bimg.getWidth()];
	

		// In order to left 3 different values within bitMap we need to
		// process and limit number of distinct colors to 3
		processBitMap();

		//Recognize nodes from image and add them 
		//correspondingly to nodes and lines Array Lists
		recognizeNodes();
		recognizeLines();
	}
	
	private void swap(int i, int j, int[] a){
		a[i] = a[j] + 0*(a[j] = a[i]);
	}

	private void recognizeNodes() {
		nodes = new ArrayList<Data>();

		// initialize data array in order to take coordinates of
		// frame that surrounds image node
		int[] coordinates = new int[4];

		for (int y = 0; y < bimg.getHeight(); y++) {
			for (int x = 0; x < bimg.getWidth(); x++) {

				coordinates[0] = coordinates[1] = Integer.MAX_VALUE;
				coordinates[2] = coordinates[3] = Integer.MIN_VALUE;

				// recognize black object as a node
				floodFill(x, y, coordinates, BLACK);

				// add detected node to nodes Array List
				if (coordinates[0] < Integer.MAX_VALUE) {
					Data data = new Data();
					data.setMinX(coordinates[0]);
					data.setMinY(coordinates[1]);
					data.setMaxX(coordinates[2]);
					data.setMaxY(coordinates[3]);
					nodes.add(data);
				}
			}
		}
	}

	private void recognizeLines() {
		lines = new ArrayList<Data>();

		// initialize data array in order to take coordinates of
		// frame that surrounds image node
		int[] coordinates = new int[4];

		for (int y = 0; y < bimg.getHeight(); y++) {
			for (int x = 0; x < bimg.getWidth(); x++) {

				coordinates[0] = coordinates[1] = Integer.MAX_VALUE;
				coordinates[2] = coordinates[3] = Integer.MIN_VALUE;

				// recognize non black and non white object as a line
				floodFill(x, y, coordinates, SOME_COLOR);

				// add detected node to nodes Array List
				if (coordinates[0] < Integer.MAX_VALUE) {
					
					if(Math.abs(coordinates[0] - x) 
							> Math.abs(coordinates[2] - x))
						swap(0, 2, coordinates);
					
					if(Math.abs(coordinates[1] - y) 
							> Math.abs(coordinates[3] - y))
						swap(1, 3, coordinates);
	        		
					Data data = new Data();
					data.setMinX(coordinates[0]);
					data.setMinY(coordinates[1]);
					data.setMaxX(coordinates[2]);
					data.setMaxY(coordinates[3]);
					lines.add(data);
				}
			}
		}
	}

	private void processBitMap() {
		for (int y = 0; y < bimg.getHeight(); y++) {
			for (int x = 0; x < bimg.getWidth(); x++) {

				// get RGB value of each pixel and extract from them
				// red, green and blue values in 0-255 range
				int color = bimg.getRGB(x, y);
				int red = (color >> 16) & 0xff;
				int green = (color >> 8) & 0xff;
				int blue = color & 0xff;

				// if pixel color is not white (-1) or black (0)
				// then set it to 1 otherwise make black (0)
				if ((red > 0 || green > 0 || blue > 0) && color != WHITE)
					color = SOME_COLOR;
				else if (color != WHITE)
					color = BLACK;

				bitMap[y][x] = color;
			}
		}
	}
	
	
	private void  floodFill(int x, int y, int[] coordinates, int objectColor){
		if(bitMap[y][x] != objectColor){
			return;
		}
		
		if(bitMap[y][x] == objectColor){
			bitMap[y][x] = WHITE;
			
			coordinates[0] = Math.min(x, coordinates[0]);
			coordinates[1] = Math.min(y, coordinates[1]);
			coordinates[2] = Math.max(x, coordinates[2]);
			coordinates[3] = Math.max(y, coordinates[3]);
			
			floodFill(x - 1, y, coordinates, objectColor);
			floodFill(x + 1, y, coordinates, objectColor);
			floodFill(x, y - 1, coordinates, objectColor);
			floodFill(x, y + 1, coordinates, objectColor);
		}
	}
	
	
	public ArrayList<Data> getNodeCoordinateList() {
		return nodes;
	}

	public ArrayList<Data> getLineCoordinateList() {
		return lines;
	}
}
