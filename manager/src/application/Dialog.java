package application;

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
	
	public static void initializeDialog(StackPane composition) {
		
		try {
			File file = new File("src\\application\\views\\components\\stylesheets\\BaseDialog.css");
			HBox background = new HBox();
			background.setCursor(Cursor.HAND);
			background.getStylesheets().add(file.toURI().toURL().toString());
			background.getStyleClass().add("background");
			HBox.setHgrow(background, Priority.ALWAYS);
			VBox.setVgrow(background, Priority.ALWAYS);
			background.setAlignment(Pos.CENTER);
			
			//create dialog content area
			HBox h = new HBox();
			HBox.setHgrow(h, Priority.ALWAYS);
			h.setPadding(new Insets(16));
			VBox v = new VBox();
			v.setAlignment(Pos.CENTER);
			v.getChildren().add(h);
			background.getChildren().add(v);
			background.setId("dialogBackground");
			v.setId("dialogVAlignment");
			h.setId("dialogHAlignment");
			background.setOnMouseClicked(closeDialog());
			background.setId("dialogBackground");
			background.setVisible(false);
			
			composition.getChildren().add(2, background);
			
		}catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}
	
	public static void setDialog ( Event event, VBox content ) throws Exception {
		try {
			//add dialog to screen
			Node source = (Node)event.getSource();
			Scene scene = source.getScene();
			StackPane composition = (StackPane)scene.getRoot();

			HBox background = (HBox)composition.getChildren().get(2);
			VBox v = (VBox)background.getChildren().get(0);
			HBox h = (HBox)v.getChildren().get(0);
			if(h.getChildren().size() == 0) {
				h.getChildren().add(content);
			}else{
				h.getChildren().set(0, content);
			}
			background.setVisible(true);
			
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
