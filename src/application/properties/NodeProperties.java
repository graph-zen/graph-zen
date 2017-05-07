package application.properties;


import application.elements.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NodeProperties {

	public static boolean start = false;
	private static boolean goal = false;
	private static double h;
	private static String id;
	private static boolean save;
	
	public static void display(Node node){
		start = node.isStart();
		goal = node.isGoal();
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		//VBox root = new VBox();
		
		window.setTitle("Node Properties");
		
		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Properties");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		root.add(scenetitle, 0, 0, 2, 1);

		Label label = new Label("Label:");
		root.add(label, 0, 1);


		TextField labelTextField = new TextField();
		root.add(labelTextField, 1, 1);
		
		id = node.getId();
		labelTextField.setOnMouseMoved(e -> {
			id = labelTextField.getText();
		});
		
		labelTextField.setPromptText(node.getId());

		Label hv = new Label("Heuristic value:");
		root.add(hv, 0, 2);

		TextField hvField = new TextField();
		root.add(hvField, 1, 2);
		hvField.setPromptText("" + node.getHeuristic());
//		final Text alert = new Text();
//		root.add(alert, 1, 6);
		
//		alert.setFill(Color.FIREBRICK);
//		alert.setVisible(false);
		
		h = node.getHeuristic();
		hvField.setOnMouseMoved(e -> {
			try{
				if(!hvField.getText().equals("")){
					h = new Double(hvField.getText());
					System.out.println("Heuristic for the node: " + node.getId() + " is: " + h);
				}
				save = true;
			}
			catch(NumberFormatException e1){
				AlertBox.display("Error", "Illegal argument!");
				save  = false;
			}
		});
		

		
		//A checkbox without a caption
		CheckBox startBox = new CheckBox("Start");
		root.add(startBox, 0, 3);
	

    	if(node.isStart()) startBox.setSelected(true);
    	else startBox.setSelected(false);
    	
		startBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov,
	            Boolean old_val, Boolean new_val) {
	        	start = !start;
	        	//System.out.println("here");
	        	
	        }
	    });
		

		//A checkbox with a string caption
		CheckBox goalBox = new CheckBox("Goal");
		root.add(goalBox, 1, 3);

    	if(node.isGoal()) goalBox.setSelected(true);
    	else goalBox.setSelected(false);

		goalBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov,
	            Boolean old_val, Boolean new_val) {
	        	goal = !goal;
	        	
	        }
	    });
		
		
		Button btn = new Button("Save");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBtn.getChildren().add(btn);
		root.add(hbBtn, 0, 5);
		

		final Text actiontarget = new Text();
		root.add(actiontarget, 1, 7);
		actiontarget.setFill(Color.FIREBRICK);
		
			btn.setOnAction( e -> {
				if(save){
					node.setId(id);
					node.setHeuristic(h);
					node.setGoal(goal);
					node.setStart(start);
					System.out.println(start);
				    actiontarget.setText("Changes are saved");
				}
				else actiontarget.setText("Enable to save changes");
			});
		
		
		Scene scene = new Scene(root, 400, 275);
		window.setScene(scene);
		window.showAndWait();
		

	}
}
