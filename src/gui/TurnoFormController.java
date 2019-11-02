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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Turno;
import model.exceptions.ValidationException;
import model.services.TurnoService;

public class TurnoFormController implements Initializable{

	private Turno entity;

	private TurnoService service;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtHoraInicio;
	
	@FXML
	private TextField txtHoraFim;

	@FXML
	private Label labelErrorHoraInicio;
	
	@FXML
	private Label labelErrorHoraFim;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	public void setTurno(Turno entity) {
		this.entity = entity;
	}

	public void setTurnoService(TurnoService service) {
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

	private Turno getFormData() {
		Turno obj = new Turno();

		ValidationException exception = new ValidationException("Erro de validacao!");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtHoraInicio.getText() == null || txtHoraInicio.getText().trim().equals(" ")) {
			exception.addError("horaInicio", "O campo nao pode ser vazio!");
		}
		
		if (txtHoraFim.getText() == null || txtHoraFim.getText().trim().equals(" ")) {
			exception.addError("horaFim", "O campo nao pode ser vazio!");
		}

		obj.setHoraInicio(txtHoraInicio.getText());
		obj.setHoraFim(txtHoraFim.getText());

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
		Constraints.setTextFieldMaxLength(txtHoraInicio, 8);
		Constraints.setTextFieldMaxLength(txtHoraFim, 8);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtHoraInicio.setText(entity.getHoraInicio());
		txtHoraFim.setText(entity.getHoraFim());
	}
	
	private void setErrorMessages(Map<String,String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorHoraInicio.setText(fields.contains("horaInicio") ? errors.get("horaInicio") : "");
		
		labelErrorHoraFim.setText(fields.contains("horaFim") ? errors.get("horaFim") : "");
	}
}
