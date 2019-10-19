package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Supervisor;
import model.entities.enums.PostoGrad;

public class SupervisorFormController implements Initializable{

	private Supervisor entity;
	
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
	
	@FXML
	public void onBtSalvarAction() {
		System.out.println("onBtSalvarAction");
	}
	
	@FXML
	public void onBtCancelarAction() {
		System.out.println("onBtCancelarAction");
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
		if(entity == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtIdentidade.setText(entity.getIdentidade());
		//cbPostoGrad
		txtCelular.setText(entity.getCelular());
		txtLogin.setText(entity.getLogin());
		txtSenha.setText("");
	}
}
