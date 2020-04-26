package main.components;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class Drawer extends VBox {

    public Button toggler_button;

    public void generateButtons(String[][] buttonsData) {
        for(Integer i = 0; i < buttonsData.length; i++){
            Image image = new Image("main/assets/icons/"+buttonsData[i][1]);
            ImageView imageView = new ImageView(image);
            Button button = new Button(buttonsData[i][0], imageView);
            button.getStyleClass().add("drawer_button");

            this.getChildren().add(button);
        }
    }

    /** creates a Drawer containing a vertical alignment of the given nodes */
    public Drawer(Node... nodes) {
        getStylesheets().add(Drawer.class.getResource("../styles/Drawer.css").toExternalForm());
        getChildren().addAll(nodes);

        // create and set a button to hide and show
        this.toggler_button = new Button();

        setId("drawer");
    
        // apply the animations when the button is pressed.
        this.toggler_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent actionEvent) {
                // create an animation to close the drawer.
                final double startWidth = getWidth();
                final Animation closeDrawer = new Transition() {
                    { setCycleDuration(Duration.millis(250)); }
                    protected void interpolate(double frac) {
                        final double curWidth = startWidth * (1.0 - frac);
                        setTranslateX(-startWidth + curWidth);
                    }
                };
                closeDrawer.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                    @Override 
                    public void handle(ActionEvent actionEvent) {
                        setVisible(false);
                    }
                });
        
                // create an animation to open the drawer.
                final Animation openDrawer = new Transition() {
                    { setCycleDuration(Duration.millis(250)); }
                    protected void interpolate(double frac) {
                        final double curWidth = startWidth * frac;
                        setTranslateX(-startWidth + curWidth);
                    }
                };

                openDrawer.onFinishedProperty().set(
                    new EventHandler<ActionEvent>() {
                        @Override 
                        public void handle(ActionEvent actionEvent) {}
                    }
                );
        
                if (openDrawer.statusProperty().get() == Animation.Status.STOPPED && closeDrawer.statusProperty().get() == Animation.Status.STOPPED) {
                    if (isVisible()) {
                        closeDrawer.play();
                    } else {
                        setVisible(true);
                        openDrawer.play();
                    }
                }
            }
        });
    }
}