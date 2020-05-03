package application.models;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class FxmlLoader {
	private VBox view;
	
	public VBox getPage(String fileName) {
		try {
			view = FXMLLoader.load(getClass().getResource("/application/views/screens/"+fileName+".fxml"));
			System.out.println("/application/views/screens/"+fileName+".fxml");
		}catch(Exception e) {
			System.out.println("Nenhuma página foi encontrada!");
		}
		return view;
	}
}
