package it.uniroma3.diadia.giocatore;

import it.uniroma3.diadia.attrezzi.Attrezzo;

public class Borsa {
	public final static int DEFAULT_PESO_MAX_BORSA = 10;

	private Attrezzo[] attrezzi;
	private int numeroAttrezzi;
	private int pesoMax;

	public Borsa() {
		this(DEFAULT_PESO_MAX_BORSA);
	}

	public Borsa(int pesoMax) {
		this.pesoMax = pesoMax;
		this.attrezzi = new Attrezzo[10]; // speriamo bastino...
		this.numeroAttrezzi = 0;
	}

	public boolean addAttrezzo(Attrezzo attrezzo) {
		if (this.getPeso() + attrezzo.getPeso() > this.getPesoMax())
			return false;
		if (this.numeroAttrezzi==10)
			return false;
		this.attrezzi[this.numeroAttrezzi] = attrezzo;
		this.numeroAttrezzi++;
		return true;
	}

	public int getPesoMax() {
		return pesoMax;
	}

	public Attrezzo[] getAttrezzi() {
		return this.attrezzi;
	}

	public Attrezzo getAttrezzo(String nomeAttrezzo) {
		Attrezzo a = null;
		for (int i= 0; i<this.numeroAttrezzi; i++)
			if (this.attrezzi[i].getNome().equals(nomeAttrezzo))
				a = attrezzi[i];

		return a;
	}

	public int getPeso() {
		int peso = 0;
		for (int i= 0; i<this.numeroAttrezzi; i++)
			if (this.attrezzi[i] != null)
				peso += this.attrezzi[i].getPeso();

		return peso;
	}

	public boolean isEmpty() {
		return this.numeroAttrezzi == 0;
	}

	public boolean hasAttrezzo(String nomeAttrezzo) {
		return this.getAttrezzo(nomeAttrezzo)!=null;
	}

	public Attrezzo removeAttrezzo(String nomeAttrezzo) {
		Attrezzo a = null;

		// Check che ci siano attrezzi in borsa
		if (!this.isEmpty()) {
			
			//Check che ci sia l'attrezzo ricercato
			if (this.hasAttrezzo(nomeAttrezzo) == true) {
				int numeroAttrezzi = this.attrezzi.length;
				Attrezzo[] attrezzoScandaglio = this.getAttrezzi();
				boolean eliminato = false;

				/* Elimina solo la prima istanza dell'oggetto sa eliminare trovata in borsa. */
				for (int i=0; i < numeroAttrezzi; i++) {
					if (attrezzoScandaglio[i] != null) {
						if (attrezzoScandaglio[i].getNome().equals(nomeAttrezzo) == true 
								&& eliminato == false) {
							// L'attrezzo che elimino lo uso come valore di ritorno del metodo.
							a = attrezzoScandaglio[i]; 	
							attrezzoScandaglio[i] = null;
							eliminato = true;
						}
					}
				}
			}
		}
		return a;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();

		if (!this.isEmpty()) {
			s.append("Contenuto borsa ("+this.getPeso()+"kg/"+this.getPesoMax()+"kg): ");
			for (int i= 0; i<this.numeroAttrezzi; i++)
				if (this.attrezzi[i] != null)
					s.append(attrezzi[i].toString()+" ");
		}
		else
			s.append("Borsa vuota");
		return s.toString();
	}
}
