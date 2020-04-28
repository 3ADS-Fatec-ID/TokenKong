package application.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DrawerButton extends Button{
	public String baseIconsPath = "application/assets/icons/";
	
	/**
	 * @param title
	 * @param iconName - icon name
	 * @param newObject - new empty screen object
	 * @param screen - current instantiated screen object
	 * @param sceneContent - screen parent component in the scene
	 */
	public DrawerButton(String title, String iconName, VBox newObject, VBox screen, Pane sceneContent) {
		Image image = new Image(this.baseIconsPath + iconName + ".png");
		ImageView imageView = new ImageView(image);
		getStyleClass().add("drawer_button");
		this.setText(title);
		this.setGraphic(imageView);
	
		this.setOnAction( new CustomEventHandler(newObject, screen, sceneContent) {
			@Override
			public void handle(ActionEvent event) {
				Pane sceneContent = this.get_sceneContent();
		    	VBox screen = this.get_screen();
				if (screen == null) {
					screen = this.get_newObject();
				}
				sceneContent.getChildren().set(0, screen);
			}
		});
	}
	
	public abstract class CustomEventHandler implements EventHandler<ActionEvent>
	{
	    VBox screen;
	    VBox newObject;
	    Pane sceneContent;

	    public CustomEventHandler(VBox newObject, VBox screen, Pane sceneContent) {
	        this.screen = screen;
	        this.newObject = newObject;
	        this.sceneContent = sceneContent;
	    }

	    public VBox get_screen() {
	        return this.screen;
	    }
	    public VBox get_newObject() {
	        return this.newObject;
	    }
	    public Pane get_sceneContent() {
	        return this.sceneContent;
	    }
	}
}