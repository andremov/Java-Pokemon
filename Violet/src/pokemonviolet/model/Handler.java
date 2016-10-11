/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package pokemonviolet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pokemonviolet.scenes.Combat;
import pokemonviolet.scenes.Scene;
import pokemonviolet.view.GameWindow;

/**
 *
 * @author Andres
 */
public class Handler implements Runnable {

	// <editor-fold defaultstate="collapsed" desc="Attributes">
		// <editor-fold defaultstate="collapsed" desc="Statics">
			/**
			 * Canvas size in X dimension.
			 */
			public static int SCREEN_SIZE_X;
			/**
			 * Canvas size in Y dimension.
			 */
			public static int SCREEN_SIZE_Y;
			public static float RESIZE;
			public static int BASE_SCREEN_SIZE_X = 240;
			public static int BASE_SCREEN_SIZE_Y = 160;
		//</editor-fold>

		/**
		 * The Player in game.
		 */
		public Player player;
		/**
		 * Maps displayed.
		 */
		public Map[][] displayedMaps;
		/**
		 * Current battle handler.
		 */
		//		public Combat currentBattle;
		/**
		 * List of states.
		 */
		public static ArrayList<Scene> gameState;
		public int curMapX, curMapY;
		private boolean randomPokemon = false;
		private boolean fillTeam = false;
		private Thread thisThread;
		private GameWindow gw;
	// </editor-fold>

	/**
	 * Build game.
	 * @float resize Resize factor of window.
	 */
	public Handler(float resize) {
		gameState = new ArrayList<Scene>();

		RESIZE = resize;
		
		SCREEN_SIZE_X = (int) (BASE_SCREEN_SIZE_X * RESIZE);
		SCREEN_SIZE_Y = (int) (BASE_SCREEN_SIZE_Y * RESIZE);
		
		gw = new GameWindow(SCREEN_SIZE_X, SCREEN_SIZE_Y);

		thisThread = new Thread(this);

		gameState.add(new pokemonviolet.scenes.Title(this, true));
	}

	public void startGame(String name, String gender, int pokeID) {

		displayedMaps = new Map[3][3];

		player = new Player(name, gender, new Pokemon(pokeID, 5, "POKEBALL"));
		player.addItem("POKEBALL", 15);
		player.addItem("MASTERBALL", 1);
		player.addItem("POTION",1);

		player.addPokemon(new Pokemon(1));
		player.addPokemon(new Pokemon(2));
		player.addPokemon(new Pokemon(3));
		player.addPokemon(new Pokemon(4));
		player.addPokemon(new Pokemon(5));
		player.addPokemon(new Pokemon(6));
		player.addPokemon(new Pokemon(7));
		player.addPokemon(new Pokemon(8));
		player.addPokemon(new Pokemon(9));
		player.addPokemon(new Pokemon(10));
		player.addPokemon(new Pokemon(11));
		player.addPokemon(new Pokemon(12));
		player.addPokemon(new Pokemon(13));
		
		refreshDisplayedMaps();
		thisThread.start();
	}

	private void refreshDisplayedMaps() {
		int maxMapsX = pokemonviolet.data.NIC.NUM_MAPS_X, maxMapsY = pokemonviolet.data.NIC.NUM_MAPS_Y;
		for (int j = player.calcMapY() - 1; j < player.calcMapY() + 2; j++) {
			for (int i = player.calcMapX() - 1; i < player.calcMapX() + 2; i++) {
				List<String> thisMapInfo;

				if (i < 1 || i >= maxMapsX || j < 1 || j >= maxMapsY) {
					thisMapInfo = new ArrayList<String>(pokemonviolet.data.NIC.INFO_BLANK_MAP);
				} else {
					int mapID = ((j) * maxMapsX) + i;
					thisMapInfo = new ArrayList<String>(pokemonviolet.data.NIC.INFO_MAPS.get(mapID));
				}
				if (thisMapInfo.get(0).split(";")[0].compareTo("#") == 0) {
					thisMapInfo.remove(0);
					thisMapInfo.add(0, i + ";" + j);
				}

				int mapIDx, mapIDy;
				mapIDy = j - (player.calcMapY() - 2) - 1;
				mapIDx = i - (player.calcMapX() - 2) - 1;

				displayedMaps[mapIDx][mapIDy] = new Map(thisMapInfo, player.getxTile(), player.getyTile(), mapIDx, mapIDy);
			}
		}

		curMapX = calcX();
		curMapY = calcY();
	}
	
