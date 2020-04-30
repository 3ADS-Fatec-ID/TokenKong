package application.components.screens;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import application.components.*;

public class Categories extends VBox {

    public Categories(){
        paddingProperty().setValue(new Insets(0, 30, 0, 30));
        getStylesheets().add(getClass().getResource("/application/styles/Categories.css").toExternalForm());
        
        // create the breadcrumb fields
        String [][] crumbsData = {
            //[title, icon]
            {"Home","home-outline"},
            {"Categories","view-grid-outline"}
        };
        //create breadcrumb component
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.generateCrumbs(crumbsData);
        getChildren().add(breadcrumb);

    }
}
