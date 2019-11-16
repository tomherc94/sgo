package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import app.Main;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Equipamento;
import model.entities.enums.Estado;
import model.entities.enums.Tipo;
import model.services.EquipamentoService;

public class EquipamentoListController implements Initializable, DataChangeListener {

	private EquipamentoService service;

	@FXML
	private TableView<Equipamento> tableViewEquipamento;

	@FXML
	private TableColumn<Equipamento, Integer> tableColumnId;

	@FXML
	private TableColumn<Equipamento, String> tableColumnNome;

	@FXML
	private TableColumn<Equipamento, Tipo> tableColumnTipo;

	@FXML
	private TableColumn<Equipamento, Estado> tableColumnEstadoAtual;

	@FXML
	private TableColumn<Equipamento, Equipamento> tableColumnEDIT;

	@FXML
	private TableColumn<Equipamento, Equipamento> tableColumnREMOVE;

	@FXML
	private Button btNovo;

	private ObservableList<Equipamento> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Equipamento obj = new Equipamento();
		createDialogForm(obj, "/gui/EquipamentoForm.fxml", parentStage);
	}

	public void setEquipamentoService(EquipamentoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();

	}

	// padrao para iniciar o comportamento das colunas
	private void initializeNode() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		tableColumnEstadoAtual.setCellValueFactory(new PropertyValueFactory<>("estadoAtual"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEquipamento.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Equipamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewEquipamento.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Equipamento obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			EquipamentoFormController controller = loader.getController();
			controller.setEquipamento(obj);
			controller.setEquipamentoService(new EquipamentoService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados do Equipamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage); // init pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL); // impede o acesso da janela anterior enquanto a mesma nao
																// for fechada
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Equipamento, Equipamento>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Equipamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/EquipamentoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Equipamento, Equipamento>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Equipamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Equipamento obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmacao", "Deseja deletar o item selecionado?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao remover", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
}
