package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import application.models.DB;
import application.models.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProvidersController {
	private DB database = new DB();
	public ObservableList<Provider> providers = FXCollections.observableArrayList();
	
	@FXML 
	public Button go_back;
	@FXML
	public Button add_provider;
	@FXML
	public TableView<Provider> table;
	@FXML
    private TableColumn<Provider, Integer> column_code;
    @FXML
    private TableColumn<Provider, String> column_name;
    @FXML
    private TableColumn<Provider, String> column_email;
    @FXML
    private TableColumn<Provider, String> column_phone;
    @FXML
    private TableColumn<Provider, String> column_cep;
    @FXML
    private TableColumn<Provider, Integer> column_addressNumber;

	@FXML
	public void initialize() {
		
		this.go_back.setOnMouseClicked(this.goBack());
		this.add_provider.setOnAction(this.openProviderForm());
		
		column_code.setCellValueFactory(new PropertyValueFactory<>("id"));
		column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		column_email.setCellValueFactory(new PropertyValueFactory<>("email"));
		column_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		column_cep.setCellValueFactory(new PropertyValueFactory<>("cep"));
		column_addressNumber.setCellValueFactory(new PropertyValueFactory<>("addressNumber"));
		table.setItems(providers);
		
		this.loadProviders();
	}
	
	private void loadProviders() {
		
		database.connect();
		ResultSet resultSet = null;
		
		String query = "";
		query += "SELECT ";
		query += "P.* ";
		query += "FROM provider as P ";
		
		if(this.database.connection != null) {
			try{				
				PreparedStatement preparedStatement = database.connection.prepareStatement(query);
				if (preparedStatement.execute()) {
					
					resultSet = preparedStatement.getResultSet();
					if(resultSet != null) {
						
						while (resultSet.next()) {
							Provider provider = new Provider();
							provider.setId(resultSet.getInt("id"));
							provider.setName(resultSet.getString("name"));
							provider.setEmail(resultSet.getString("email"));
							provider.setAddressNumber(resultSet.getInt("address_number"));
							provider.setPhone(resultSet.getString("phone"));
							provider.setCep(resultSet.getString("cep"));
							
							providers.add(provider);
						}
						
					}
				}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}finally {
				database.disconnect();
			}	
		}
		
	}
	
	private EventHandler<MouseEvent> goBack() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent event) {
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					VBox page = FXMLLoader.load(getClass().getResource("/application/views/screens/HomeView.fxml"));
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
	
	private EventHandler<ActionEvent> openProviderForm(){
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				
				HBox pagesParent = (HBox)Main.getPagesParent(event);
				
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/screens/ProviderFormView.fxml"));
					ProviderFormController providerFormController = new ProviderFormController();
					fxmlLoader.setController(providerFormController);
					VBox page = fxmlLoader.load();
					
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
}
