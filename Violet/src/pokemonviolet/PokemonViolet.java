/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package pokemonviolet;


/**
 *
 * @author Andres
 */
public class PokemonViolet {
	
	public static void main(String[] args) throws InterruptedException{
		
		//	new MapBuilder(javax.swing.JFrame.EXIT_ON_CLOSE);
		
			Game game = new Game();

			Thread gameThread = new Thread(game);
			gameThread.start();
		
    }
	
}
