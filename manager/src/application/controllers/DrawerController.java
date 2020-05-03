package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.models.FxmlLoader;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DrawerController implements Initializable{
	
	public boolean oppened = true;
	FxmlLoader pageLoader = new FxmlLoader();
	
	@FXML public Button toggle_drawer;	
	
	@FXML public Button home_button;
	@FXML public Button categories_button;
	@FXML public Button products_button;
	@FXML public Button providers_button;
	@FXML public Button logout_button;
	
	@FXML public VBox drawer;
	@FXML public VBox pseudoDrawer;
	@FXML public HBox content;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML public void showHomePage(ActionEvent event) {
		this.changePage("HomeView");
	}
	@FXML public void showCategoriesPage(ActionEvent event) {
		this.changePage("CategoriesView");
	}
	@FXML public void showProductsPage(ActionEvent event) {
		this.changePage("ProductsView");
	}
	@FXML public void showProvidersPage(ActionEvent event) {
		this.changePage("ProvidersView");
	}
	
	public void changePage(String pageName) {
		VBox page = pageLoader.getPage(pageName);
		HBox.setHgrow(page, Priority.ALWAYS);
		if(content.getChildren().isEmpty()) {
			content.getChildren().add(page);
		}else {
			content.getChildren().set(0, page);			
		}
	}
	
	
	@FXML public void logout(ActionEvent event) {

	}
	
	@FXML public void toggleDrawer(ActionEvent event) {

        final double startWidth = 160;
        
        final Animation closeDrawer = new Transition() {
            { setCycleDuration(Duration.millis(250)); }
            protected void interpolate(double frac) {
                final double curWidth = ((startWidth - 40) * (1.0 - frac)) + 40;
                pseudoDrawer.setPrefWidth(curWidth);
                drawer.setMaxWidth(curWidth);
            }
        };
        
        final Animation openDrawer = new Transition() {
            { setCycleDuration(Duration.millis(250)); }
            protected void interpolate(double frac) {
                final double curWidth = ((startWidth - 40) * frac) + 40;
                pseudoDrawer.setPrefWidth(curWidth);
                drawer.setMaxWidth(curWidth);
            }
        };
        
        closeDrawer.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent actionEvent) {
            	oppened = false;
            }
        });

        openDrawer.onFinishedProperty().set(
            new EventHandler<ActionEvent>() {
                @Override 
                public void handle(ActionEvent actionEvent) {
                	oppened = true;
                }
            }
        );

        if (openDrawer.statusProperty().get() == Animation.Status.STOPPED && closeDrawer.statusProperty().get() == Animation.Status.STOPPED) {
            if (oppened == true) {
                closeDrawer.play();
            } else {
                openDrawer.play();
            }
        }
	}
	
}
