package it.uniroma3.diadia.giocatore;

public class Giocatore {
	
	private int cfu;
	private Borsa borsa;

	public Giocatore() {
		borsa = new Borsa();
	}
	
	public Giocatore(int numeroCfuIniziali) {
		super();
		this.cfu = numeroCfuIniziali;
	}

	public int getCfu() {
		return cfu;
	}

	public void setCfu(int cfu) {
		this.cfu = cfu;
	}
	
	public Borsa getBorsa() {
		return this.borsa;
	}
}