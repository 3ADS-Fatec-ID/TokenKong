package application.controllers;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.Main;
import application.models.DB;
import application.models.Provider;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProviderFormController {
	private DB database = new DB();
	private Provider provider = new Provider();
	
	@FXML 
	public Button go_back;
	@FXML
	public ImageView breadcrumb_icon;
	@FXML
	public ImageView title_icon;
	@FXML 
	public Label breadcrumb_title;
	@FXML 
	public Label title;
	@FXML 
	public HBox page_content;
	@FXML 
	public Button cancel_button;
	@FXML 
	public Button submit_button;
	@FXML 
	public TextField provider_name;
	@FXML 
	public TextField provider_phone;
	@FXML 
	public TextField provider_email;
	@FXML 
	public TextField provider_cep;
	@FXML 
	public TextField provider_addressNumber;
	
	public void initialize() {
		this.setInitialFormValues();
		
		this.go_back.setOnMouseClicked(this.goBack());
		this.cancel_button.setOnMouseClicked(goBack());
		this.submit_button.setOnAction(submit());
	}
	
	private void setInitialFormValues() {
		if(this.provider.getId() != null) {
			breadcrumb_icon.setImage(new Image("application/assets/icons/light/pencil-outline.png"));
			title_icon.setImage(new Image("application/assets/icons/light/pencil-outline.png"));
			breadcrumb_title.setText("Edit");
			title.setText("Edit Provider");
			
			provider_name.setText(provider.getName());
			provider_phone.setText(provider.getPhone());
			provider_email.setText(provider.getEmail());
			provider_cep.setText(provider.getCep());
			provider_addressNumber.setText(provider.getAddressNumber().toString());
		}else {
			breadcrumb_icon.setImage(new Image("application/assets/icons/light/plus-thick.png"));
			title_icon.setImage(new Image("application/assets/icons/light/plus-thick.png"));
			breadcrumb_title.setText("New");
			title.setText("New Provider");
		}
	}
	
	private EventHandler<ActionEvent> submit(){
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				if(provider.getId() == null) {
					createProvider();
				}else {
					updateProvider();
				}
			}
		};
		
		return eventHandler;
	}
	
	private void createProvider() {
		database.connect();
		
		PreparedStatement pstmtInsert = null;
		
		if(database.connection != null) {
			try {
				
				String queryInsert = "INSERT INTO provider (name, email, phone, cep, address_number) VALUES (?, ?, ?, ?, ?)";
				
				pstmtInsert = database.connection.prepareStatement(queryInsert);
				
				pstmtInsert.setString	(1, provider_name.getText());
				pstmtInsert.setString	(2, provider_email.getText());
				pstmtInsert.setString	(3, provider_phone.getText());
				pstmtInsert.setString	(4, provider_cep.getText());
				pstmtInsert.setInt		(5, Integer.parseInt(provider_addressNumber.getText()));

				if(pstmtInsert.execute()) {
					database.connection.commit();
				}else {
					//Mostrar mensagem de erro
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(pstmtInsert != null && !pstmtInsert.isClosed()) {
						pstmtInsert.close();
					}
					database.disconnect();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	private void updateProvider() {
		
	}
	
	private EventHandler<MouseEvent> goBack() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/views/screens/ProvidersView.fxml"));
					HBox.setHgrow(page, Priority.ALWAYS);
					
					if(pagesParent.getChildren().isEmpty()) {
						pagesParent.getChildren().add(page);
					}else {
						pagesParent.getChildren().set(0, page);			
					}
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		};
		
		return eventHandler;
	}
	
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
}
