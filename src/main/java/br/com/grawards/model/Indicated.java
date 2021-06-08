package br.com.grawards.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Indicated {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer year;
	private String title;
	private String winner;

	@ManyToMany
	@JoinTable(name = "indicated_studios")
	private List<Studio> studios;

	@ManyToMany
	@JoinTable(name = "indicated_producer")
	private List<Producer> producers;

	public Indicated(IndicatedCsv indicatedCsv, HashMap<String, Studio> studioHmp,
			HashMap<String, Producer> producerHmp) {
		super();
		this.year = indicatedCsv.getYear();
		this.title = indicatedCsv.getTitle();
		this.winner = indicatedCsv.getWinner();

		this.studios = new ArrayList<Studio>();
		indicatedCsv.getListStudios().forEach(s -> this.studios.add(studioHmp.get(s)));

		this.producers = new ArrayList<Producer>();
		indicatedCsv.getListProducers().forEach(p -> this.producers.add(producerHmp.get(p)));
	}

	public Indicated() {
		super();
	}

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

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Studio> getStudios() {
		return studios;
	}

	public void setStudios(List<Studio> studios) {
		this.studios = studios;
	}

	public List<Producer> getProducers() {
		return producers;
	}

	public void setProducers(List<Producer> producers) {
		this.producers = producers;
	}

	@Override
	public String toString() {
		return "Winner: "+this.winner+", Title: "+this.title+", year: "+this.year;
	}

}
