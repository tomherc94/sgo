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
import model.entities.Supervisor;
import model.entities.enums.PostoGrad;
import model.exceptions.ValidationException;
import model.services.SupervisorService;

public class SupervisorFormController implements Initializable {

	private Supervisor entity;

	private SupervisorService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtIdentidade;

	@FXML
	private ComboBox<PostoGrad> cbPostoGrad;

	private ObservableList<PostoGrad> obsListPostoGrad;

	@FXML
	private TextField txtCelular;

	@FXML
	private TextField txtLogin;

	@FXML
	private TextField txtSenha;

	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorIdentidade;
	
	@FXML
	private Label labelErrorPostoGrad;
	
	@FXML
	private Label labelErrorCelular;
	
	@FXML
	private Label labelErrorLogin;
	
	@FXML
	private Label labelErrorSenha;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setSupervisor(Supervisor entity) {
		this.entity = entity;
	}

	public void setSupervisorService(SupervisorService service) {
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
		} catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}		
	}

	private Supervisor getFormData() {
		Supervisor obj = new Supervisor();

		ValidationException exception = new ValidationException("Erro de validacao!");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals(" ")) {
			exception.addError("nome", "O campo nao pode ser vazio!");
		}
		
		if(txtIdentidade.getText() == null || txtIdentidade.getText().trim().equals(" ")) {
			exception.addError("identidade", "O campo nao pode ser vazio!");
		}
		
		if(txtLogin.getText() == null || txtLogin.getText().trim().equals(" ")) {
			exception.addError("login", "O campo nao pode ser vazio!");
		}
		
		if(cbPostoGrad.getValue() == null) {
			exception.addError("postoGrad", "O campo nao pode ser vazio!");
		}
		
		if(txtCelular.getText() == null || txtCelular.getText().trim().equals(" ")) {
			exception.addError("celular", "O campo nao pode ser vazio!");
		}
		
		if(txtSenha.getText() == null || txtSenha.getText().trim().equals(" ")) {
			exception.addError("senha", "O campo nao pode ser vazio!");
		}
		
		obj.setNome(txtNome.getText());
		obj.setIdentidade(txtIdentidade.getText());
		obj.setPostoGrad(cbPostoGrad.getValue());
		obj.setCelular(txtCelular.getText());
		obj.setLogin(txtLogin.getText());
		obj.setSenha(txtSenha.getText());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

		obsListPostoGrad = FXCollections.observableArrayList(PostoGrad.values());
		cbPostoGrad.setItems(obsListPostoGrad);
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 45);
		Constraints.setTextFieldMaxLength(txtIdentidade, 13);
		Constraints.setTextFieldMaxLength(txtCelular, 16);
		Constraints.setTextFieldMaxLength(txtLogin, 20);
		Constraints.setTextFieldMaxLength(txtSenha, 15);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtIdentidade.setText(entity.getIdentidade());
		cbPostoGrad.setValue(entity.getPostoGrad());
		txtCelular.setText(entity.getCelular());
		txtLogin.setText(entity.getLogin());
		txtSenha.setText("");
	}
	
	private void setErrorMessages(Map<String,String> errors) {
		Set<String> fields = errors.keySet();
		
		
		labelErrorName.setText(fields.contains("nome") ? errors.get("nome") : "");
		
		labelErrorIdentidade.setText(fields.contains("identidade") ? errors.get("identidade") : "");
		
		labelErrorPostoGrad.setText(fields.contains("postoGrad") ? errors.get("postoGrad") : "");
		
		labelErrorCelular.setText(fields.contains("celular") ? errors.get("celular") : "");
		
		labelErrorLogin.setText(fields.contains("login") ? errors.get("login") : "");
		
		labelErrorSenha.setText(fields.contains("senha") ? errors.get("senha") : "");
		
	}
}
