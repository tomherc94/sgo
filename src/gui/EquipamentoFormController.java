package gui;

import java.net.URL;
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
import javafx.scene.control.TextField;
import model.entities.Equipamento;
import model.entities.enums.Estado;
import model.entities.enums.Tipo;
import model.exceptions.ValidationException;
import model.services.EquipamentoService;

public class EquipamentoFormController implements Initializable {

	private Equipamento entity;

	private EquipamentoService service;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private ComboBox<Tipo> cbTipo;

	@FXML
	private ComboBox<Estado> cbEstadoAtual;

	private ObservableList<Tipo> obsListTipo;

	private ObservableList<Estado> obsListEstado;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

		obsListTipo = FXCollections.observableArrayList(Tipo.values());
		cbTipo.setItems(obsListTipo);
		
		obsListEstado = FXCollections.observableArrayList(Estado.values());
		cbEstadoAtual.setItems(obsListEstado);

	}

	public void setEquipamento(Equipamento entity) {
		this.entity = entity;
	}

	public void setEquipamentoService(EquipamentoService service) {
		this.service = service;
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
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Equipamento getFormData() {
		Equipamento obj = new Equipamento();

		ValidationException exception = new ValidationException("Erro de validacao!");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals(" ")) {
			exception.addError("nome", "O campo nao pode ser vazio!");
		}

		obj.setNome(txtNome.getText());
		obj.setTipo(cbTipo.getValue());
		obj.setEstadoAtual(cbEstadoAtual.getValue());

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
		Constraints.setTextFieldMaxLength(txtNome, 45);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		cbTipo.setValue(entity.getTipo());
		cbEstadoAtual.setValue(entity.getEstadoAtual());
	}
	
	private void setErrorMessages(Map<String,String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText(fields.contains("nome") ? errors.get("nome") : "");
	}

}
