package application.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

public class ProductsController {
	@FXML
	public ScrollPane scroller;
	@FXML
	public FlowPane products_grid;

	@FXML
	public void initialize() {
		scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
            	products_grid.setPrefWidth(bounds.getWidth());
            }
        });
	}
}
