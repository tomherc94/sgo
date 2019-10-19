package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import app.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSupervisor;

	@FXML
	private MenuItem menuItemEquipamento;

	@FXML
	private MenuItem menuItemTurno;

	@FXML
	private MenuItem menuItemLivro;

	@FXML
	private MenuItem menuItemHistorico;

	@FXML
	private MenuItem menuItemSobre;

	@FXML
	public void onMenuItemSupervisorAction() {
		System.out.println("onMenuItemSupervisorAction");
	}

	@FXML
	public void onMenuItemEquipamentoAction() {
		System.out.println("onMenuItemEquipamentoAction");
	}

	@FXML
	public void onMenuItemTurnoAction() {
		System.out.println("onMenuItemTurnoAction");
	}

	@FXML
	public void onMenuItemLivroAction() {
		System.out.println("onMenuItemLivroAction");
	}

	@FXML
	public void onMenuItemHistoricoAction() {
		System.out.println("onMenuItemHistoricoAction");
	}

	@FXML
	public void onMenuItemSobreAction() {
		loadView("/gui/SobreView.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}

	}
}
