package application.components.DialogComponent.ProductFormDialogComponent;

import java.util.ArrayList;

import application.DAO.BrandDAO;
import application.DAO.CategoryDAO;
import application.DAO.ProductDAO;
import application.components.AlertComponent.Alert;
import application.components.ConfirmComponent.Confirm;
import application.components.ConfirmComponent.Confirm.ConfirmCallback;
import application.components.DialogComponent.Dialog;
import application.components.ImagePickerComponent.ImagePickerController;
import application.models.Brand;
import application.models.Category;
import application.models.Product;
import application.models.Product.ProductImage;
import application.screens.controllers.ProductsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ProductFormDialogController {
	
	boolean editing = false;
	
	public ProductsController productsController;
	ArrayList<Category> productCategories = new ArrayList<Category>();
	ArrayList<Brand> productBrands = new ArrayList<Brand>();	
	
	private Product product = new Product();
	final FileChooser fileChooser = new FileChooser();
	public ImagePickerController imagePickerController = new ImagePickerController();
	
	@FXML public ImageView closeDialog;
	@FXML public VBox content;
	
	@FXML public Label headerTitle;
	@FXML public ImageView headerIcon;
	@FXML public Button cancelButton;
	@FXML public Button submitButton;
	@FXML public Button deleteButton;
	@FXML public TextField productName;
	@FXML public TextField productPrice;
	@FXML public ChoiceBox<String> productCategory;
	@FXML public ChoiceBox<String> productBrand;
	
	@FXML public TextField productQuantity;
	@FXML public TextArea productDescription;
	
	public void initialize() {
		initChoiceBoxes();
		setButtonsActions();
		setInitialFormValues();
		generateImagePicker();
	}
	
	private void setInitialFormValues() {
		editing = (this.product.getId() != null);
		if(editing) {
			headerTitle.setText("Edit Product");
			productName.setText(product.getName());
			productPrice.setText(String.format("%.2f", product.getPrice()));
			productDescription.setText(product.getDescription());
			productQuantity.setText(String.format("%d", product.getQuantity()));
			productCategory.setValue(product.getCategory().name);
			productBrand.setValue(product.getBrand().name);
		}else {
			product.setImages(new ArrayList<ProductImage>());
			headerTitle.setText("New Product");
		}
		deleteButton.setVisible(editing);
	}
	
	private void setButtonsActions() {
		closeDialog.setId("closeDialog");
		cancelButton.setId("closeDialog");
		closeDialog.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event){
				close(event);
			}
		});
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				if(editing)
					deleteProduct(event);
			}
		});
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				close(event);
			}
		});
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				submit(event);
			}
		});
	}
	
	private void initChoiceBoxes() {
		productCategory.setMaxWidth(Double.MAX_VALUE);
		productBrand.setMaxWidth(Double.MAX_VALUE);
		
		loadCategories();
		loadBrands();
		ObservableList<String> productCategories = FXCollections.observableArrayList();
		ObservableList<String> productBrands = FXCollections.observableArrayList();
		
		for(Category productCategory : this.productCategories) {
			productCategories.add(productCategory.name);
		}		
		for(Brand productBrand : this.productBrands) {
			productBrands.add(productBrand.name);
		}
		
		productCategory.setItems(productCategories);
		productBrand.setItems(productBrands);
	}
	
	private void loadCategories() {
		try {
			productCategories = CategoryDAO.getAll();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void loadBrands() {
		try {
			productBrands = BrandDAO.getAll();			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void loadProduct() {
		Integer productId = product.getId();
		try {
			if(productId != null) {
				this.product = ProductDAO.getOne(productId);
				this.imagePickerController.setImages(product.getImages());
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void generateImagePicker() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/components/ImagePickerComponent/ImagePickerComponent.fxml"));
			ArrayList<ProductImage> images = product.getImages();
			imagePickerController.setImages(images);
			
			fxmlLoader.setController(imagePickerController);
			VBox imagePicker = fxmlLoader.load();
			HBox.setHgrow(imagePicker, Priority.ALWAYS);
			VBox.setMargin(imagePicker, new Insets(0, 0, 10, 0));
			content.getChildren().set(3,imagePicker);
			
			this.loadProduct();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void deleteProduct(Event event) {
		Scene scene = ((Node)event.getSource()).getScene();
		
		ConfirmCallback callback = new ConfirmCallback() {
			public EventHandler<ActionEvent>confirm(){
				EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
					@Override
					public void handle( ActionEvent event) {
						
						try {
							Confirm.close(scene);
							ProductDAO.delete(product.getId());
							reloadProductList();
							Dialog.close(scene, closeDialog);
							Alert.showAlert(scene, "Success", "Product was successfully deleted!", "success", 5000);
						}catch(Exception e) {
							System.out.println(e.getMessage());
							Alert.showAlert(scene, "An error has occurred", "Couldn't delete product", "error", 5000);
						}
					}
				};
				return eventHandler;
			}
		};
		
		Confirm.show(scene, "Warning", "Are you shure that you realy want to delete this product?", "warning", true, callback);
	}
	
	private void submit(Event event){
		if(editing) {
			updateProduct(event);			
		}else {
			createProduct(event);
		}
	}
	
	private void updateProduct(Event event) {
		Scene scene = ((Node)event.getSource()).getScene();
		
		try {
			Category chosenCategory = getChosenCategory();
			Brand chosenBrand = getChosenBrand();
			
			Product product = new Product();
			product.setId(this.product.getId());
			product.setName(productName.getText());
			if(chosenCategory != null)
				product.setCategory(chosenCategory);
			if(chosenBrand != null)
				product.setBrand(chosenBrand);
			product.setImages(imagePickerController.getImages());
			product.setDescription(productDescription.getText());
			product.setQuantity(Integer.parseInt(productQuantity.getText()));
			product.setPrice(Double.parseDouble(productPrice.getText().replace(',', '.')));
		
			this.product = ProductDAO.update(product);
			reloadProductList();
			
			if(!this.product.equals(null)) {
				setInitialFormValues();
				ArrayList<ProductImage> productImages = this.product.getImages();
				if(productImages.equals(null))
					productImages = new ArrayList<ProductImage>();
				imagePickerController.setImages(productImages);
				
				Alert.showAlert(scene, "Success", "Product was successfully updated!", "success", 5000);
			}else {
				Alert.showAlert(scene, "Success", "Product was successfully updated!", "success", 5000);
				Dialog.close(scene, closeDialog);
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			Alert.showAlert(scene, "An error has occurred", "Couldn't save product", "error", 5000);
		}
	}
	
	private void createProduct(Event event) {
		Scene scene = ((Node)event.getSource()).getScene();
		
		try {
			Category chosenCategory = getChosenCategory();
			Brand chosenBrand = getChosenBrand();
			
			Product product = new Product();
			product.setName(productName.getText());
			if(chosenCategory != null)
				product.setCategory(chosenCategory);
			if(chosenBrand != null)
				product.setBrand(chosenBrand);
			product.setImages(imagePickerController.getImages());
			product.setDescription(productDescription.getText());
			product.setQuantity(Integer.parseInt(productQuantity.getText()));
			product.setPrice(Double.parseDouble(productPrice.getText().replace(',', '.')));
		
			this.product = ProductDAO.create(product);
			reloadProductList();
			
			if(!this.product.equals(null)) {
				setInitialFormValues();
				ArrayList<ProductImage> productImages = this.product.getImages();
				if(productImages.equals(null))
					productImages = new ArrayList<ProductImage>();
				imagePickerController.setImages(productImages);
				
				Alert.showAlert(scene, "Success", "Product was successfully created!", "success", 5000);
			}else {
				Alert.showAlert(scene, "Success", "Product was successfully created!", "success", 5000);
				Dialog.close(scene, closeDialog);
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			Alert.showAlert(scene, "An error has occurred", "Could not create product", "error", 5000);
		}
	}
	
	private Category getChosenCategory() {
		Category chosenCategory = null;
		String categoryName = productCategory.getValue();
		if(categoryName != null) {
			for(Category category : productCategories) {
				if(category.name == productCategory.getValue()) {
					chosenCategory = category;
					break;
				}
			}
		}
		return chosenCategory;
	}
	
	private Brand getChosenBrand() {
		Brand chosenBrand = null;
		String brandName = productBrand.getValue();
		if(brandName != null) {
			for(Brand brand : productBrands) {
				if(brand.name == productBrand.getValue()) {
					chosenBrand = brand;
					break;
				}
			}
		}
		return chosenBrand;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	private void reloadProductList() {
		this.productsController.loadProducts();
	}
	
	private void close(Event event){
		Node target = (Node)event.getTarget();
		Scene scene = ((Node)event.getSource()).getScene();
		Dialog.close(scene, target);
	}
}
