package application.components;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class Breadcrumb extends FlowPane{

    public void generateCrumbs( String [][] crumbsData ){
        for( Integer i = 0; i < crumbsData.length; i++ ){

            Image image = new Image("/application/assets/icons/"+crumbsData[i][1]+".png");
            
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(24);
            imageView.setFitWidth(24);

            Button button = new Button(crumbsData[i][0],imageView);
            button.getStyleClass().add("crumb");
            
            this.getChildren().add(button);

            if(i < crumbsData.length - 1){
                Label divisor = new Label("/");
                this.getChildren().add(divisor);
            }
        }
    }

    public Breadcrumb(){
        getStylesheets().add(getClass().getResource("/application/styles/Breadcrumb.css").toExternalForm());
        setOrientation(Orientation.HORIZONTAL);
        paddingProperty().setValue(new Insets(16,16,16,16));
        setVgap(0);
        setHgap(4);
    }
}