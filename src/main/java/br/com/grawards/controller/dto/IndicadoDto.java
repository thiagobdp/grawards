package br.com.grawards.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.grawards.modelo.Indicado;

public class IndicadoDto {

	private Integer year;
	private String title;
	private String studios;
	private String producers;
	private String winner;
	
	public IndicadoDto(Indicado indicado) {
		super();
		this.year = indicado.getYear();
		this.title = indicado.getTitle();
		this.studios = indicado.getStudios();
		this.producers = indicado.getProducers();
		this.winner = indicado.getWinner();
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

	public static List<IndicadoDto> converter(List<Indicado> indicados) {
		 return indicados.stream().map(IndicadoDto::new).collect(Collectors.toList());
	}
}
