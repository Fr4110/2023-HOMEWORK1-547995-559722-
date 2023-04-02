package it.uniroma3.diadia;

import it.uniroma3.diadia.ambienti.Labirinto;
import it.uniroma3.diadia.ambienti.Stanza;
import it.uniroma3.diadia.giocatore.Giocatore;

/**
 * Questa classe modella una partita del gioco
 *
 * @author  docente di POO
 * @see Stanza
 * @version base
 */

public class Partita {

	static final private int CFU_INIZIALI = 20;

	private Labirinto labirinto;
	private boolean finita;
	private Giocatore giocatore = new Giocatore();

	public Partita(){
		creaLabirinto();
		this.finita = false;
		this.giocatore.setCfu(CFU_INIZIALI);
	}

    /**
     * Crea tutte le stanze e le porte di collegamento.
     *      
     * @deprecated Usare direttamente costruttore e i metodi offerti da Labirinto.	
     */
    private void creaLabirinto() {
		/* Crea il labirinto */
		Labirinto labirintoOG = new Labirinto();
		
		labirinto = labirintoOG;
    }
    /** Per il momento le stanze vengono accedute in maniera abbastanza brutta 
     * dalle altre classi: praticamente le altre classi continuano ad accedere
     * ai campi stanzaCorrente e stanzaVincente tramite i metodi offerti 
     * da Partita, nonostante appartengano invero alla classe Labirinto, la
     * quale deve essere ovviamente interrogata dalla classe Partita perché 
     * il codice funzioni.
     * 
     * REFACTORING ON COURSE
     */
	public Stanza getStanzaVincente() {
		return labirinto.getStanzaVincente();
	}

	public void setStanzaCorrente(Stanza stanzaCorrente) {
//		this.stanzaCorrente = stanzaCorrente;
		this.labirinto.setStanzaCorrente(stanzaCorrente);
	}

	public Stanza getStanzaCorrente() {
		return this.labirinto.getStanzaCorrente();
	}
	
	public Labirinto getLabirinto() {
		return this.labirinto;
	}
	
	/**
	 * Restituisce vero se e solo se la partita e' stata vinta
	 * @return vero se partita vinta
	 */
	public boolean vinta() {
		return this.getStanzaCorrente()== this.getStanzaVincente();
	}

	/**
	 * Restituisce vero se e solo se la partita e' finita
	 * @return vero se partita finita
	 */
	public boolean isFinita() {
		return finita || vinta() || (giocatore.getCfu() == 0);
	}

	/**
	 * Imposta la partita come finita
	 *
	 */
	public void setFinita() {
		this.finita = true;
	}

	public int getCfu() {
		return this.giocatore.getCfu();
	}

	public void setCfu(int cfu) {
		this.giocatore.setCfu(cfu);		
	}

	public Giocatore getGiocatore() {
		return this.giocatore;
	}	
}
