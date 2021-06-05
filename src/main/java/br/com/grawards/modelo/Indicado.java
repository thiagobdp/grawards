package br.com.grawards.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Indicado {

	@Id @GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer year;
	private String title;
	private String studios;
	private String producers;		
	private String winner;
	
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) { 
		this.year = year;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStudios() {
		return studios;
	}
	public void setStudios(String studios) {
		this.studios = studios;
	}
	public String getProducers() {
		return producers;
	}
	public void setProducers(String producers) {
		this.producers = producers;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	
}