	private void cleanMaps() {
		for (int i = 0; i < displayedMaps.length; i++) {
			for (int j = 0; j < displayedMaps[i].length; j++) {
				displayedMaps[i][j] = null;
			}
		}
	}
	
	private boolean getCanMove(String direction) {
		boolean canMove = false;
		
		int posX, posY;
		int pTotalx, pTotaly;
		
		pTotalx = -1;
		pTotaly = -1;
		posX = -1;
		posY = -1;
		
		switch (direction) {
			case "LEFT":
				pTotalx = player.getxTile() - 1;
				pTotaly = player.getyTile();
				
				posX = player.calcXinMap() - 1;
				posY = player.calcYinMap();
				break;
			case "RIGHT":
				pTotalx = player.getxTile() + 1;
				pTotaly = player.getyTile();
				
				posX = player.calcXinMap() + 1;
				posY = player.calcYinMap();
				break;
			case "UP":
				pTotalx = player.getxTile();
				pTotaly = player.getyTile() - 1;
				
				posX = player.calcXinMap();
				posY = player.calcYinMap() - 1;
				break;
			case "DOWN":
				pTotalx = player.getxTile();
				pTotaly = player.getyTile() + 1;
				
				posX = player.calcXinMap();
				posY = player.calcYinMap() + 1;
				break;
		}
		
		int newPlayerMapX, newPlayerMapY;
		newPlayerMapX = ((int) Math.floor(pTotalx / Map.MAP_ROW_TILES)) + 1;
		newPlayerMapY = ((int) Math.floor(pTotaly / Map.MAP_ROW_TILES)) + 1;
		
		int diffX, diffY;
		
		diffX = newPlayerMapX - (player.calcMapX()+1);
		diffY = newPlayerMapY - (player.calcMapY()+1);
		
		posX = posX - (diffX * Map.MAP_ROW_TILES);
		posY = posY - (diffY * Map.MAP_ROW_TILES);
		
		if (displayedMaps[diffX + 1][diffY + 1].getBounds()[posX][posY].substring(0, 1).compareTo("0") == 0) {
			canMove = true;
		}
		
		return canMove;
	}
	
	private boolean moveMap() {
		int baseX, baseY;
		boolean isDone = true;
		
		baseX = calcX();
		baseY = calcY();
		
		if (curMapX == baseX && curMapY == baseY) {
			switch (player.getDirection()) {
				case "LEFT":
					if (getCanMove(player.getDirection())) {
						player.setxTile(player.getxTile() - 1);
					}
					break;
				case "RIGHT":
					if (getCanMove(player.getDirection())) {
						player.setxTile(player.getxTile() + 1);
					}
					break;
				case "UP":
					if (getCanMove(player.getDirection())) {
						player.setyTile(player.getyTile() - 1);
					}
					break;
				case "DOWN":
					if (getCanMove(player.getDirection())) {
						player.setyTile(player.getyTile() + 1);
					}
					break;
			}
			mapCheck();
		}
		
		baseX = calcX();
		baseY = calcY();
		
		if (curMapX != baseX || curMapY != baseY) {
			int amount = player.MOVE_POS;
			if (player.isRunning()) {
				amount = (int) (amount * player.RUN_MULT);
			}
			
			switch (player.getvDirection()) {
				case "LEFT":
					if (Math.abs(baseX - curMapX) >= amount) {
						curMapX = curMapX + amount;
						isDone = false;
					} else {
						curMapX = baseX;
					}
					break;
				case "RIGHT":
					if (Math.abs(baseX - curMapX) >= amount) {
						curMapX = curMapX - amount;
						isDone = false;
					} else {
						curMapX = baseX;
					}
					break;
				case "UP":
					if (Math.abs(baseY - curMapY) >= amount) {
						curMapY = curMapY + amount;
						isDone = false;
					} else {
						curMapY = baseY;
					}
					break;
				case "DOWN":
					if (Math.abs(baseY - curMapY) >= amount) {
						curMapY = curMapY - amount;
						isDone = false;
					} else {
						curMapY = baseY;
					}
					break;
			}
			if (isDone) {
				tileCheck();
			}
		}
		
		return isDone;
	}
	
