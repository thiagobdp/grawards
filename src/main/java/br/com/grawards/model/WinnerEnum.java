package br.com.grawards.model;

public enum WinnerEnum {
	YES("YES");

	private String winner = new String();

	WinnerEnum(String winner) {
		this.winner = winner;
	}

	public String getWinner() {
		return this.winner;
	}
}
