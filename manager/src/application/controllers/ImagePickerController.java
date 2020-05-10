package application.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.models.ProductImage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImagePickerController {
	
	private ArrayList<ProductImage> images= new ArrayList<ProductImage>();
	final FileChooser fileChooser = new FileChooser();
	
	@FXML VBox image_picker;
	@FXML ScrollPane image_queue;
	@FXML Button chooseImage_button;
	@FXML HBox image_queue_content;
	@FXML Label images_names;
	
	@FXML
	public void initialize() {
		this.updateImageQueue();
		this.chooseImage_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Node source = (Node)event.getSource();
				Stage stage = (Stage)source.getScene().getWindow();
				List<File> files = fileChooser.showOpenMultipleDialog(stage);
				
				if (files.size() > 0) {
					String imagesNames = "";
					int i = 0;
					for(File file:files) {
						if(i == 0) {
							imagesNames += file.getName();						
						}else {
							imagesNames += " / " + file.getName();
						}
						ProductImage image = new ProductImage(file.toURI().toString());
						image.setName(file.getName());
						images.add(image);
						i++;
					}
					images_names.setText(imagesNames);
					updateImageQueue();
				}				
			}
        });
	}
	
	private void updateImageQueue() {
		if(image_queue_content != null) {
			if(image_queue_content.getChildren().size() > 0) {
				image_queue_content.getChildren().clear();			
			}
			ArrayList<ImageView> imageList = new ArrayList<ImageView>();
			
			if(images.size() > 0) {
				for(ProductImage image:images) {
					
					ImageView imageComponent = new ImageView(image);
					imageComponent.setFitWidth(100);
					imageComponent.setFitHeight(100);
					imageComponent.setPreserveRatio(true);
					imageList.add(imageComponent);
					
				}
			}else {
				ImageView image = new ImageView(new Image("/application/assets/images/products/no_image.png"));
				imageList.add(image);
			}
			
			image_queue_content.getChildren().addAll(imageList);
		}
	}
	
	public void setImages(ArrayList<ProductImage> newImages) {
		this.images.clear();
		this.images = newImages;
		this.updateImageQueue();
	}
	
	public ArrayList<ProductImage> getImages(){
		return this.images;
	}
}
