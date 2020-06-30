package application;

import java.io.File;

import application.DAO.UserDAO;
import application.components.AlertComponent.Alert;
import application.components.ConfirmComponent.Confirm;
import application.components.DialogComponent.Dialog;
import application.components.DrawerComponent.DrawerController;
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
    
	@Override
	public void start(Stage stage) {
		try {
			if(UserDAO.isUserLogged()) {
				openAppScene(stage);
			}else {
				openSigninScene(stage);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void openAppScene(Stage stage) throws Exception {
		File file = new File("src\\application\\components\\DrawerComponent\\DrawerComponent.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(file.toURI().toURL());
		DrawerController drawerController = new DrawerController();
		fxmlLoader.setController(drawerController);
		Parent drawer = fxmlLoader.load();
		
		Dialog.initializeDialog(drawerController.composition);
		Alert.initializeAlert(drawerController.composition);
		Confirm.initializeConfirm(drawerController.composition);
		
		Scene scene = new Scene(drawer);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void openSigninScene(Stage stage) throws Exception {
		File file = new File("src\\application\\screens\\SigninView.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(file.toURI().toURL());
		SigninController signinController = new SigninController();
		fxmlLoader.setController(signinController);
		Parent root = fxmlLoader.load();
		
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	signinController.xOffset = event.getSceneX();
            	signinController.yOffset = event.getSceneY();
            }
		});
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - signinController.xOffset);
                stage.setY(event.getScreenY() - signinController.yOffset);
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
