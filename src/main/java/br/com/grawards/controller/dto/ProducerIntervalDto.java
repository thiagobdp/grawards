package br.com.grawards.controller.dto;

public class ProducerIntervalDto {

	private String producer;
	private Integer interval;
	private Integer previousWin;
	private Integer followingWin;
	
	public ProducerIntervalDto(String producer, Integer interval, Integer win1, Integer win2) {
		super();
		this.producer = producer;
		this.interval = interval;
		this.previousWin = win1 < win2 ? win1 : win2;
		this.followingWin = win1 > win2 ? win1 : win2;
	}

	public ProducerIntervalDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getProducer() {
		return producer;
	}

	public Integer getInterval() {
		return interval;
	}

	public Integer getPreviousWin() {
		return previousWin;
	}

	public Integer getFollowingWin() {
		return followingWin;
	}
}
