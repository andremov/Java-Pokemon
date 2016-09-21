/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package pokemonviolet;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author Andres
 */
public class Trainer {
	
	// <editor-fold defaultstate="collapsed" desc="Attributes">
		/**
		 * Trainer image representation.
		 */
		public static BufferedImage SPRITE_SHEET;
		// <editor-fold defaultstate="collapsed" desc="General">
			/**
			 * Trainer reward.
			 */
			private final int reward;
			/**
			 * Trainer name.
			 */
			private final String name;
			/**
			 * Trainer type.
			 */
			private final String type;
		// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Pokemon">
			/**
			 * Player Pokemon Team.
			 */
			private Pokemon[] team;
			/**
			 * Number of Pokemon in Player Team.
			 */
			private int numPokemonTeam;
			/**
			 * Current active Pokemon.
			 */
			private int currentPokemon;
		// </editor-fold>
	// </editor-fold>

	public Trainer(String name, String type, int[][] pokemon, int maxTeam) {
		this.name = name;
		this.type = type;
		this.currentPokemon = 0;
		this.numPokemonTeam = 0;
		this.team = new Pokemon[6];
		
		for (int i = 0; i < maxTeam; i++) {
		//	if (pokemon[i] != null){
				team[numPokemonTeam] = new Pokemon(pokemon[i][0],"WILD",pokemon[i][1]);
				numPokemonTeam = numPokemonTeam+1;
		//	}
		}
		
		this.reward = 100;
	}
	
}
