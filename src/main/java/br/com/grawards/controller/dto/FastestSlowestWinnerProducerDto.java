package br.com.grawards.controller.dto;

import java.util.List;

public class FastestSlowestWinnerProducerDto {

	private List<ProducerIntervalDto> min;
	private List<ProducerIntervalDto> max;

	public FastestSlowestWinnerProducerDto(List<ProducerIntervalDto> min, List<ProducerIntervalDto> max) {
		super();
		this.min = min;
		this.max = max;
	}

	public List<ProducerIntervalDto> getMin() {
		return min;
	}

	public List<ProducerIntervalDto> getMax() {
		return max;
	}

}
