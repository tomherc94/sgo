package gui;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Livro;
import model.entities.Supervisor;
import model.entities.Turno;
import model.entities.enums.StatusLivro;
import model.services.EquipamentoService;
import model.services.LivroService;
import model.services.OcorrenciaService;
import model.services.SupervisorService;
import model.services.TurnoService;

public class LivroListController implements Initializable, DataChangeListener {

	private LivroService service;

	private SupervisorService supervisorService;

	@FXML
	private TableView<Livro> tableViewLivro;

	@FXML
	private TableColumn<Livro, Integer> tableColumnId;

	@FXML
	private TableColumn<Livro, Date> tableColumnDataHoraAbertura;

	@FXML
	private TableColumn<Livro, Date> tableColumnDataHoraFechamento;

	@FXML
	private TableColumn<Livro, StatusLivro> tableColumnStatus;

	@FXML
	private TableColumn<Livro, Supervisor> tableColumnSupervisor;

	@FXML
	private TableColumn<Livro, Turno> tableColumnTurno;

	@FXML
	private TableColumn<Livro, Livro> tableColumnEDIT;

	@FXML
	private TableColumn<Livro, Livro> tableColumnREMOVE;

	@FXML
	private TableColumn<Livro, Livro> tableColumnFECHARLIVRO;

	@FXML
	private TableColumn<Livro, Livro> tableColumnOCORRENCIAS;

	@FXML
	private ComboBox<Supervisor> cbSupervisor;

	private ObservableList<Supervisor> obsListSupervisor;

	@FXML
	private DatePicker dpDataInicio;

	@FXML
	private DatePicker dpDataFim;

	@FXML
	private Button btBuscar;

	@FXML
	private Button btTodosLivros;

	@FXML
	private Button btNovo;

	private ObservableList<Livro> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		boolean livroAberto = service.confirmaLivroAberto();