	private int calcX() {
		
		int xTile, xDisplace, xTotal;
		
		xTile = player.getxTile();
		
		while (xTile > 39) {
			xTile = xTile - 20;
		}
		
		while (xTile < 20) {
			xTile = xTile + 20;
		}
		
		xDisplace = xTile * (Map.MAP_TOTAL_SIZE_X / Map.MAP_ROW_TILES);
		
		xTotal = (xDisplace * -1) + (BASE_SCREEN_SIZE_X / 2);
		
		return xTotal;
	}
	
	private int calcY() {
		
		int yTile, yTotal, yDisplace;
		
		yTile = player.getyTile();
		
		while (yTile > 39) {
			yTile = yTile - 20;
		}
		
		while (yTile < 20) {
			yTile = yTile + 20;
		}
		
		yDisplace = yTile * (Map.MAP_TOTAL_SIZE_Y / Map.MAP_ROW_TILES);
		
		yTotal = (yDisplace * -1) + (BASE_SCREEN_SIZE_Y / 2);
		
		return yTotal;
	}
	
	private void mapCheck() {
		if (player.calcMapX() != displayedMaps[1][1].getxMap() || player.calcMapY() != displayedMaps[1][1].getyMap()) {
			cleanMaps();
			refreshDisplayedMaps();
		}
	}

	private void tileCheck() {
		int xTile, yTile;
		xTile = player.getxTile();
		yTile = player.getyTile();
		while (xTile > 19) {
			xTile = xTile - 20;
		}
		while (xTile < 0) {
			xTile = xTile + 20;
		}
		while (yTile > 19) {
			yTile = yTile - 20;
		}
		while (yTile < 0) {
			yTile = yTile + 20;
		}

		String[] tileInfo = displayedMaps[1][1].getTileInformation(yTile, xTile);
		if (Integer.parseInt(tileInfo[2]) == 0 && Integer.parseInt(tileInfo[3]) == 1) {
			steppedGrass();
		}
	}

	private void steppedGrass() {
		player.setSpawnSteps(player.getSpawnSteps() - 1);
		if (player.getSpawnSteps() == 0) {
			player.setRunning(false);
			int maxNum = 0;
			int[][] enemyTeam = new int[6][2];

			if (fillTeam) {
				for (int i = 0; i < player.getNumPokemonTeam(); i++) {
					if (randomPokemon) {
						Random rnd = new Random();
						enemyTeam[i][0] = rnd.nextInt(151) + 1;
						enemyTeam[i][1] = player.getTeam()[i].getLevel() - 3;
						maxNum = maxNum + 1;
					} else {
						enemyTeam[i] = getWildPokemon();
						maxNum = maxNum + 1;
					}
				}
			} else {
				int i = 0;
				if (randomPokemon) {
					Random rnd = new Random();
					enemyTeam[i][0] = rnd.nextInt(151) + 1;
					enemyTeam[i][1] = player.getTeam()[i].getLevel() - 3;
					maxNum = maxNum + 1;
				} else {
					enemyTeam[i] = getWildPokemon();
					maxNum = maxNum + 1;
				}
			}

			gameState.add(new Combat(player, new Trainer("", "", enemyTeam, maxNum), true, this));
		}
	}

	private int[] getWildPokemon() {
		int xTile, yTile;
		xTile = player.getxTile();
		yTile = player.getyTile();
		while (xTile > 20) {
			xTile = xTile - 20;
		}
		while (xTile < 0) {
			xTile = xTile + 20;
		}
		while (yTile > 20) {
			yTile = yTile - 20;
		}
		while (yTile < 0) {
			yTile = yTile + 20;
		}
		return displayedMaps[1][1].getWildPokemon(xTile, yTile);
	}
	
	public void canContinue() {
		gw.setVisible(true);
		gw.startCanvasThread();
	}
	
	public void clearStates(String limit) {
		boolean pullOut = true;
		while (pullOut && gameState.get(gameState.size() - 1).getName().compareTo(limit) != 0) {
			gameState.get(gameState.size() - 1).dispose();
			pullOut = !gameState.isEmpty();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			if (!gameState.isEmpty()) {
				if (gameState.get(gameState.size() - 1).getName().compareTo("GAME") == 0) {
					if (player.getvDirection().compareTo("") != 0) {
						boolean finished = moveMap();
						
						if (finished) {
							player.setvDirection("");
						}
					}
				}
				
				try {
					Thread.sleep(70);
				} catch (InterruptedException ex) {
				}
			}
		}
	}
}
