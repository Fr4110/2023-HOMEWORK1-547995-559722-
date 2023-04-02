package it.uniroma3.diadia;


import javax.swing.plaf.basic.BasicListUI;

/**
 * Questa classe modella un comando.
 * Un comando consiste al piu' di due parole:
 * il nome del comando ed un parametro
 * su cui si applica il comando.
 * (Ad es. alla riga digitata dall'utente "vai nord"
 *  corrisponde un comando di nome "vai" e parametro "nord").
 *
 * @author  docente di POO
 * @version base
 */

public class Comando {

    private String nome;
    private String parametro;

    //@param istruzione	L'istruzione offerta come comando dal giocatore.
    
    public Comando(String istruzione) {
		/* Divide l'istruzione in diverse parole, le quali verrano poi, in caso,
		 * assegnate alle variabili nome e parametro
		 */
		// Divide l'istruzione fornita in tot parole
		String[] paroleLette = istruzione.split(" ", 0);
		
		// Poco elegante, ma funziona. Magari poi ci ritorno e trovo qualcos'altro...
		if (paroleLette.length > 0) {
			this.nome = paroleLette[0];
			if (paroleLette.length > 1)
				this.parametro = paroleLette[1];
		}
	}

    public String getNome() {
        return this.nome;
    }

    public String getParametro() {
        return this.parametro;
    }

    public boolean sconosciuto() {
        return (this.nome == null);
    }
}