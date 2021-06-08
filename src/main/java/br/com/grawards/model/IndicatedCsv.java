package br.com.grawards.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndicatedCsv {

	private Integer year;
	private String title;
	private String studios;
	private List<String> listStudios;
	private String producers;
	private List<String> listProducers;
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
		this.listStudios = this.splitStrings(studios);
	}

	public String getProducers() {
		return producers;
	}

	public void setProducers(String producers) {
		this.producers = producers;
		this.listProducers = this.splitStrings(producers);
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public List<String> getListStudios() {
		return listStudios;
	}

	public List<String> getListProducers() {
		return listProducers;
	}

	@Override
	public String toString() {
		return "Title: ".concat(this.title).concat(", Studios: ").concat(this.listStudios.toString())
				.concat(", Producers: ").concat(this.listProducers.toString()).concat(", Year:")
				.concat(this.year.toString()).concat(", Winner: ").concat(this.winner);
	}

	/*
	 * Return the original string as a list splitted by: ", " " and " at the end,
	 * stripes the values
	 */
	private List<String> splitStrings(String original) {
		List<String> splitComma = Arrays.asList(original.split(", "));

		List<String> splitAnd = new ArrayList<String>();
		splitComma.forEach(s -> splitAnd.addAll(Arrays.asList(s.split(" and "))));

		List<String> stripedString = new ArrayList<String>();
		splitAnd.forEach(s -> stripedString.add(s.strip()));

		return stripedString;
	}

}
