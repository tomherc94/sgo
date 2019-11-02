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
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Livro;
import model.entities.Supervisor;
import model.entities.Turno;
import model.entities.enums.StatusLivro;
import model.exceptions.ValidationException;
import model.services.LivroService;
import model.services.SupervisorService;
import model.services.TurnoService;

public class LivroFormController implements Initializable {

	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	private Livro entity;

	private LivroService service;

	private SupervisorService supervisorService;

	private TurnoService turnoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDataHoraAbertura;

	@FXML
	private TextField txtDataHoraFechamento;

	@FXML
	private TextField txtStatus;

	@FXML
	private ComboBox<Supervisor> cbSupervisor;

	@FXML
	private ComboBox<Turno> cbTurno;
	
	@FXML
	private Label labelErrorSupervisor;
	
	@FXML
	private Label labelErrorTurno;

	private ObservableList<Supervisor> obsListSupervisor;

	private ObservableList<Turno> obsListTurno;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void loadAssociateObjects() {
		if (supervisorService == null) {
			throw new IllegalStateException("SupervisorService was null!");
		}
		List<Supervisor> listSupervisores = supervisorService.findAll();
		List<Turno> listTurnos = turnoService.findAll();

		obsListSupervisor = FXCollections.observableArrayList(listSupervisores);
		cbSupervisor.setItems(obsListSupervisor);

		obsListTurno = FXCollections.observableArrayList(listTurnos);
		cbTurno.setItems(obsListTurno);
	}

	public void setLivro(Livro entity) {
		this.entity = entity;
	}

	public void setServices(LivroService service, SupervisorService supervisorService, TurnoService turnoService) {
		this.service = service;
		this.supervisorService = supervisorService;
		this.turnoService = turnoService;
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

	private Livro getFormData() throws ParseException {
		Livro obj = new Livro();

		ValidationException exception = new ValidationException("Erro de validacao!");
		
		if (cbSupervisor.getValue() == null) {
			exception.addError("supervisor", "O campo nao pode ser vazio!");
		}
		
		if (cbTurno.getValue() == null) {
			exception.addError("turno", "O campo nao pode ser vazio!");
		}
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setDataHoraAbertura(sdf.parse(txtDataHoraAbertura.getText()));

		// testa se a data/hora de fechamento existe
		if (entity.getDataHoraFechamento() == null) {
			obj.setDataHoraFechamento(null);
		} else {
			obj.setDataHoraFechamento(sdf.parse(txtDataHoraFechamento.getText()));
		}

		obj.setStatus(StatusLivro.valueOf(txtStatus.getText()));
		obj.setSupervisor(cbSupervisor.getValue());
		obj.setTurno(cbTurno.getValue());
		
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

		initializeComboBoxSupervisor();
		initializeComboBoxTurno();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtDataHoraAbertura.setText(sdf.format(entity.getDataHoraAbertura()));

		// testa se a data/hora de fechamento existe
		if (entity.getDataHoraFechamento() == null) {
			txtDataHoraFechamento.setText(null);
		} else {
			txtDataHoraFechamento.setText(sdf.format(entity.getDataHoraFechamento()));
		}

		txtStatus.setText(String.valueOf(entity.getStatus()));

		if (entity.getSupervisor() == null) {
			cbSupervisor.getSelectionModel().clearSelection();
		} else {
			cbSupervisor.setValue(entity.getSupervisor());
		}

		cbSupervisor.setItems(obsListSupervisor);
		cbTurno.setItems(obsListTurno);
	}

	private void initializeComboBoxSupervisor() {
		Callback<ListView<Supervisor>, ListCell<Supervisor>> factory = lv -> new ListCell<Supervisor>() {
			@Override
			protected void updateItem(Supervisor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		cbSupervisor.setCellFactory(factory);
		cbSupervisor.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxTurno() {
		Callback<ListView<Turno>, ListCell<Turno>> factory = lv -> new ListCell<Turno>() {
			@Override
			protected void updateItem(Turno item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.toString());
			}
		};
		cbTurno.setCellFactory(factory);
		cbTurno.setButtonCell(factory.call(null));
	}
	
	private void setErrorMessages(Map<String,String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorSupervisor.setText(fields.contains("supervisor") ? errors.get("supervisor") : "");
		
		labelErrorTurno.setText(fields.contains("turno") ? errors.get("turno") : "");
	}

}
