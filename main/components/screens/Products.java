package main.components.screens;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import main.components.*;

public class Products extends VBox {

    public Products( Node... nodes ){
        paddingProperty().setValue(new Insets(0, 30, 0, 30));
        getStylesheets().add(Products.class.getResource("../../styles/Products.css").toExternalForm());
        
        // create the breadcrumb fields
        String [][] crumbsData = {
            //[title, icon]
            {"Home","home-outline.png"},
            {"Products","package-variant-closed.png"}
        };
        //create breadcrumb component
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.generateCrumbs(crumbsData);
        getChildren().add(breadcrumb);

        getChildren().addAll(nodes);

    }
}