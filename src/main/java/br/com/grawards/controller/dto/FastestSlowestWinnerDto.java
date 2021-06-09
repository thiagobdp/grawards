package br.com.grawards.controller.dto;

import java.util.List;

public class FastestSlowestWinnerDto {

	private List<WinnerIntervalDto> min;
	private List<WinnerIntervalDto> max;

	public FastestSlowestWinnerDto(List<WinnerIntervalDto> min, List<WinnerIntervalDto> max) {
		super();
		this.min = min;
		this.max = max;
	}

	public List<WinnerIntervalDto> getMin() {
		return min;
	}

	public List<WinnerIntervalDto> getMax() {
		return max;
	}

}
