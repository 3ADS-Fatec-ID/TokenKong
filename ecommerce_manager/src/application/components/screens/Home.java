package application.components.screens;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import application.components.*;

public class Home extends VBox {

    public Home( Node... nodes ){
        paddingProperty().setValue(new Insets(0, 30, 0, 30));
        getStylesheets().add(getClass().getResource("/application/styles/Home.css").toExternalForm());
        
        // create the breadcrumb fields
        String [][] crumbsData = {
            //[title, icon]
            {"Home","home-outline"}
        };
        //create breadcrumb component
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.generateCrumbs(crumbsData);
        getChildren().add(breadcrumb);

        getChildren().addAll(nodes);

    }
}