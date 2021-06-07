package br.com.grawards.model;

public class IndicatedCsv {
	
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
	@Override
	public String toString() {
		return "Title: ".concat(this.title)
				.concat(", Year:").concat(this.year.toString())
				.concat(", Studios: ").concat(this.studios)
				.concat(", Producers: ").concat(this.producers)
				.concat(", Winner: ").concat(this.winner);		
	}
	
}
