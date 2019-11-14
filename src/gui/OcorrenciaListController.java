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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Equipamento;
import model.entities.Livro;
import model.entities.Ocorrencia;
import model.entities.enums.Estado;
import model.services.EquipamentoService;
import model.services.LivroService;
import model.services.OcorrenciaService;

public class OcorrenciaListController implements Initializable, DataChangeListener {

	private OcorrenciaService service;
	
	private LivroService livroService;
	
	private EquipamentoService equipamentoService;

	@FXML
	private TableView<Ocorrencia> tableViewOcorrencia;

	@FXML
	private TableColumn<Ocorrencia, Integer> tableColumnId;

	@FXML
	private TableColumn<Ocorrencia, Estado> tableColumnEstado;

	@FXML
	private TableColumn<Ocorrencia, Date> tableColumnDataHora;

	@FXML
	private TableColumn<Ocorrencia, Equipamento> tableColumnEquipamento;

	@FXML
	private TableColumn<Ocorrencia, Integer> tableColumnIdLivro;

	@FXML
	private TableColumn<Ocorrencia, Ocorrencia> tableColumnEDIT;

	@FXML
	private TableColumn<Ocorrencia, Ocorrencia> tableColumnREMOVE;

	@FXML
	private TableColumn<Ocorrencia, Ocorrencia> tableColumnDESCRICAO;
	
	@FXML
	private ComboBox<Equipamento> cbEquipamento;

	private ObservableList<Equipamento> obsListEquipamento;

	@FXML
	private DatePicker dpDataInicio;

	@FXML
	private DatePicker dpDataFim;

	@FXML
	private Button btBuscar;

	@FXML
	private Button btTodasOcorrencias;

	@FXML
	private Button btNovo;

	private ObservableList<Ocorrencia> obsList;

