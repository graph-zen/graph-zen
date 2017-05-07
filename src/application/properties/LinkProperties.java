package application.properties;

import application.elements.Link;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LinkProperties {
	private static boolean save;
	private static double dist;
	
	public static void display(Link link) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle("Line Properties");
		
		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Line Properties");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		root.add(scenetitle, 0, 0, 2, 1);

		Label label = new Label("Connection:");
		root.add(label, 0, 1);


//		TextField labelTextField = new TextField();
//		root.add(labelTextField, 1, 1);
		
		Text connection = new Text("From " + link.getNodeFrom().getId() + " to " + link.getNodeTo().getId());
		root.add(connection, 1, 1);
		
//		id = node.getId();
//		labelTextField.setOnAction(e -> {
//			id = labelTextField.getText();
//		});
//		
//		labelTextField.setPromptText(node.getId());

		Label hv = new Label("Distance:");
		root.add(hv, 0, 2);

		TextField distanceFeild = new TextField();
		root.add(distanceFeild, 1, 2);
		distanceFeild.setPromptText("" + link.getDistance());
		
		
		dist = link.getDistance();
		distanceFeild.setOnMouseMoved(e -> {
			try{
				if(!distanceFeild.getText().equals("")){
					dist = new Double(distanceFeild.getText());
				}
				save = true;
				//AlertBox.display("Error", "OK!");
			}
			catch(NumberFormatException e1){
				AlertBox.display("Error", "Illegal argument!");
				save  = false;
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
					link.setDistance(dist);
					System.out.println("Link between nodes " + link.getNodeFrom().getId() + " and " + link.getNodeTo().getId() + " is equal to: " + link.getDistance());
				    actiontarget.setText("Changes are saved");
				}
				else actiontarget.setText("Enable to save changes");
			});
		
		
		Scene scene = new Scene(root, 400, 275);
		window.setScene(scene);
		window.showAndWait();
		

	}

}
