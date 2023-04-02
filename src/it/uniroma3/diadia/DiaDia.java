package it.uniroma3.diadia;

import java.net.http.HttpResponse.BodySubscriber;
import java.util.Scanner;

import it.uniroma3.diadia.ambienti.Labirinto;
import it.uniroma3.diadia.ambienti.Stanza;
import it.uniroma3.diadia.attrezzi.Attrezzo;
import it.uniroma3.diadia.giocatore.Borsa;
import it.uniroma3.diadia.giocatore.Giocatore;

/**
 * Classe principale di diadia, un semplice gioco di ruolo ambientato al dia.
 * Per giocare crea un'istanza di questa classe e invoca il letodo gioca
 *
 * Questa e' la classe principale crea e istanzia tutte le altre
 *
 * @author  docente di POO 
 *         (da un'idea di Michael Kolling and David J. Barnes) 
 *          
 * @version base
 */

public class DiaDia {

	static final private String MESSAGGIO_BENVENUTO = ""+
			"Ti trovi nell'Universita', ma oggi e' diversa dal solito...\n" +
			"Meglio andare al piu' presto in biblioteca a studiare. Ma dov'e'?\n"+
			"I locali sono popolati da strani personaggi, " +
			"alcuni amici, altri... chissa!\n"+
			"Ci sono attrezzi che potrebbero servirti nell'impresa:\n"+
			"puoi raccoglierli, usarli, posarli quando ti sembrano inutili\n" +
			"o regalarli se pensi che possano ingraziarti qualcuno.\n\n"+
			"Per conoscere le istruzioni usa il comando 'aiuto'.";

	static final private String[] elencoComandi = {"vai", "aiuto", "fine"};
	
	private IOConsole ioConsole = new IOConsole();
	private Partita partita;
	private Labirinto labirintoCorrente;
	

	public DiaDia() {
		this.partita = new Partita();
		this.labirintoCorrente = this.partita.getLabirinto();
	}

	public void gioca() {
		String istruzione; 
	
		ioConsole.mostraMessaggio(MESSAGGIO_BENVENUTO);
		do		
			istruzione = ioConsole.leggiRiga();
		while (!processaIstruzione(istruzione));
	}   


	/**
	 * Processa una istruzione 
	 *
	 * @return true se l'istruzione e' eseguita e il gioco continua, false altrimenti
	 */
	private boolean processaIstruzione(String istruzione) {
		Comando comandoDaEseguire = new Comando(istruzione);

		if (comandoDaEseguire.getNome() == null)
			ioConsole.mostraMessaggio("Comando inesistente. \nScrivere un comando valido.");
		else if (comandoDaEseguire.getNome().equals("fine")) {
			this.fine(); 
			return true;
		} else if (comandoDaEseguire.getNome().equals("vai"))
			this.vai(comandoDaEseguire.getParametro());
		else if (comandoDaEseguire.getNome().equals("prendi"))
			this.prendi(comandoDaEseguire.getParametro());
		else if (comandoDaEseguire.getNome().equals("posa"))
			this.posa(comandoDaEseguire.getParametro());
		else if (comandoDaEseguire.getNome().equals("aiuto"))
			this.aiuto();

		else
			ioConsole.mostraMessaggio("Comando sconosciuto");


		if (this.partita.vinta()) {
			ioConsole.mostraMessaggio("Hai vinto!");
			return true;
		} else
			return false;
	}   

	

	// implementazioni dei comandi dell'utente:

	/**
	 * Cerca di andare in una direzione. Se c'e' una stanza ci entra 
	 * e ne stampa il nome, altrimenti stampa un messaggio di errore
	 */
	private void vai(String direzione) {
		
		if(direzione==null)
			ioConsole.mostraMessaggio("Dove vuoi andare?");
		
		Stanza prossimaStanza = null;
		prossimaStanza = this.labirintoCorrente.getStanzaCorrente().getStanzaAdiacente(direzione);
		
		if (prossimaStanza == null)
			ioConsole.mostraMessaggio("Direzione inesistente");
		else {
			this.labirintoCorrente.setStanzaCorrente(prossimaStanza);
			Giocatore giocatore = this.partita.getGiocatore();
			int cfu = giocatore.getCfu();
			
			giocatore.setCfu(cfu--);
		}
		ioConsole.mostraMessaggio(partita.getStanzaCorrente().getDescrizione());
	}

