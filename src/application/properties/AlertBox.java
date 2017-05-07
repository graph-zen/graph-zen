package application.properties;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	public static boolean display(String title, String message){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		VBox root = new VBox(10);
		
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label(message);
		Button ok = new Button("Ok");
		
		ok.setOnAction(e -> window.close());
		
		root.getChildren().addAll(label, ok);
		root.setAlignment(Pos.CENTER);
		
		
		Scene scene = new Scene(root, 100, 100);
		window.setScene(scene);
		window.showAndWait();
		return true;
	}
}
