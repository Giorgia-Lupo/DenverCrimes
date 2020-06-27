package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.Adiacenze;
import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	EventsDao dao;
	private List<Event> eventi;
	List<String> tipi;
	private Event e;
	private Graph<String, DefaultWeightedEdge> grafo ; 
	
	private List<String> soluzione;
	
	public Model() {
		dao = new EventsDao();
		this.eventi = new ArrayList<>(dao.listAllEvents());
	}
	
	public void creaGrafo(String categoria, Integer mese) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		tipi = this.dao.getTipi(categoria, mese);
		
		//Aggiungere i vertici
		Graphs.addAllVertices(this.grafo, this.tipi);
		
		//Aggiungere gli archi
		for(Adiacenze a : dao.getAdiacenze(categoria, mese)) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, a.getT1(), a.getT2(), a.getPeso());
			}
		}
		System.out.println(this.grafo) ;
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenze> getArchi(String categoria, Integer mese) {
		double pesoMedio = 0;
		for(DefaultWeightedEdge de : this.grafo.edgeSet()) {
				pesoMedio += this.grafo.getEdgeWeight(de);		
		}		
		
		pesoMedio = pesoMedio/this.grafo.edgeSet().size();
		
		List<Adiacenze> archi = new ArrayList<>();
		 for(DefaultWeightedEdge de : this.grafo.edgeSet()) {
			 if(this.grafo.getEdgeWeight(de) > pesoMedio) {
				 archi.add(new Adiacenze(this.grafo.getEdgeSource(de), 
						 this.grafo.getEdgeTarget(de),
						 this.grafo.getEdgeWeight(de)));
			 }
		 }
		 return archi;
	}

	public List<String> getAllCategorie(){
		return this.dao.getAllCategorie();
	}
	
	public List<Integer> getAllMesi(){
		return this.dao.getAllMesi();
	}
	
	public List<String> trovaPercorso(String sorgente, String destinazione){
		this.soluzione = new ArrayList<>();
		List<String> parziale = new ArrayList<>();
		parziale.add(sorgente);
		this.ricorsione(destinazione, parziale, 0);	
		return soluzione;
	}
	
	private void ricorsione(String destinazione, List<String> parziale, int livello) {
		//caso terminale --> se sono già a destinazione, questa è migliore di soluzione?
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(parziale.size() > this.soluzione.size())
				this.soluzione = new ArrayList<>(parziale);
		}
		
		for(String vicino : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))){
			//aciclico = non ho già preso il vertice
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				ricorsione(destinazione, parziale, livello+1);
				parziale.remove(vicino);
			}
			
		}
		
	}
}
