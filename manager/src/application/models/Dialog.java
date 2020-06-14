package application.models;

import java.io.File;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Dialog {
	public static void setDialog ( Event event, VBox content ) throws Exception {
		try {
			//add dialog to screen
			Node source = (Node)event.getSource();
			Scene scene = source.getScene();
			StackPane composition = (StackPane)scene.getRoot();
			
			//content.getProperties().get(key)
			
			if( composition.getChildren().size() <= 2 ) {
				//create background dialog
				File file = new File("src\\application\\views\\dialogs\\stylesheets\\BaseDialog.css");
				HBox background = new HBox();
				background.setCursor(Cursor.HAND);
				background.getStylesheets().add(file.toURI().toURL().toString());
				background.getStyleClass().add("background");
				HBox.setHgrow(background, Priority.ALWAYS);
				VBox.setVgrow(background, Priority.ALWAYS);
				background.setAlignment(Pos.CENTER);
				
				//create dialog context content area
				HBox h = new HBox();
				HBox.setHgrow(h, Priority.ALWAYS);
				h.setPadding(new Insets(16));
				h.getChildren().add(content);
				VBox v = new VBox();
				v.setAlignment(Pos.CENTER);
				v.getChildren().add(h);
				background.getChildren().add(v);
				
				background.setId("dialogBackground");
				v.setId("dialogVAlignment");
				h.setId("dialogHAlignment");
				background.setId("dialogBackground");
				
				
				background.setOnMouseClicked(closeDialog());
				
				composition.getChildren().add(2, background);	
				
			}else {
				HBox background = (HBox)composition.getChildren().get(2);
				VBox v = (VBox)background.getChildren().get(0);
				HBox h = (HBox)v.getChildren().get(0);
				h.getChildren().set(0, content);
				background.setVisible(true);
			}
			
		}catch(Exception e) {
			throw e;
		}
	}
	
	public static EventHandler<Event> closeDialog(){
		EventHandler<Event> eventHandler = new EventHandler<Event>() { 
			@Override 
			public void handle(Event event) {
				try {
					Node source = (Node)event.getSource();
					Scene scene = source.getScene();
					StackPane composition = (StackPane)scene.getRoot();
					HBox background = (HBox)composition.getChildren().get(2);
					VBox v = (VBox)background.getChildren().get(0);
					HBox h = (HBox)v.getChildren().get(0);
					
					String id = ((Node)event.getTarget()).getId();
					if(id != null) {
						if(id == background.getId() || id == v.getId() || id == h.getId() || id.equals("closeDialog")) {
							background.setVisible(false);						
						}						
					}
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
					throw e;
				}
			}
		};
		
		return eventHandler;
	}
}
