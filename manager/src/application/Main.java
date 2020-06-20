package application;

import application.screens.controllers.SigninController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

public class Main extends Application {
	
	private double xOffset = 0;
    private double yOffset = 0;
    
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/screens/SigninView.fxml"));
			SigninController signinController = new SigninController();
			fxmlLoader.setController(signinController);
			Parent root = fxmlLoader.load();
			
			root.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                xOffset = event.getSceneX();
	                yOffset = event.getSceneY();
	            }
			});
	        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                stage.setX(event.getScreenX() - xOffset);
	                stage.setY(event.getScreenY() - yOffset);
	            }
	        });
	            
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			stage.setScene(scene);
			DropShadow shadow = new DropShadow(15, new Color(0, 0 ,0 , 0.3));
			shadow.setOffsetY(5);
			stage.getScene().getRoot().setEffect(shadow);
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Parent getPagesParent ( Event event ) {
		Node source = (Node)event.getSource();
		Scene scene = source.getScene();
		Parent parent = scene.getRoot();
		Parent parent2 = (Parent)parent.getChildrenUnmodifiable().get(0);
		Parent parent3 = (Parent)parent2.getChildrenUnmodifiable().get(0);
		Parent pagesParent = (Parent)parent3.getChildrenUnmodifiable().get(1);
		return pagesParent;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
