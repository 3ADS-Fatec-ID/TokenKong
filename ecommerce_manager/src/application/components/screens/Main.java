package application.components.screens;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.stage.Stage;

import java.util.ArrayList;

import application.components.*;

public class Main extends Application {

	Products productsScreen = null;
	Categories categoriesScreen = null;
	Home homeScreen = new Home();
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(final Stage stage) throws Exception {

		stage.setTitle("P.I");

		// create the drawer component
		Drawer drawer = new Drawer();

		// create the header component
		Pane header = new Header(drawer.toggler_button);

		Pane content = new Pane();
		content.getChildren().add(this.homeScreen);

		// generate drawer buttons
		ArrayList<DrawerButton> drawer_buttons = new ArrayList<DrawerButton>();
		
		drawer_buttons.add(new DrawerButton("Home", "home-outline", new Home(), this.homeScreen, content));
		drawer_buttons.add(new DrawerButton("Products", "package-variant-closed", new Products(), this.productsScreen, content));
		drawer_buttons.add(new DrawerButton("Categories", "view-grid-outline", new Categories(), this.categoriesScreen, content));

		drawer.getChildren().addAll(drawer_buttons);

		// region between the logout button and the other buttons
		Region region = new Region();
		VBox.setVgrow(region, Priority.ALWAYS);
		
		DrawerButton logout_button = new DrawerButton("Logout", "logout", null, null, null);

		drawer.getChildren().addAll(region, logout_button);
		drawer.setId("drawer");

		// create a stack component to stack the drawer on top of the contents
		StackPane stack = new StackPane();
		// set the alignment of layers childrens
		stack.setAlignment(Pos.TOP_LEFT);
		// add the layers childrens
		stack.getChildren().addAll(content, drawer);
		// create the border pane to separate the header from the stack component
		BorderPane border = new BorderPane();
		// add the stack on the middle of the scene
		border.centerProperty().set(stack);
		// add the header on the top of the scene
		border.topProperty().set(header);
		// add the border pane to the scene
		Scene scene = new Scene(border);
		scene.getStylesheets().add(getClass().getResource("/application/styles/Main.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
}