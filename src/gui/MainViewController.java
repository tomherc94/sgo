package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.EquipamentoService;
import model.services.LivroService;
import model.services.SupervisorService;
import model.services.TurnoService;

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
		loadView("/gui/SupervisorList.fxml", (SupervisorListController controller) -> {
			controller.setSupervisorService(new SupervisorService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemEquipamentoAction() {
		loadView("/gui/EquipamentoList.fxml", (EquipamentoListController controller) -> {
			controller.setEquipamentoService(new EquipamentoService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemTurnoAction() {
		loadView("/gui/TurnoList.fxml", (TurnoListController controller) -> {
			controller.setTurnoService(new TurnoService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemLivroAction() {
		loadView("/gui/LivroList.fxml", (LivroListController controller) -> {
			controller.setLivroService(new LivroService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemHistoricoAction() {
		System.out.println("onMenuItemHistoricoAction");
	}

	@FXML
	public void onMenuItemSobreAction() {
		loadView("/gui/SobreView.fxml", x -> {});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializeAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
