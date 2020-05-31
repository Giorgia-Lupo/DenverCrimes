package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	EventsDao dao;
	private List<Event> eventi;
	
	public Model() {
		dao = new EventsDao();
		this.eventi = new ArrayList<>(dao.listAllEvents());
	}
	

	public List<String> getAllCategorie(){
		return this.dao.getAllCategorie();
	}
	
	public List<Integer> getAllMesi(){
		return this.dao.getAllMesi();
	}
	
}
