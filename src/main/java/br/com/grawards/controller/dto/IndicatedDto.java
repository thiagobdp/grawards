package br.com.grawards.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.grawards.model.Indicated;

public class IndicatedDto {

	private Integer year;
	private String title;
	private String studios;
	private String producers;
	private String winner;
	
	public IndicatedDto(Indicated indicated) {
		super();
		this.year = indicated.getYear();
		this.title = indicated.getTitle();
//		this.studios = indicated.getStudios();
//		this.producers = indicated.getProducers();
		this.winner = indicated.getWinner();
	}

	public Integer getYear() {
		return year;
	}

	public String getTitle() {
		return title;
	}

	public String getStudios() {
		return studios;
	}

	public String getProducers() {
		return producers;
	}

	public String getWinner() {
		return winner;
	}

	public static List<IndicatedDto> converter(List<Indicated> indicateds) {
		 return indicateds.stream().map(IndicatedDto::new).collect(Collectors.toList());
	}
}
