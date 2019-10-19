package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemSupervisor;
	
	@FXML
	private MenuItem menuItemEquipamento;
	
	@FXML
	private MenuItem menuItemTurno;
	
	@FXML
	private MenuItem menuItemLivro;
	
	@FXML
	private MenuItem menuItemHistorico;
	
	@FXML
	private MenuItem menuItemSobre;
	
	
	@FXML
	public void onMenuItemSupervisorAction() {
		System.out.println("onMenuItemSupervisorAction");
	}
	
	@FXML
	public void onMenuItemEquipamentoAction() {
		System.out.println("onMenuItemEquipamentoAction");
	}
	
	@FXML
	public void onMenuItemTurnoAction() {
		System.out.println("onMenuItemTurnoAction");
	}
	
	@FXML
	public void onMenuItemLivroAction() {
		System.out.println("onMenuItemLivroAction");
	}
	
	@FXML
	public void onMenuItemHistoricoAction() {
		System.out.println("onMenuItemHistoricoAction");
	}
	
	@FXML
	public void onMenuItemSobreAction() {
		System.out.println("onMenuItemSobreAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}

}
