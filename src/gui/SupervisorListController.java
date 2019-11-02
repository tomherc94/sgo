package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import app.Main;
import db.DbIntegrityException;
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
import model.entities.Supervisor;
import model.entities.enums.PostoGrad;
import model.services.SupervisorService;

public class SupervisorListController implements Initializable, DataChangeListener {

	private SupervisorService service;

	@FXML
	private TableView<Supervisor> tableViewSupervisor;

	@FXML
	private TableColumn<Supervisor, Integer> tableColumnId;

	@FXML
	private TableColumn<Supervisor, String> tableColumnNome;

	@FXML
	private TableColumn<Supervisor, String> tableColumnIdentidade;

	@FXML
	private TableColumn<Supervisor, PostoGrad> tableColumnPostoGrad;

	@FXML
	private TableColumn<Supervisor, String> tableColumnCelular;

	@FXML
	private TableColumn<Supervisor, String> tableColumnLogin;

	@FXML
	private TableColumn<Supervisor, Supervisor> tableColumnEDIT;

	@FXML
	private TableColumn<Supervisor, Supervisor> tableColumnREMOVE;

	@FXML
	private Button btNovo;

	private ObservableList<Supervisor> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Supervisor obj = new Supervisor();
		createDialogForm(obj, "/gui/SupervisorForm.fxml", parentStage);
	}

	public void setSupervisorService(SupervisorService service) {
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
		tableColumnIdentidade.setCellValueFactory(new PropertyValueFactory<>("identidade"));
		tableColumnPostoGrad.setCellValueFactory(new PropertyValueFactory<>("postoGrad"));
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableColumnLogin.setCellValueFactory(new PropertyValueFactory<>("login"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSupervisor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Supervisor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSupervisor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Supervisor obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SupervisorFormController controller = loader.getController();
			controller.setSupervisor(obj);
			controller.setSupervisorService(new SupervisorService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados do Supervisor");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Supervisor, Supervisor>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Supervisor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SupervisorForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Supervisor, Supervisor>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Supervisor obj, boolean empty) {
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

	private void removeEntity(Supervisor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmacao", "Deseja deletar o item selecionado?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
}
