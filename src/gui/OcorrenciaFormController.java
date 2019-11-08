package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Equipamento;
import model.entities.Ocorrencia;
import model.entities.enums.Estado;
import model.exceptions.ValidationException;
import model.services.EquipamentoService;
import model.services.OcorrenciaService;

public class OcorrenciaFormController implements Initializable {

	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	private Ocorrencia entity;

	private OcorrenciaService service;

	private EquipamentoService EquipamentoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDataHora;

	@FXML
	private ComboBox<Estado> cbEstado;

	@FXML
	private ComboBox<Equipamento> cbEquipamento;

	@FXML
	private TextArea txtDescricao;
	
	@FXML
	private Label labelErrorEstado;
	
	@FXML
	private Label labelErrorEquipamento;
	
	@FXML
	private Label labelErrorDescricao;

	private ObservableList<Equipamento> obsListEquipamento;

	private ObservableList<Estado> obsListEstado;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void loadAssociateObjects() {
		if (EquipamentoService == null) {
			throw new IllegalStateException("EquipamentoService was null!");
		}
		List<Equipamento> listEquipamentos = EquipamentoService.findAll();

		obsListEquipamento = FXCollections.observableArrayList(listEquipamentos);
		cbEquipamento.setItems(obsListEquipamento);
		
		obsListEstado = FXCollections.observableArrayList(Estado.values());
		cbEstado.setItems(obsListEstado);

	}

	public void setOcorrencia(Ocorrencia entity) {
		this.entity = entity;
	}

	public void setServices(OcorrenciaService service, EquipamentoService EquipamentoService) {
		this.service = service;
		this.EquipamentoService = EquipamentoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo!");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		} catch (ParseException e) {
			Alerts.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Ocorrencia getFormData() throws ParseException {
		Ocorrencia obj = new Ocorrencia();

		ValidationException exception = new ValidationException("Erro de validacao!");
		
		if (cbEquipamento.getValue() == null) {
			exception.addError("equipamento", "O campo nao pode ser vazio!");
		}
		
		if (cbEstado.getValue() == null) {
			exception.addError("estado", "O campo nao pode ser vazio!");
		}
		
		if (txtDescricao.getText() == null) {
			exception.addError("descricao", "O campo nao pode ser vazio!");
			obj.setDescricao(null);
		}else {
			obj.setDescricao(new StringBuilder(txtDescricao.getText()));
		}
		
		
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setDataHora(sdf.parse(txtDataHora.getText()));
		obj.setEstado(cbEstado.getValue());
		obj.setEquipamento(cbEquipamento.getValue());
		//obj.setDescricao(new StringBuilder(txtDescricao.getText()));
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);

		initializeComboBoxEquipamento();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtDataHora.setText(sdf.format(entity.getDataHora()));
		
		if(entity.getDescricao() == null) {
			txtDescricao.setText(null);
		}else {
			txtDescricao.setText(entity.getDescricao().toString());
		}
		
		
		
		if (entity.getEquipamento() == null) {
			cbEquipamento.getSelectionModel().clearSelection();
		} else {
			cbEquipamento.setValue(entity.getEquipamento());
		}
		
		if (entity.getEstado() == null) {
			cbEstado.getSelectionModel().clearSelection();
		} else {
			cbEstado.setValue(entity.getEstado());
		}
		
		

		cbEquipamento.setItems(obsListEquipamento);
		cbEstado.setItems(obsListEstado);
	}

	private void initializeComboBoxEquipamento() {
		Callback<ListView<Equipamento>, ListCell<Equipamento>> factory = lv -> new ListCell<Equipamento>() {
			@Override
			protected void updateItem(Equipamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		cbEquipamento.setCellFactory(factory);
		cbEquipamento.setButtonCell(factory.call(null));
	}

	
	private void setErrorMessages(Map<String,String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorEquipamento.setText(fields.contains("equipamento") ? errors.get("equipamento") : "");
		
		labelErrorEstado.setText(fields.contains("estado") ? errors.get("estado") : "");
		
		labelErrorDescricao.setText(fields.contains("descricao") ? errors.get("descricao") : "");
		
	}

}