	private void posa(String nomeAttrezzoDaPosare) {
		Stanza stanzaCorrente = this.labirintoCorrente.getStanzaCorrente();
		Borsa borsaGiocatore = this.partita.getGiocatore().getBorsa();
		
		// Controlla che ci siano attrezzi in borsa		
		if (borsaGiocatore.getPeso() <= 0)
			ioConsole.mostraMessaggio("Non possiedi attrezzi, non puoi posare niente!");
		
		else {
			// Controlla che si stia cercando un attrezzo			
			if (nomeAttrezzoDaPosare == null)
				ioConsole.mostraMessaggio("Quale attrezzo vuoi posare?\n" + borsaGiocatore.toString());
			
			else {
				// Controlla di possedere l'attrezzo in borsa
				if (borsaGiocatore.hasAttrezzo(nomeAttrezzoDaPosare) == false) 
					ioConsole.mostraMessaggio("Non possiedi " + nomeAttrezzoDaPosare + ".");
				
				else {
					// Controlla di poter posare l'attrezzo nella stanza
					
					/* Prima verifica il numero effettivo di attrezzi in stanza, poiché
					 * la length dell'array attrezzi[] è pari al numero massimo di attrezzi,
					 * non al numero di attrezzi effettivamente presenti...
					 */
					int numeroMaxAttrezziInStanza = stanzaCorrente.getNumeroMassimoAttrezzi();
					int dimensioneArrayAttrezziInStanza = stanzaCorrente.getAttrezzi().length;
					int numeroEffettivoAttrezziInStanza = 0;
					Attrezzo[] attrezziInStanza = stanzaCorrente.getAttrezzi();
					
					for (int i=0; i < dimensioneArrayAttrezziInStanza; i++) {	// Si potrebbe anche usare un foreach
						if (attrezziInStanza[i] != null)
							numeroEffettivoAttrezziInStanza++;
					}
					// ...poi verifico di poter aggiungere l'attrezzo in stanza.
					if (numeroEffettivoAttrezziInStanza + 1 > numeroMaxAttrezziInStanza) 
						ioConsole.mostraMessaggio("La stanza è piena di oggetti, non riesci a posare niente. "
								+ "Prova ad andare in un'altra stanza più vuota.");
					
					else {
						// Aggiungo l'attrezzo in stanza e lo rimuovo dalla borsa (SOLO UNO!)
						boolean posato = false;
						Attrezzo attrezzoDaPosare = borsaGiocatore.getAttrezzo(nomeAttrezzoDaPosare);
						
						for (int i=0; i < dimensioneArrayAttrezziInStanza; i++) {
							if (attrezziInStanza[i] == null 
									&& posato == false) {
								attrezziInStanza[i] = attrezzoDaPosare;
								borsaGiocatore.removeAttrezzo(nomeAttrezzoDaPosare);
								posato = true;
							}
						}
						
						// Avviso di avvenuta azione
						ioConsole.mostraMessaggio("Hai posato " + nomeAttrezzoDaPosare 
								+ ".\n" + borsaGiocatore.toString()
								+ ".\n" + stanzaCorrente.toString());
					}
					
				}
			}
		}
		
	}

	private void prendi(String attrezzoDaPrendere) {
		Stanza stanzaCorrente = this.labirintoCorrente.getStanzaCorrente();
		Attrezzo[] attrezziInStanza = stanzaCorrente.getAttrezzi();

		// Controlla che ci siano attrezzi nella stanza
		if (attrezziInStanza.length <= 0) 
			ioConsole.mostraMessaggio("Non ci sono oggetti in questa stanza! "
					+ "Forse puoi trovarne in un'altra.");
		else {
			// Controlla che si stia cercando un attrezzo
			if (attrezzoDaPrendere == null)
				ioConsole.mostraMessaggio("Quale attrezzo vuoi prendere dalla stanza?" + stanzaCorrente.toString());
			else {
				// Controlla che ci sia l'attrezzo cercato nella stanza
				if (stanzaCorrente.hasAttrezzo(attrezzoDaPrendere) == false)
					ioConsole.mostraMessaggio("L'attrezzo che cerchi non si trova in questa stanza. "
							+ "Forse puoi trovarlo in un'altra stanza.");
				else {
					/* Controlla di poter mettere l'attrezzo cercato in stanza.
					 * ATTENZIONE! L'attrezzo trovato è la prima copia del tipo di oggetto
					 * cercato presente in stanza.
					 */
					Attrezzo attrezzoPreso = stanzaCorrente.getAttrezzo(attrezzoDaPrendere);
					int pesoAttrezzo = attrezzoPreso.getPeso();
					int pesoBorsa = this.partita.getGiocatore().getBorsa().getPeso();
					int pesoMaxBorsa = this.partita.getGiocatore().getBorsa().getPesoMax();
					
					if (pesoBorsa + pesoAttrezzo > pesoMaxBorsa)
						ioConsole.mostraMessaggio("L'attrezzo è troppo pesante, non riesci a metterlo in borsa. "
								+ "Posa qualche attrezzo prima di poter prendere questo.\n"
								+ "Peso " + attrezzoDaPrendere + ": " + pesoAttrezzo + "\n"
								+ "Peso della borsa: " + pesoBorsa + "\n"
								+ "Peso massimo trasportabile: " + pesoMaxBorsa + "\n"
								+ "Peso minimo da togliere: " 
								+ ((pesoBorsa + pesoAttrezzo) - pesoMaxBorsa));
					else {
						// Prende l'attrezzo dalla stanza e lo pone nella borsa
						Borsa borsa = this.partita.getGiocatore().getBorsa();
						borsa.addAttrezzo(attrezzoPreso);
						//ATTENZIONE! Elimina tutte le copie dello stesso attrezzo
						stanzaCorrente.removeAttrezzo(attrezzoPreso);
						
						// Avviso che l'oggetto è stato preso
						ioConsole.mostraMessaggio("Hai ottenuto " + attrezzoDaPrendere + "!");
					}
				}
			}
		}
	}

	/**
	 * Stampa informazioni di aiuto.
	 */
	private void aiuto() {
		for(int i=0; i< elencoComandi.length; i++) 
			System.out.print(elencoComandi[i]+" ");
		ioConsole.mostraMessaggio("");
	}

	/**
	 * Comando "Fine".
	 */
	private void fine() {
		ioConsole.mostraMessaggio("Grazie di aver giocato!");  // si desidera smettere
	}

	public static void main(String[] argc) {
		DiaDia gioco = new DiaDia();
		gioco.gioca();
	}
} 