package application.components;

import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Header extends Pane {
    public Header( Button drawer_toggler ){

        getStylesheets().add(getClass().getResource("/application/styles/Header.css").toExternalForm());
        
        //create the header shadow
        DropShadow drop = new DropShadow();
        drop.setBlurType(BlurType.GAUSSIAN);
        drop.setColor(Color.valueOf("#00000009"));    
        drop.setOffsetX(0);  
        drop.setOffsetY(3);  
        drop.setSpread(0.2);
        drop.setRadius(5);
        
        // create the header button handler
        Image image = new Image("application/assets/icons/menu.png");
        ImageView icon = new ImageView(image);
        drawer_toggler.setGraphic(icon);
        drawer_toggler.setId("drawer_toggler");
        
        setId("header");
        setEffect(drop);

        getChildren().add(drawer_toggler);
    }
}