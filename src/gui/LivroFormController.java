package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.entities.Livro;
import model.entities.Supervisor;
import model.entities.Turno;
import model.entities.enums.StatusLivro;
import model.services.LivroService;

public class LivroFormController implements Initializable {

	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	private Livro entity;

	private LivroService service;

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

	private ObservableList<Supervisor> obsListSupervisor;

	private ObservableList<Turno> obsListTurno;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

		/*obsListTipo = FXCollections.observableArrayList(Tipo.values());
		cbTipo.setItems(obsListTipo);

		obsListEstado = FXCollections.observableArrayList(Estado.values());
		cbEstadoAtual.setItems(obsListEstado);*/

	}

	public void setLivro(Livro entity) {
		this.entity = entity;
	}

	public void setLivroService(LivroService service) {
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
		} catch (ParseException e) {
			Alerts.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Livro getFormData() throws ParseException {
		Livro obj = new Livro();

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setDataHoraAbertura(sdf.parse(txtDataHoraAbertura.getText()));
		obj.setDataHoraFechamento(sdf.parse(txtDataHoraFechamento.getText()));
		obj.setStatus(StatusLivro.valueOf(txtStatus.getText()));
		obj.setSupervisor(cbSupervisor.getValue());
		obj.setTurno(cbTurno.getValue());

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtDataHoraAbertura.setText(sdf.format(entity.getDataHoraAbertura()));
		
		//testa se a data/hora de fechamento existe
		if (entity.getDataHoraFechamento() == null) {
			txtDataHoraFechamento.setText("<Livro aberto>");
		} else {
			txtDataHoraFechamento.setText(sdf.format(entity.getDataHoraFechamento()));
		}
		
		txtStatus.setText(String.valueOf(entity.getStatus()));
		cbSupervisor.setItems(obsListSupervisor);
		cbTurno.setItems(obsListTurno);
	}

}
