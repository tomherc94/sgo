package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Supervisor;
import model.entities.enums.PostoGrad;
import model.services.SupervisorService;

public class SupervisorListController implements Initializable{
	
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
	private Button btNovo;
	
	private ObservableList<Supervisor> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Supervisor obj = new Supervisor();
		createDialogForm(obj, "/gui/SupervisorForm.fxml", parentStage);
	}

	public void setSupervisorService (SupervisorService service) {
		this.service = service;
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
		
	}

	//padrao para iniciar o comportamento das colunas
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
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Supervisor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSupervisor.setItems(obsList);
	}
	
	private void createDialogForm(Supervisor obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			SupervisorFormController controller = loader.getController();
			controller.setSupervisor(obj);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados do Supervisor");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage); //init pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL); //impede o acesso da janela anterior enquanto a mesma nao for fechada
			dialogStage.showAndWait();			
			
		}catch(IOException e){
			Alerts.showAlert("IO Exception", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}
}