	@FXML
	public void onBtBuscarAction(ActionEvent event) {

		Equipamento equipamento;
		Date dataInicio;
		Date dataFim;

		if (cbEquipamento.getValue() != null) {
			equipamento = cbEquipamento.getValue();
		} else {
			equipamento = null;
		}

		if (dpDataInicio.getValue() != null && dpDataFim.getValue() != null) {
			dataInicio = Date.from(dpDataInicio.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			dataFim = Date.from(dpDataFim.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			if (dataInicio.after(dataFim)) {
				Alerts.showAlert("Erro ao buscar ocorrências", null, "Data final menor que a inicial!", AlertType.ERROR);
				throw new RuntimeException();
			}
		} else {
			dataInicio = null;
			dataFim = null;
		}
		updateTableViewBusca(equipamento, dataInicio, dataFim);
		dpDataInicio.setValue(null);
		dpDataFim.setValue(null);
		cbEquipamento.setValue(null);
	}

	@FXML
	public void onBtTodasOcorrenciasAction(ActionEvent event) {
		updateTableView(null);
	}
	
	public void updateTableViewBusca(Equipamento equipamento, Date dataInicio, Date dataFim) {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		if(equipamento== null && (dataInicio == null || dataFim == null)){
			Alerts.showAlert("Erro ao buscar por data", null, "O periódo não foi selecionado!", AlertType.ERROR);
			throw new IllegalStateException("Falha ao buscar por data!");
			
		}
		
		if(equipamento != null && dataInicio == null && dataFim == null) {
			List<Ocorrencia> list = service.findByEquipamento(equipamento);
			obsList = FXCollections.observableArrayList(list);
			tableViewOcorrencia.setItems(obsList);
		}
		
		if(equipamento == null && dataInicio != null && dataFim != null) {
			List<Ocorrencia> list = service.findByDatas(dataInicio, dataFim);
			obsList = FXCollections.observableArrayList(list);
			tableViewOcorrencia.setItems(obsList);
		}
		
		if(equipamento != null && (dataInicio != null && dataFim != null)) {
			List<Ocorrencia> listByEquipamento = service.findByEquipamento(equipamento);
			List<Ocorrencia> listByDatas = service.findByDatas(dataInicio, dataFim);
			
			List<Ocorrencia> result = new ArrayList<>();
			
			for(Ocorrencia ocorrencia : listByDatas) {
				if(listByEquipamento.contains(ocorrencia)) {
					result.add(ocorrencia);
				}
			}
			
			
			obsList = FXCollections.observableArrayList(result);
			tableViewOcorrencia.setItems(obsList);
		}

		initEditButtons();
		initRemoveButtons();
		initDescricaoButtons();
	}
	
	
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		LivroService livroService = new LivroService();
		if(livroService.confirmaLivroAberto()) {
			Stage parentStage = Utils.currentStage(event);
			Ocorrencia obj = new Ocorrencia();
			obj.setDataHora(new Date());
			createDialogForm(obj, "/gui/OcorrenciaForm.fxml", parentStage);
		}else {
			Alerts.showAlert("Erro ao cadastrar ocorrência", null, "É necessário um livro aberto para cadastrar ocorrência!", AlertType.ERROR);
		}
		
	}

	public void setOcorrenciaService(OcorrenciaService service, LivroService livroService, EquipamentoService equipamentoService) {
		this.service = service;
		this.livroService = livroService;
		this.equipamentoService = equipamentoService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();

	}

	// padrao para iniciar o comportamento das colunas
	private void initializeNode() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		tableColumnDataHora.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
		Utils.formatTableColumnDate(tableColumnDataHora, "dd/MM/yyyy - HH:mm:ss");
		tableColumnEquipamento.setCellValueFactory(new PropertyValueFactory<>("equipamento"));
		tableColumnIdLivro.setCellValueFactory(new PropertyValueFactory<>("idLivro"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOcorrencia.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView(Integer idLivro) {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Equipamento> listEquipamento = equipamentoService.findAll();
		
		obsListEquipamento = FXCollections.observableArrayList(listEquipamento);
		cbEquipamento.setItems(obsListEquipamento);
		
		if(idLivro == null) {
			List<Ocorrencia> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewOcorrencia.setItems(obsList);
		}else {
			List<Ocorrencia> list = service.findByLivro(idLivro);
			obsList = FXCollections.observableArrayList(list);
			tableViewOcorrencia.setItems(obsList);
		}
		
		
		
		initEditButtons();
		initRemoveButtons();
		initDescricaoButtons();
	}

	private void createDialogForm(Ocorrencia obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			OcorrenciaFormController controller = loader.getController();
			controller.setOcorrencia(obj);
			controller.setServices(new OcorrenciaService(), new EquipamentoService());
			controller.loadAssociateObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados da Ocorrencia");
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
		if(livroService.confirmaLivroAberto()) {
			int idLivro = livroService.findLivroAberto().getId();
			updateTableView(idLivro);
		}else {
			updateTableView(null);
		}
		
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Ocorrencia, Ocorrencia>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Ocorrencia obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}

				Livro livroAberto = livroService.findLivroAberto();
				if (livroAberto != null && obj.getIdLivro() == livroAberto.getId()) {
					setGraphic(button);
					button.setOnAction(
							event -> createDialogForm(obj, "/gui/OcorrenciaForm.fxml", Utils.currentStage(event)));
				}
			}
		});

	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Ocorrencia, Ocorrencia>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Ocorrencia obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				Livro livroAberto = livroService.findLivroAberto();
				if (livroAberto != null && obj.getIdLivro() == livroAberto.getId()) {
					setGraphic(button);
					button.setOnAction(event -> removeEntity(obj));
				}
			}
		});
	}

	private void removeEntity(Ocorrencia obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmacao", "Deseja deletar o item selecionado?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				service.remove(obj);
				if(livroService.confirmaLivroAberto()) {
					int idLivro = livroService.findLivroAberto().getId();
					updateTableView(idLivro);
				}else {
					updateTableView(null);
				}
			} catch (DbException e) {
				Alerts.showAlert("Erro ao remover", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}

	private void initDescricaoButtons() {
		tableColumnDESCRICAO.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDESCRICAO.setCellFactory(param -> new TableCell<Ocorrencia, Ocorrencia>() {
			private final Button button = new Button("descrição");

			@Override
			protected void updateItem(Ocorrencia obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}

					setGraphic(button);
					button.setOnAction(
							event -> createDialogForm(obj, "/gui/OcorrenciaFormDescricao.fxml", Utils.currentStage(event)));
				}
			
		});

	}
}
