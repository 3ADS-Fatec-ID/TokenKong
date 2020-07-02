package application.screens.controllers;

import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HomeController {
	@FXML public Button productsButton;
	
	@FXML
	public void initialize() {
		productsButton.setOnAction(openProductsScreen());
	}
	
	private EventHandler<ActionEvent> openProductsScreen() {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() { 
			@Override 
			public void handle(ActionEvent event) {
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/screens/ProductsView.fxml"));
					HBox.setHgrow(page, Priority.ALWAYS);
					
					if(pagesParent.getChildren().isEmpty()) {
						pagesParent.getChildren().add(page);
					}else {
						pagesParent.getChildren().set(0, page);			
					}
				}catch(Exception e) {
					System.out.println(e.getMessage());

					//show error message
					
				}
			}
		};
		
		return eventHandler;
	}
}
