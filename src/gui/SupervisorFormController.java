package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
import model.services.SupervisorService;

public class SupervisorFormController implements Initializable {

	private Supervisor entity;

	private SupervisorService service;

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
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setSupervisor(Supervisor entity) {
		this.entity = entity;
	}

	public void setSupervisorService(SupervisorService service) {
		this.service = service;
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
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private Supervisor getFormData() {
		Supervisor obj = new Supervisor();

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		obj.setIdentidade(txtIdentidade.getText());
		obj.setPostoGrad(cbPostoGrad.getValue());
		obj.setCelular(txtCelular.getText());
		obj.setLogin(txtLogin.getText());
		obj.setSenha(txtSenha.getText());

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
		cbPostoGrad.setItems(obsListPostoGrad);
		txtCelular.setText(entity.getCelular());
		txtLogin.setText(entity.getLogin());
		txtSenha.setText("");
	}
}
