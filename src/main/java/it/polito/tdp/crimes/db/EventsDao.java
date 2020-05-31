package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getAllCategorie() {
		String sql = "SELECT DISTINCT e.offense_category_id AS c " + 
				"FROM `events` AS e " + 
				"ORDER BY e.offense_category_id ASC ";
		
		List<String> categorie = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String s = res.getString("c");
				categorie.add(s);
			}
			conn.close();
			return categorie;
			
		}catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	public List<Integer> getAllMesi() {
		String sql = "SELECT DISTINCT MONTH(e.reported_date) AS mese " + 
				"FROM `events` AS e " + 
				"ORDER BY Month(e.reported_date) ASC ";
		
		List<Integer> mesi = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Integer i = res.getInt("mese");
				mesi.add(i);
			}
			conn.close();
			return mesi;
			
		}catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	public List<String> getTipi(String categoria, Integer mese) {
		String sql = "SELECT distinct e.offense_type_id AS tipo " + 
				"FROM events AS e " + 
				"WHERE e.offense_category_id = ? " + 
				"AND Month(e.reported_date) = ? ";
		
		List<String> tipi = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, categoria);
			st.setInt(2, mese);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String t = res.getString("tipo");
				tipi.add(t);
			}
			conn.close();
			return tipi;
			
		}catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenze> getAdiacenze(String categoria, Integer mese){
		String sql = "SELECT e1.offense_type_id AS tipo1, e2.offense_type_id AS tipo2, COUNT(distinct(e1.neighborhood_id)) AS peso " + 
				"FROM events AS e1, events AS e2 " + 
				"WHERE e1.offense_category_id = ? AND e2.offense_category_id = ? " + 
				"AND Month(e1.reported_date) = ? AND MONTH(e2.reported_date) = ? " + 
				"and e1.neighborhood_id = e2.neighborhood_id " + 
				"AND e1.offense_type_id != e2.offense_type_id " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id"; 
		
		List<Adiacenze> adiacenze = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, mese);
			st.setInt(4, mese);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String t1 = res.getString("tipo1");
				String t2 = res.getString("tipo2");
				int peso = res.getInt("peso");
				Adiacenze a = new Adiacenze(t1, t2, peso);
				adiacenze.add(a);
			}
			conn.close();
			return adiacenze;
			
		}catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}


}
