package br.com.grawards.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.grawards.model.Indicated;

public class IndicatedDto {

	private Integer year;
	private String title;
	private String winner;
	private List<ProducerDto> producers;
	private List<StudioDto> studios;

	public IndicatedDto(Indicated indicated) {
		super();
		this.year = indicated.getYear();
		this.title = indicated.getTitle();
		this.winner = indicated.getWinner();
		this.producers = indicated.getProducers().stream().map(ProducerDto::new).collect(Collectors.toList());
		this.studios = indicated.getStudios().stream().map(StudioDto::new).collect(Collectors.toList());
	}

	public Integer getYear() {
		return year;
	}

	public String getTitle() {
		return title;
	}

	public List<StudioDto> getStudios() {
		return studios;
	}

	public String getWinner() {
		return winner;
	}

	public List<ProducerDto> getProducers() {
		return producers;
	}

	public static List<IndicatedDto> converter(List<Indicated> indicateds) {
		return indicateds.stream().map(IndicatedDto::new).collect(Collectors.toList());
	}
}
