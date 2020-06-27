/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.db.Adiacenze;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenze> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    
    	Adiacenze a = this.boxArco.getValue();
    	String sorgente = a.getT1();
    	String destinazione = a.getT2();
    	
    	List<String> percorso = this.model.trovaPercorso(sorgente, destinazione);
    	for(String s : percorso)
    		txtResult.appendText(s+"\n");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String categoria = this.boxCategoria.getValue();
    	Integer mese = this.boxMese.getValue();
    	
    	if(categoria==null) {
    		this.txtResult.appendText("Devi selezionare una categoria!\n");
    	}
    	if(mese==null) {
    		this.txtResult.appendText("Devi selezionare un mese!\n");
    	}
    	
    	this.model.creaGrafo(categoria, mese);
    	txtResult.appendText(String.format("Grafo creato! %d vertici, %d archi\n",this.model.nVertici(), this.model.nArchi()));
    	
    	this.boxArco.getItems().addAll(this.model.getArchi(categoria, mese));
    	txtResult.appendText("L'elenco degli archi con peso maggiore del peso medio del grafo e': \n");
    	for(Adiacenze a : this.model.getArchi(categoria, mese)) {
    		txtResult.appendText(""+a.toString()+"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(model.getAllCategorie());
    	this.boxMese.getItems().addAll(this.model.getAllMesi());
    	
    }
}
