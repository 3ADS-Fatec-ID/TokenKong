package application.components.DrawerComponent;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DrawerController implements Initializable{
	
	public boolean oppened = false;
		
	@FXML public Button toggle_drawer;
	
	@FXML public Button home_button;
	@FXML public Button categories_button;
	@FXML public Button products_button;
	@FXML public Button providers_button;
	@FXML public Button users_button;
	@FXML public Button logout_button;
	
	@FXML public VBox drawer;
	@FXML public VBox pseudoDrawer;
	@FXML public HBox content;
	@FXML public StackPane composition;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.changePage("HomeView");
	}
	
	@FXML public void showHomePage(ActionEvent event) {
		if(!content.getChildren().isEmpty()) {
			Node page = content.getChildren().get(0);
			if(!page.getId().toString().equals("home")) {
				this.changePage("HomeView");
			}
		}else {
			this.changePage("HomeView");
		}
	}
	@FXML public void showCategoriesPage(ActionEvent event) {
		if(!content.getChildren().isEmpty()) {
			Node page = content.getChildren().get(0);
			if(!page.getId().toString().equals("categories")) {
				this.changePage("CategoriesView");
			}
		}else {
			this.changePage("CategoriesView");
		}
	}
	@FXML public void showProductsPage(ActionEvent event) {
		if(!content.getChildren().isEmpty()) {
			Node page = content.getChildren().get(0);;
			if(!page.getId().toString().equals("products")) {
				this.changePage("ProductsView");
			}
		}else {
			this.changePage("ProductsView");
		}
	}
	@FXML public void showProvidersPage(ActionEvent event) {
		if(!content.getChildren().isEmpty()) {
			Node page = content.getChildren().get(0);;
			if(!page.getId().toString().equals("providers")) {
				this.changePage("ProvidersView");
			}
		}else {
			this.changePage("ProvidersView");
		}
	}
	@FXML public void showUsersPage(ActionEvent event) {
		if(!content.getChildren().isEmpty()) {
			Node page = content.getChildren().get(0);;
			if(!page.getId().toString().equals("users")) {
				this.changePage("UsersView");
			}
		}else {
			this.changePage("UsersView");
		}
	}
	
	public void changePage(String pageName) {
		
		try {
			VBox page = FXMLLoader.load(getClass().getResource("/application/screens/"+pageName+".fxml"));
			HBox.setHgrow(page, Priority.ALWAYS);
			
			if(content.getChildren().isEmpty()) {
				content.getChildren().add(page);
			}else {
				content.getChildren().set(0, page);			
			}
			
		}catch(Exception e) {
			System.out.println("Nenhuma página foi encontrada!");
		}
	}
	
	
	@FXML public void logout(ActionEvent event) {

	}
	
	@FXML public void toggleDrawer(ActionEvent event) {

        final double startWidth = 160;
        final double minDrawerWidth = 45;
        final double minPseudoDrawerWidth = 40;
        
        final Animation closeDrawer = new Transition() {
            { setCycleDuration(Duration.millis(250)); }
            protected void interpolate(double frac) {
                pseudoDrawer.setPrefWidth(((startWidth - minPseudoDrawerWidth ) * (1.0 - frac)) + minPseudoDrawerWidth);
                drawer.setMaxWidth(((startWidth - minDrawerWidth) * (1.0 - frac)) + minDrawerWidth);
            }
        };
        
        final Animation openDrawer = new Transition() {
            { setCycleDuration(Duration.millis(250)); }
            protected void interpolate(double frac) {
                pseudoDrawer.setPrefWidth(((startWidth - minPseudoDrawerWidth) * frac) + minPseudoDrawerWidth - (5 * frac));
                drawer.setMaxWidth(((startWidth - minDrawerWidth) * frac) + minDrawerWidth);
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
