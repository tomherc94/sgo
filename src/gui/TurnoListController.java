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
import model.entities.Turno;
import model.services.TurnoService;

public class TurnoListController implements Initializable, DataChangeListener {

	private TurnoService service;

	@FXML
	private TableView<Turno> tableViewTurno;

	@FXML
	private TableColumn<Turno, Integer> tableColumnId;

	@FXML
	private TableColumn<Turno, String> tableColumnHoraInicio;

	@FXML
	private TableColumn<Turno, String> tableColumnHoraFim;

	@FXML
	private TableColumn<Turno, Turno> tableColumnEDIT;

	@FXML
	private TableColumn<Turno, Turno> tableColumnREMOVE;

	@FXML
	private Button btNovo;

	private ObservableList<Turno> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Turno obj = new Turno();
		createDialogForm(obj, "/gui/TurnoForm.fxml", parentStage);
	}

	public void setTurnoService(TurnoService service) {
		this.service = service;
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
	}

	// padrao para iniciar o comportamento das colunas
	private void initializeNode() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
		tableColumnHoraFim.setCellValueFactory(new PropertyValueFactory<>("horaFim"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewTurno.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Turno> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewTurno.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Turno obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			TurnoFormController controller = loader.getController();
			controller.setTurno(obj);
			controller.setTurnoService(new TurnoService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados do Turno");
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
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Turno, Turno>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Turno obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/TurnoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Turno, Turno>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Turno obj, boolean empty) {
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

	private void removeEntity(Turno obj) {
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
