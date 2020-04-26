package main.components.screens;

import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.components.*;

public class Principal extends Application {
  public static void main(String[] args) throws Exception { 
    launch(args); 
  }

  public void start(final Stage stage) throws Exception {

    stage.setTitle("P.I");

    // create the drawer component
    String [][] buttonsData = {
      //[title, icon]
      {"Home", "home-outline.png"},
      {"Products", "package-variant-closed.png"},
      {"Categories", "view-grid-outline.png"}
    };
    
    Drawer drawer = new Drawer();
    drawer.generateButtons(buttonsData);
    
    //region between the logout button and the other buttons
    Region region = new Region();
    VBox.setVgrow(region, Priority.ALWAYS);

    //logout button
    Image image = new Image("main/assets/icons/logout.png");
    ImageView icon = new ImageView(image);
    Button logout_button = new Button("Logout", icon);
    logout_button.getStyleClass().add("drawer_button");

    drawer.getChildren().addAll(region,logout_button);
    drawer.setId("drawer");
    
    // create the header component
    Pane header = new Header( drawer.toggler_button );

    Products products = new Products();
    VBox content = products;

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
    scene.getStylesheets().add(Principal.class.getResource("../../styles/Principal.css").toExternalForm());
    stage.setScene(scene);
    stage.show();
  }
}