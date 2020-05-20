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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImagePickerController {
	
	ArrayList<Integer> idsToDelete = new ArrayList<Integer>();
	
	private ArrayList<ProductImage> images= new ArrayList<ProductImage>();
	
	
	final FileChooser fileChooser = new FileChooser();
	
	ImageContextMenu contextMenu = new ImageContextMenu();
	
	@FXML VBox image_picker;
	@FXML ScrollPane image_queue;
	@FXML Button chooseImage_button;
	@FXML HBox image_queue_content;
	@FXML Label images_names;
	
	@FXML
	public void initialize() {
		updateImageQueue();
		chooseImage_button.setOnAction(chooseImage());
		prepareContextMenu();
	}
	
	private void prepareContextMenu() {
		
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
		
		MenuItem menuItemRemove = new MenuItem("Remove");
		menuItemRemove.setOnAction(removeImage());
		contextMenu.getItems().add(menuItemRemove);
		
		MenuItem menuItemSetCoverImage = new MenuItem("Set as cover");
		menuItemSetCoverImage.setOnAction(setCoverImage());		
		contextMenu.getItems().add(menuItemSetCoverImage);
		
	}
	
	private EventHandler<ActionEvent> chooseImage() {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
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
        };
		
		return eventHandler;
	}
	
	private EventHandler<ActionEvent> setCoverImage() {
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!contextMenu.getImage().equals(null)) {
					//PRECISA SER FEITO
				}
			}
		};
		
		return eventHandler;
	}
	
	private EventHandler<ActionEvent> removeImage() {
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!contextMenu.getImage().equals(null)) {
					ProductImage image = contextMenu.getImage();
					if(!image.getId().equals(null)) {
			    		idsToDelete.add(image.getId());
			    	}
			    	images.remove(image.getIndex().intValue());
			    	contextMenu.setImage(null);
			    	updateImageQueue();
				}
			}
		};
		
		return eventHandler;
	}
	
	private void updateImageQueue() {
		if(image_queue_content != null) {
			if(image_queue_content.getChildren().size() > 0) {
				image_queue_content.getChildren().clear();			
			}
			ArrayList<ImageView> imageList = new ArrayList<ImageView>();
			
			if(images.size() > 0) {
				for(int i = 0; i < images.size(); i++) {
					ProductImage image = images.get(i);
					image.setIndex(i);
					
					ImageView imageComponent = new ImageView(image);
					imageComponent.setFitWidth(100);
					imageComponent.setFitHeight(100);
					imageComponent.setPreserveRatio(true);
					
					imageComponent.setOnContextMenuRequested( new EventHandler<ContextMenuEvent>() {
						 
			            @Override
			            public void handle(ContextMenuEvent event) {
			            	ProductImage image = (ProductImage)((ImageView)event.getTarget()).getImage();
			            	contextMenu.setImage(image);
			            	contextMenu.show(imageComponent , event.getScreenX(), event.getScreenY());
			            }
			        
					});
					
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
		
		for(int i = 0; i < newImages.size(); i++) {
			newImages.get(i).setIndex(i);
		}
		this.images = newImages;
		this.updateImageQueue();
	}
	
	public ArrayList<ProductImage> getImages(){
		return this.images;
	}
	
	public ArrayList<Integer> getIdsToDelete() {
		return idsToDelete;
	}
}

class ImageContextMenu extends ContextMenu{
	
	private ProductImage image = null;
	
	public ProductImage getImage() {
		return image;
	}

	public void setImage(ProductImage image) {
		this.image = image;
	}
}
