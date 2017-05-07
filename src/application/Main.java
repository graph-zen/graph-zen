package application;

import java.util.ArrayList;



import application.algorithms.*;
import application.drawable.Images;
import application.elements.Link;
import application.elements.Node;
import application.external.ImageRecognizer;
import application.properties.LinkProperties;
import application.properties.NodeProperties;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;


public class Main extends Application {
	private final static int NEXT = 1;
	private final static int BACK = 2;
	private final static boolean FULL = true;
	
	private SearchAlgorithm sa;
	private String algorithm;
	private boolean lineIsSelected;
	private int mouseClicks;
	private int defaultID;
	private ContextMenu lineContextMenu;
	private ContextMenu nodeContextMenu;
	private MenuItem deleteLink;
	private MenuItem linkProperties;
	private MenuItem nodeProperties;
	private MenuItem deleteNode;
	private Node from;
	private Link line;
	private boolean directed;
	private ImageView lineButton;
	private Images image;
	private boolean isPaused;
	private ImageRecognizer ir;

	@Override
	public void start(Stage primaryStage) throws Exception {
		//ir = new ImageRecognizer("/home/ziyaddin/github/graph/src/application/drawable/some.png");
		//---------------------Menu-------------------------------------------------------------
        
		BorderPane root2 = new BorderPane();
        root2.setId("pane");

		//new
	    MenuBar menuBar = new MenuBar();
	    menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
	    root2.setTop(menuBar);

		//---------------------Menu-------------------------------------------------------------
        
		Menu fileMenu = new Menu("File");
	    MenuItem newMenuItem = new MenuItem("New");
	    MenuItem saveMenuItem = new MenuItem("Save");
	    MenuItem importPicMenuItem = new MenuItem("Import Picture");
	    MenuItem exitMenuItem = new MenuItem("Exit");
	    exitMenuItem.setOnAction(actionEvent -> Platform.exit());
	    
	    //importPicMenuItem.setOnAction(e -> justDoIt());

	    fileMenu.getItems().addAll(newMenuItem, saveMenuItem,
	        new SeparatorMenuItem(), importPicMenuItem, exitMenuItem);
 
	    Menu webMenu = new Menu("Edit");
	    CheckMenuItem htmlMenuItem = new CheckMenuItem("HTML");
	    htmlMenuItem.setSelected(true);
	    webMenu.getItems().add(htmlMenuItem);

	    CheckMenuItem cssMenuItem = new CheckMenuItem("CSS");
	    cssMenuItem.setSelected(true);
	    webMenu.getItems().add(cssMenuItem);

	    Menu sqlMenu = new Menu("View");
	    ToggleGroup tGroup = new ToggleGroup();
	    RadioMenuItem mysqlItem = new RadioMenuItem("Home");
	    mysqlItem.setToggleGroup(tGroup);

	    RadioMenuItem oracleItem = new RadioMenuItem("About");
	    oracleItem.setToggleGroup(tGroup);
	    oracleItem.setSelected(true);

	    sqlMenu.getItems().addAll(mysqlItem, oracleItem,
	        new SeparatorMenuItem());

	    Menu tutorialManeu = new Menu("Tutorial");
	    tutorialManeu.getItems().addAll(
	        new CheckMenuItem("Java"),
	        new CheckMenuItem("JavaFX"),
	        new CheckMenuItem("Algorithms"));

	    sqlMenu.getItems().add(tutorialManeu);

	    menuBar.getMenus().addAll(fileMenu, webMenu, sqlMenu);
		
        //---------------------Algorithms-------------------------------------------------------
        ComboBox<String> algorithms = new ComboBox<String>();
        algorithms.getItems().addAll(
            "A* Search",
            "Breadth First Search",
            "Depth First Search",
            "Uniform Cost Search"
        );
        
        //by default SearchAlgorithm is BFS
        image = new Images();
        sa = new BFS();
        
        algorithms.valueProperty().addListener((ov, t, selected) -> {
        	if(algorithms.getSelectionModel().getSelectedIndex() == 0){
        		sa = new AStar();
        	}
        	else if(algorithms.getSelectionModel().getSelectedIndex() == 1){
        		sa = new BFS();
        	}
        	else if(algorithms.getSelectionModel().getSelectedIndex() == 2){
        		sa = new DFS();
        	}
        	else if(algorithms.getSelectionModel().getSelectedIndex() == 3){
        		sa = new UCS();
        	}
        }); 
       
        algorithms.setEditable(false);
        algorithms.getSelectionModel().select(3); 
        System.out.println("SELECTED!!!");
        
      //---------------------Timing------------------------------------------------------------  

        ComboBox<String> timeOptions = new ComboBox<String>();
        timeOptions.getItems().addAll(
            "Very Fast",
            "Fast",
            "Medium",
            "Slow",
            "Very Slow"  
        );
        
        timeOptions.setEditable(false);
        timeOptions.getSelectionModel().select(2);
        timeOptions.valueProperty().addListener((ov, t, selected) -> {
        	if(selected.equals("Very Fast"))
        		sa.setSpeed(250);
        	else if(selected.equals("Fast"))
        		sa.setSpeed(500);
        	else if(selected.equals("Medium"))
        		sa.setSpeed(1000);
        	else if(selected.equals("Slow"))
        		sa.setSpeed(2000);
        	else if(selected.equals("Very Slow"))
        		sa.setSpeed(4000);
        	else sa.setSpeed(1000);
        }); 
        //---------------------Tools------------------------------------------------------------  
        //Node icon in the tool bar
        Node dragNode = new Node();
        ImageView draggedImage = dragNode.getImageView();
        draggedImage.setImage(image.notVisitedIcon());
        draggedImage.setOnDragDetected(event -> {
               /* allow any transfer mode */
               Dragboard db = draggedImage.startDragAndDrop(TransferMode.COPY);
                
               /* put a image on drag board */
               ClipboardContent content = new ClipboardContent();
                
               Image sourceImage = image.notVisitedIcon();
               content.putImage(sourceImage);
               db.setContent(content);
               
               event.consume();
        });
        
        //line icon in tool bar
        lineButton = new ImageView(image.penIcon());
        lineButton.setOnMouseEntered(e -> lineButton.setImage(image.drawIcon()));
        lineButton.setOnMouseExited(e -> {
        	if(!lineIsSelected) 
        		lineButton.setImage(image.penIcon());
        	});
        
        lineButton.setOnMouseClicked(e -> {
        	if(e.getButton() == MouseButton.PRIMARY){
            	lineIsSelected = !lineIsSelected;
            	mouseClicks = 0;
        	}
        });
        
        ImageView playButton = new ImageView(image.playIcon());
		playButton.setOnMouseClicked(e ->{
			isPaused = !isPaused;
			
			if (isPaused)
				playButton.setImage(image.pauseIcon());
			else
				playButton.setImage(image.playIcon());
			
			sa.play(isPaused);
		});
		
        ImageView nextButton = new ImageView(image.nextIcon());
        nextButton.setOnMouseEntered(e -> nextButton.setImage(image.stepIcon()));
        nextButton.setOnMouseExited(e -> nextButton.setImage(image.nextIcon()));
        nextButton.setOnMouseClicked(e ->{ 
        	sa.step(NEXT);
        	///resetPlay();
        });
        
        ImageView backButton = new ImageView(image.backIcon());
        backButton.setOnMouseEntered(e -> backButton.setImage(image.backStepIcon()));
        backButton.setOnMouseExited(e -> backButton.setImage(image.backIcon()));
        backButton.setOnMouseClicked(e ->{ 
        	sa.reset(!FULL);
        	sa.step(BACK);
        	//resetPlay();
        });
		
		ImageView resetButton = new ImageView(image.resetIcon());
        resetButton.setOnMouseEntered(e -> resetButton.setImage(image.reset2Icon()));
        resetButton.setOnMouseExited(e -> resetButton.setImage(image.resetIcon()));
        resetButton.setOnMousePressed(e ->{
        	sa.reset(FULL);
        	//resetPlay();
        });
        
	    ToolBar tools = new ToolBar();
	    tools.setPrefWidth(tools.getPrefWidth());
	    tools.setPrefHeight(tools.getPrefHeight());
	    tools.getItems().addAll(
	    		draggedImage, lineButton, 
	    		backButton, playButton, 
	    		nextButton, resetButton, algorithms, timeOptions);
	    //---------------------TabPane---------------------------------------------------------
	    TabPane tabLayout = new TabPane();
        
        Tab addButton = new Tab(" + ");
        addButton.setOnSelectionChanged(event -> {
        	int size = tabLayout.getTabs().size();
        	
        	if(size > 14){
        		addButton.setDisable(true);
        	}
	        Tab tab = new Tab("Tab " + (size));
	        
	        if(size == 1) tab.setClosable(false);
	        
	        AnchorPane anchorPane = new AnchorPane();
	        tab.setContent(anchorPane);
	        
	        tabLayout.getTabs().add(tab);
	        tabLayout.getSelectionModel().select(tab);
	        
	        //If one of the tabs is closed enable addButton
	        tab.setOnClosed(e -> addButton.setDisable(false));
	              
	        setupNodeTarget(anchorPane);      
        });
        
        tabLayout.getTabs().addAll(addButton);
        //---------------------LineContextMenu----------------------------------------------
        lineContextMenu = new ContextMenu();
        
        linkProperties = new MenuItem("Properties");
        deleteLink = new MenuItem("Delete");
        
        lineContextMenu.getItems().addAll(linkProperties, deleteLink);
        //---------------------NodeContextMenu-------------------------------------------------	
        nodeContextMenu = new ContextMenu();
        
        nodeProperties = new MenuItem("Properties");
        deleteNode = new MenuItem("Delete");
        
        nodeContextMenu.getItems().addAll(nodeProperties, deleteNode);  
        //---------------------Root------------------------------------------------------------			
		primaryStage.getIcons().add(image.AppIcon());
		primaryStage.setTitle("Graph Zen");
		
		//root Layout
		VBox root = new VBox();
		tabLayout.prefHeightProperty().bind(root.heightProperty());
		
		Scene scene = new Scene(root, 1000, 600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        root.getChildren().addAll(menuBar, tools, tabLayout);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void bind(Node a1, Node a2, AnchorPane ap){
	    	line = new Link();
	    	line.setNodeFrom(a1);
	    	
	    	//bind line into the center of the Node
	    	line.startXProperty().bind(a1.getImagePropertyX().add(a1.getImageHalfWidth()));
	    	line.startYProperty().bind(a1.getImagePropertyY().add(a1.getImageHalfHeight()));
	    	
	    	//second selected node should not contain from node
	    	//as adjacent one or link to itself
	    	if(!a2.contains(a1) && a1 != a2){
	    		Line link =  line.getLink();
	    		
	    		//add from node to adjacent list of current node
	    		//if graph is directed perform the same with from node
		    	a2.addAdjacentNode(a1);
		    	if(!directed) a1.addAdjacentNode(a2);
		    		
		    	line.setNodeTo(a2);
		    	
		    	//bind line into the center of the Node
		    	line.endXProperty().bind(a2.getImagePropertyX().add(a2.getImageHalfWidth()));
		    	line.endYProperty().bind(a2.getImagePropertyY().add(a2.getImageHalfHeight()));
		    	
		    	link.setStrokeWidth(3);
		    	link.setStrokeLineCap(StrokeLineCap.SQUARE);
		    	link.getStrokeLineCap();
			    
		    	setUpLineSource(line, ap);
			    ap.getChildren().add(link);
			    link.toBack();
			    
				line.getNodeTo().addLink(line);
				line.getNodeFrom().addLink(line);
				
		    	mouseClicks = 0;
			    lineIsSelected = !lineIsSelected;
			    lineButton.setImage(image.penIcon());
	    	}
	}
	
	
	private double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
	}
	
	private Node getNode(double x, double y, ArrayList<Node> nodes){
		double minValue = Double.MAX_VALUE, distance;
		Node node = null;
		for(int j = 0; j < nodes.size(); j++){
			double ivX = nodes.get(j).getImageView().getX();
			double ivY = nodes.get(j).getImageView().getY();
			
			if((distance = distance(x, y, ivX, ivY)) <  minValue){
				minValue  = distance;
				node = nodes.get(j);
			}
		}
		return node;
	}
	
	private void setupNodeTarget(AnchorPane targetPane) {
		
		
		//When image is loaded
		if(ir != null){
			double x, y;
			ArrayList<Node> nodes = new ArrayList<Node>();
			for(int i = 0; i < ir.getNodeCoordinateList().size(); i++){
				x = ir.getNodeCoordinateList().get(i).getCenterX();
				y = ir.getNodeCoordinateList().get(i).getCenterY();
				insertImage(new Images().notVisitedIcon(), targetPane, x, y, nodes);
			}
			
			for(int i = 0; i < ir.getLineCoordinateList().size(); i++){
				x = ir.getLineCoordinateList().get(i).getMinX();
				y = ir.getLineCoordinateList().get(i).getMinY();
				Node firstNode = getNode(x, y, nodes);
				
				x = ir.getLineCoordinateList().get(i).getMaxX();
				y = ir.getLineCoordinateList().get(i).getMaxY();
				Node secondNode = getNode(x, y, nodes);
				
				bind(firstNode, secondNode, targetPane);
			}
		}
		
		
		targetPane.setOnDragOver(event -> { 
			
                Dragboard db = event.getDragboard();
                 
                if(db.hasImage()){
                    event.acceptTransferModes(TransferMode.COPY);
                }
                 
                event.consume();
        });
 
        targetPane.setOnDragDropped(event -> {
        	
                Dragboard db = event.getDragboard();
                 
                
                if(db.hasImage()){
                	
                    insertImage(db.getImage(), targetPane, event.getX(), event.getY(), null);
                     
                    event.setDropCompleted(true);
                }
                else{
                    event.setDropCompleted(false);
                }
                
        		
                event.consume();
        });
	}
	
    private void insertImage(Image i, AnchorPane ap, double x, double y, ArrayList<Node> nodeList){
    	
        Node node = new Node();
        node.setImage(i);
        
        //label Nodes by default
        node.setX(x);
        node.setY(y);
        
        node.setId("" + defaultID++);
        
        System.out.println(node.getText().getX() + " " + node.getImageView().getX());
        System.out.println(node.getText().getY() + " " + node.getImageView().getY() + "\n");
        
        if(nodeList != null)
        	nodeList.add(node);
        	
        insert(node, ap);
        ap.getChildren().addAll(node.getImageView(), node.getText());
    }
    
    private void insert(Node node, AnchorPane ap){
    	ImageView iv = node.getImageView();
    	
    	

        
        
    	iv.setOnMouseDragged(event -> {
    		
    		iv.setCursor(Cursor.HAND);
    		iv.toFront();
           		
        	if(event.getButton() == MouseButton.PRIMARY){
        		double offsetX = event.getX() - node.getImageHalfWidth();
	           	double offsetY = event.getY() - node.getImageHalfHeight();
	           		
	           	if(ap.getBoundsInLocal().contains(offsetX + 0.1, offsetY + 0.1)){
	           		node.setX(offsetX);
	           		node.setY(offsetY);
	           	}
        	}
        	
           	event.consume();
    	});
        
    	iv.setOnMouseEntered(event -> {
        	
        	if(lineIsSelected) 
        		iv.setCursor(Cursor.CROSSHAIR);
        	else 
        		iv.setCursor(Cursor.HAND);
        	
        	if(node.getState() == 0){
        		node.setImage(image.currentIcon());
				//Tooltip.install(source.getParent(), new Tooltip(source.getId()));	
        	}
        });
        
    	iv.setOnMouseExited(event -> {
        	if(node.getState() == 0)
        		node.setImage(image.notVisitedIcon());
        });
        
        
        iv.setOnMouseClicked(event -> {
        	if(event.getButton() == MouseButton.PRIMARY){
				if(lineIsSelected){
					
					if(++mouseClicks == 1){
						from = node;
					}
					
					if(mouseClicks == 2){
						bind(from, node, ap);
					}
				}
        	}
        	else if(event.getButton() == MouseButton.SECONDARY){
        		nodeContextMenu.hide();
    			nodeContextMenu.show(iv, event.getScreenX(), event.getScreenY());
        		
    			deleteNode.setOnAction(action -> {
    				//While deleting nodes it is necessary to delete all connected links
            		ArrayList<Link> linksList = node.getLinksList();
            		for(int i = 0; i < node.branchingFactor(); i++){
            			//Bug is here!!! need to remove nodes from the list of another nodes
            			ap.getChildren().remove(linksList.get(i).getLink());
            		}
            		ap.getChildren().remove(node.getImageView());
    			});
    			
    			nodeProperties.setOnAction(action -> {
    				NodeProperties.display(node);
    				
    				if(node.isStart())
    					sa.addStart(node);
    				else {
    					sa.removeAllStarts();
    				}
    			});
    			
    			event.consume();
        	}
        });
    }
    
    private void setUpLineSource(Link link,  AnchorPane ap){
    	Line line = link.getLink();
    	
    	//Highlight the line on mouse entered
    	line.setOnMouseEntered(e -> {
    		line.setCursor(Cursor.HAND);
    		line.setStroke(Color.web("#972234")); //Dark-red
    		line.setStrokeWidth(5);
    		
    	});
    	
    	line.setOnMouseExited(e -> {
    		line.setStroke(Color.BLACK);
    		line.setStrokeWidth(3);
    	});
    	
    	line.setOnMouseClicked(e -> {
    		if(e.getButton() == MouseButton.SECONDARY){
    			lineContextMenu.hide();
    			lineContextMenu.show(line, e.getScreenX(), e.getScreenY());
    			
    			deleteLink.setOnAction(action -> {
	    				if(link.getNodeFrom().contains(link)){
	    					link.getNodeFrom().getLinksList().remove(link);
	    					link.getNodeFrom().getNodesList().remove(link.getNodeTo());
	    				}
	    				
	    				if(link.getNodeTo().contains(link)){
	    					link.getNodeTo().getLinksList().remove(link);
	    					link.getNodeTo().getNodesList().remove(link.getNodeFrom());
	    				}	
	    				ap.getChildren().remove(line);
    			});
    			
    			linkProperties.setOnAction(action ->{
    				LinkProperties.display(link);
    			});
    		}
    		e.consume();
    	});
    }

	
	public static void main(String[] args) {
		launch(args);
	}
}
