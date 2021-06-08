package br.com.grawards.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Producer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	@ManyToMany (mappedBy = "producers")
	private List<Indicated> indicateds; 

	public Producer() {
		super();
	}

	public Producer(String name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Indicated> getIndicateds() {
		return indicateds;
	}

	public void setIndicateds(List<Indicated> indicateds) {
		this.indicateds = indicateds;
	}

	@Override
	public String toString() {
		
		return this.name+this.getIndicateds().stream().map(i -> i.toString()).collect(Collectors.toList())+"\n";
	}
	
}