		if (livroAberto == false) {
			Livro obj = new Livro();
			obj.setDataHoraAbertura(new Date());
			obj.setStatus(StatusLivro.ABERTO);
			createDialogForm(obj, "/gui/LivroForm.fxml", parentStage);
		} else {
			Alerts.showAlert("Erro ao cadastrar livro", null, "Ja existem um livro com status ABERTO!",
					AlertType.ERROR);
		}

	}

	@FXML
	public void onBtBuscarAction(ActionEvent event) {

		Supervisor supervisor;
		Date dataInicio;
		Date dataFim;

		if (cbSupervisor.getValue() != null) {
			supervisor = cbSupervisor.getValue();
		} else {
			supervisor = null;
		}

		if (dpDataInicio.getValue() != null && dpDataFim.getValue() != null) {
			dataInicio = Date.from(dpDataInicio.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			dataFim = Date.from(dpDataFim.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			if (dataInicio.after(dataFim)) {
				Alerts.showAlert("Erro ao buscar livros", null, "Data final menor que a inicial!", AlertType.ERROR);
				throw new RuntimeException();
			}
		} else {
			dataInicio = null;
			dataFim = null;
		}
		updateTableViewBusca(supervisor, dataInicio, dataFim);
		dpDataInicio.setValue(null);
		dpDataFim.setValue(null);
		cbSupervisor.setValue(null);
	}

	@FXML
	public void onBtTodosLivrosAction(ActionEvent event) {
		updateTableView();
	}

	public void setServices(LivroService service, SupervisorService supervisorService) {
		this.service = service;
		this.supervisorService = supervisorService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
	}

	// padrao para iniciar o comportamento das colunas
	private void initializeNode() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDataHoraAbertura.setCellValueFactory(new PropertyValueFactory<>("dataHoraAbertura"));
		Utils.formatTableColumnDate(tableColumnDataHoraAbertura, "dd/MM/yyyy - HH:mm:ss");
		tableColumnDataHoraFechamento.setCellValueFactory(new PropertyValueFactory<>("dataHoraFechamento"));
		Utils.formatTableColumnDate(tableColumnDataHoraFechamento, "dd/MM/yyyy - HH:mm:ss");
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		tableColumnSupervisor.setCellValueFactory(new PropertyValueFactory<>("supervisor"));
		tableColumnTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewLivro.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Livro> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewLivro.setItems(obsList);

		List<Supervisor> listSupervisores = supervisorService.findAll();

		obsListSupervisor = FXCollections.observableArrayList(listSupervisores);
		cbSupervisor.setItems(obsListSupervisor);

		initEditButtons();
		initRemoveButtons();
		initFecharLivroButtons();
		initOcorrenciasButtons();
	}

	public void updateTableViewBusca(Supervisor supervisor, Date dataInicio, Date dataFim) {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		if (supervisor == null && (dataInicio == null || dataFim == null)) {
			Alerts.showAlert("Erro ao buscar por data", null, "O periódo não foi selecionado!", AlertType.ERROR);
			throw new IllegalStateException("Falha ao buscar por data!");

		}

		if (supervisor != null && dataInicio == null && dataFim == null) {
			List<Livro> list = service.findBySupervisor(supervisor);
			obsList = FXCollections.observableArrayList(list);
			tableViewLivro.setItems(obsList);
		}

		if (supervisor == null && dataInicio != null && dataFim != null) {
			List<Livro> list = service.findByDatas(dataInicio, dataFim);
			obsList = FXCollections.observableArrayList(list);
			tableViewLivro.setItems(obsList);
		}

		if (supervisor != null && (dataInicio != null && dataFim != null)) {
			List<Livro> listBySupervisor = service.findBySupervisor(supervisor);
			List<Livro> listByDatas = service.findByDatas(dataInicio, dataFim);

			List<Livro> result = new ArrayList<>();

			for (Livro livro : listBySupervisor) {
				if (listByDatas.contains(livro)) {
					result.add(livro);
				}
			}

			obsList = FXCollections.observableArrayList(result);
			tableViewLivro.setItems(obsList);
		}

		initEditButtons();
		initRemoveButtons();
		initFecharLivroButtons();
		initOcorrenciasButtons();
	}

	private void createDialogForm(Livro obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			LivroFormController controller = loader.getController();
			controller.setLivro(obj);
			controller.setServices(new LivroService(), new SupervisorService(), new TurnoService());
			controller.loadAssociateObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados do Livro");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Livro, Livro>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Livro obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				if (obj.getStatus() == StatusLivro.ABERTO) {
					setGraphic(button);
					button.setOnAction(
							event -> createDialogForm(obj, "/gui/LivroForm.fxml", Utils.currentStage(event)));
				}
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Livro, Livro>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Livro obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				if (obj.getStatus() == StatusLivro.ABERTO) {
					setGraphic(button);
					button.setOnAction(event -> removeEntity());
				}
			}
		});
	}

	private void removeEntity() {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmacao", "Deseja deletar o item selecionado?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				service.remove();
				;
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}

	private void initFecharLivroButtons() {
		tableColumnFECHARLIVRO.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnFECHARLIVRO.setCellFactory(param -> new TableCell<Livro, Livro>() {
			private final Button button = new Button("fechar");

			@Override
			protected void updateItem(Livro obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				if (obj.getStatus() == StatusLivro.ABERTO) {
					setGraphic(button);
					button.setOnAction(event -> fecharEntity());
				}
			}
		});
	}

	private void fecharEntity() {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmacao", "Deseja fechar o livro selecionado?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				service.fecharLivro();
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}

	private void initOcorrenciasButtons() {
		tableColumnOCORRENCIAS.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnOCORRENCIAS.setCellFactory(param -> new TableCell<Livro, Livro>() {
			private final Button button = new Button("ocorrências");

			@Override
			protected void updateItem(Livro obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				MainViewController mainViewController = new MainViewController();
				setGraphic(button);
				button.setOnAction(event -> mainViewController.loadView("/gui/OcorrenciaList.fxml",
						(OcorrenciaListController controller) -> {
							controller.setOcorrenciaService(new OcorrenciaService(), new LivroService(), new EquipamentoService());
							controller.updateTableView(obj.getId());
						}));
			}
		});
	}
}
