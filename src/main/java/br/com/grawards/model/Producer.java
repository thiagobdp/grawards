package br.com.grawards.model;

import java.util.List;

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

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
}
