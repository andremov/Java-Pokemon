/*
 *  Team Violet - A University Project by Andres Movilla
 *  Team COPYRIGHT 2002-2016 Team.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Team character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Team content on Team Violet.
 */
package pokemonviolet.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import pokemonviolet.model.Handler;
import pokemonviolet.model.Pokemon;
import pokemonviolet.model.PokemonMove;

/**
 *
 * @author Andres
 */
public class Team extends Scene {
	
	private Pokemon[] team;
	private static String path = "assets/pokeui/";
	private int selection;
	private int swap;
	private int animFrame;
	private BufferedImage animImg;
	private boolean inCombat;
	private Combat combat;
	private boolean canExit;
	private int curPokemon;
	private int itemToUse;
	private boolean usingItem;
	private int[] TMappropriate;
	
			
	public Team(Handler main) {
		super(main, "TEAM", true);
		
		this.curPokemon = -1;
		
		this.canExit = true;
		this.combat = null;
		this.inCombat = false;
		
		this.TMappropriate = new int[6];
		for (int i = 0; i < TMappropriate.length; i++) {
			TMappropriate[i] = 2;
		}
		
		this.usingItem = false;
		this.itemToUse = -1;
		
		this.team = main.player.getTeam();
		selection = 0;
		swap = -1;
		animFrame = 0;
		try {
			animImg = ImageIO.read(new File("assets/ballAnim.png"));
		} catch (IOException ex) {
		}
	}
	
	public Team(Handler main, Combat cmb, boolean canExit, int currentPokemonNum) {
		super(main, "TEAM", true);
		
		this.curPokemon = currentPokemonNum;
		
		this.canExit = canExit;
		this.combat = cmb;
		this.inCombat = true;
		
		this.TMappropriate = new int[6];
		for (int i = 0; i < TMappropriate.length; i++) {
			TMappropriate[i] = 2;
		}
		
		this.usingItem = false;
		this.itemToUse = -1;
		
		this.team = main.player.getTeam();
		selection = 0;
		swap = -1;
		animFrame = 0;
		try {
			animImg = ImageIO.read(new File(path+"ballAnim.png"));
		} catch (IOException ex) {
		}
	}
	
	public Team(Handler main, Combat cmb, int itemID) {
		super(main, "TEAM", true);
		
		this.curPokemon = -1;
		
		this.canExit = true;
		this.combat = cmb;
		this.inCombat = true;
		if (this.combat == null) {
			this.inCombat = false;
		}
		
		this.usingItem = true;
		this.itemToUse = itemID;
		
		this.team = main.player.getTeam();
		
		this.TMappropriate = new int[6];
		if (new pokemonviolet.model.Item(itemToUse).getUseOutBattle() < 3) {
			for (int i = 0; i < TMappropriate.length; i++) {
				TMappropriate[i] = 2;
			}
		} else {
			for (int i = 0; i < TMappropriate.length; i++) {
				if (team[i] != null) {
					String internalName = new pokemonviolet.model.Item(itemToUse).getMoveName();
					TMappropriate[i] = 0;
					int counter = 0;
					while (counter < pokemonviolet.data.NIC.INFO_TM.size()) {
						if (pokemonviolet.data.NIC.INFO_TM.get(counter).split("=")[0].compareTo(internalName) == 0) {
							int level2 = 0;
							while (level2 < pokemonviolet.data.NIC.INFO_TM.get(counter).split("=")[1].split(",").length) {
								if (team[i].getNameInternal().compareTo(pokemonviolet.data.NIC.INFO_TM.get(counter).split("=")[1].split(",")[level2]) == 0) {
									TMappropriate[i] = 1;
									level2 = pokemonviolet.data.NIC.INFO_TM.get(counter).split("=")[1].split(",").length;
								} else {
									level2 = level2 + 1;
								}
							}
							counter = pokemonviolet.data.NIC.INFO_TM.size();
						} else {
							counter = counter + 1;
						}
					}
				} else {
					TMappropriate[i] = 2;
				}
			}
		}
		
		selection = 0;
		swap = -1;
		animFrame = 0;
		try {
			animImg = ImageIO.read(new File(path+"ballAnim.png"));
		} catch (IOException ex) {
		}
	}

	private void useItem() {
		if (inCombat) {
			if (useMedTypeItem()) {
				main.player.subItem(itemToUse);
				main.clearStates("COMBAT");
				combat.usedItem();
			}
		} else if (!inCombat) {
			if (new pokemonviolet.model.Item(itemToUse).getUseOutBattle() == 1) {
				if (useMedTypeItem()) {
					main.player.subItem(itemToUse);
					main.clearStates("BAG");
				}				
			} else if (new pokemonviolet.model.Item(itemToUse).getUseOutBattle() == 3) {
				if (TMappropriate[selection] == 1) {
					if (team[selection].getNumMoves() < 4) {
						team[selection].addMove(new pokemonviolet.model.Item(itemToUse).getMoveName());
						main.clearStates("BAG");
						main.player.subItem(itemToUse);
					} else {
						main.gameState.add(new LearnMove(main, team[selection], new PokemonMove(new pokemonviolet.model.Item(itemToUse).getMoveName()), "BAG"));
					}
				}
			} else if (new pokemonviolet.model.Item(itemToUse).getUseOutBattle() == 4) {
				if (TMappropriate[selection] == 1) {
					if (team[selection].getNumMoves() < 4) {
						team[selection].addMove(new pokemonviolet.model.Item(itemToUse).getMoveName());
						main.clearStates("BAG");
						main.player.subItem(itemToUse);
					} else {
						main.gameState.add(new LearnMove(main, team[selection], new PokemonMove(new pokemonviolet.model.Item(itemToUse).getMoveName()), "BAG"));
					}
				}
			}
		}
	}
	
	private boolean useMedTypeItem() {
		boolean used = false;
		if (new pokemonviolet.model.Item(itemToUse).getId() < 22) {
			used = true;
			team[selection].setCurHP(team[selection].getCurHP() + (int) new pokemonviolet.model.Item(itemToUse).getValue());
		} else if (new pokemonviolet.model.Item(itemToUse).getId() < 25) {
			used = true;
			team[selection].setCurHP((double) 1.0);
		} else if (new pokemonviolet.model.Item(itemToUse).getId() == 32) {
			used = true;
			team[selection].setCurHP((double) 0.5);
		} else if (new pokemonviolet.model.Item(itemToUse).getId() < 33) {
			used = true;
			team[selection].setCurHP((double) 1.0);
		} else if (new pokemonviolet.model.Item(itemToUse).getId() < 41) {
			used = true;
			team[selection].setCurHP(team[selection].getCurHP() + (int) new pokemonviolet.model.Item(itemToUse).getValue());
		} else if (new pokemonviolet.model.Item(itemToUse).getId() == 42 && team[selection].isFainted()) {
			used = true;
			team[selection].revive(1.0);
		} else if (new pokemonviolet.model.Item(itemToUse).getId() < 45) {
			boolean usedIt = false;
			for (int i = 0; i < team[selection].getMoveSet().length; i++) {
				if (new pokemonviolet.model.Item(itemToUse).getValue() != 0) {
					if (!usedIt && team[selection].getMoveSet()[i].getPP() + new pokemonviolet.model.Item(itemToUse).getValue() <= team[selection].getMoveSet()[i].getPPMax()) {
						usedIt = true;
						team[selection].getMoveSet()[i].setPP(team[selection].getMoveSet()[i].getPP() + (int) new pokemonviolet.model.Item(itemToUse).getValue());
					}
				} else {
					if (!usedIt && team[selection].getMoveSet()[i].getPP() != team[selection].getMoveSet()[i].getPPMax()) {
						usedIt = true;
						team[selection].getMoveSet()[i].setPP(team[selection].getMoveSet()[i].getPPMax());
					}
				}
			}
			used = usedIt;
		} else if (new pokemonviolet.model.Item(itemToUse).getId() < 47) {
			used = true;
			for (int i = 0; i < team[selection].getMoveSet().length; i++) {
				if (new pokemonviolet.model.Item(itemToUse).getValue() != 0) {
					if (team[selection].getMoveSet()[i].getPP() + new pokemonviolet.model.Item(itemToUse).getValue() <= team[selection].getMoveSet()[i].getPPMax()) {
						team[selection].getMoveSet()[i].setPP(team[selection].getMoveSet()[i].getPP() + (int) new pokemonviolet.model.Item(itemToUse).getValue());
					}
				} else {
					if (team[selection].getMoveSet()[i].getPP() != team[selection].getMoveSet()[i].getPPMax()) {
						team[selection].getMoveSet()[i].setPP(team[selection].getMoveSet()[i].getPPMax());
					}
				}
			}
		}
		return used;
	}
	
	@Override
	public void receiveKeyAction(String action, String state) {
		if (state.compareTo("RELEASE") == 0) {
			if (action.compareTo("A") == 0) {
				accept();
			} else if (action.compareTo("B") == 0) {
				cancel();
			} else if (action.compareTo("START") == 0) {
			}
		} else if (state.compareTo("PRESS") == 0) {
			if (action.compareTo("UP") == 0 || action.compareTo("DOWN") == 0 || action.compareTo("LEFT") == 0 || action.compareTo("RIGHT") == 0) {
				move(action);
			}
		}
	}

	@Override
	protected void accept() {
		if (selection == 6) {
			if (canExit) {
				if (inCombat) {
					combat.cancelExtraAction();
				}
				this.dispose();
			}
		}else if (inCombat && !usingItem) {
			if (!team[selection].isFainted() && curPokemon != selection) {
				combat.receiveNewPokemon(selection);
				this.dispose();
			}
		}else if (usingItem) {
			useItem();
		} else if (swap == -1) {
			swap = selection;
		} else {
			if (selection != swap) {
				main.player.movePokemon(selection, swap);
			}
			swap = -1;
		}
	}

	@Override
	protected void cancel() {
		if (swap == -1) {
			selection = 6;
		} else {
			swap = -1;
		}
	}

	@Override
	protected void move(String dir) {
		if (selection == 0) {
			if (dir.compareTo("RIGHT") == 0) {
				selection = 1;
				if (team[selection] == null) {
					selection = 6;
				}
			}
		} else {
			if (dir.compareTo("LEFT") == 0) {
				selection = 0;
				if (team[selection] == null) {
					selection = 6;
				}
			} else {
				if (dir.compareTo("UP") == 0) {
					selection = selection - 1;
					
					if (selection < 1) {
						if (team[5] == null) {
							selection = selection + 1;
						} else {
							selection = 5;
						}
					} else if (selection > 6) {
						if (team[1] == null) {
							selection = selection + 1;
						} else {
							selection = 1;
						}
					} else if (team[selection] == null) {
						selection = selection + 1;
						
						int value = 5;
						boolean found = false;
						while (value > 0 && found == false) {
							if (team[value] != null) {
								found = true;
							}
							
							if (!found) {
								value = value - 1;
							}
						}
						
						if (found) {
							selection = value;
						}
					}
				} else if (dir.compareTo("DOWN") == 0) {
					selection = selection + 1;
					
					if (selection < 1) {
						if (team[5] == null) {
							selection = selection - 1;
						} else {
							selection = 5;
						}
					} else if (selection > 6) {
						if (team[1] == null) {
							selection = selection - 1;
						} else {
							selection = 1;
						}
					} else if (team[selection] == null) {
						selection = 6;
					}
				}
			}
		}
	}

	@Override
	protected void start() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public BufferedImage getDisplay() throws IOException {
		BufferedImage tempStitched = new BufferedImage(ssX, ssY, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tempStitched.getGraphics();

		g.drawImage(ImageIO.read(new File(path+"background.png")), 0, 0, ssX, ssY, null);

		g.drawImage(genWindow(6, (int) (80 * RESIZE), (int) (49 * RESIZE)), 8, ssY - 20 - (int) ((49 * RESIZE)), null);
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		if (selection < 6) {
			g.drawString("HP: "+team[selection].getCurHP()+"/"+team[selection].getStatHP(), 22, ssY - 35 - (int) ((49 * RESIZE) / 2));
			if (swap != -1) {
				g.drawString("Swapping...", 22, ssY - 10 - (int) ((49 * RESIZE) / 2));
				g.drawString("HP: "+team[swap].getCurHP()+"/"+team[swap].getStatHP(), 22, ssY + 15 - (int) ((49 * RESIZE) / 2));
			}
		} else {
			if (canExit) {
				g.drawString("Exit", 22, ssY - 35 - (int) ((49 * RESIZE) / 2));
			} else {
				g.drawString("Can't exit!", 22, ssY - 35 - (int) ((49 * RESIZE) / 2));
			}
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		for (int i = 0; i < team.length; i++) {
			String state = "";
			if (team[i] != null) {
				if (i == selection) {
					state = "Active";
				}
			} else {
				state = "Blank";
			}
			
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			if (i == 0) {
				int dimX = (int) (78 * RESIZE), dimY = (int) (49 * RESIZE);

				g.setColor(Color.green);
				if ((float) team[i].getCurHP() / (float) team[i].getStatHP() < 0.5f) {
					g.setColor(Color.orange);
					if ((float) team[i].getCurHP() / (float) team[i].getStatHP() < 0.25f) {
						g.setColor(Color.red);
					}
				}
				g.fillRect(57, 112, (int) ((float) 100 * (float) ((float) team[i].getCurHP() / (float) team[i].getStatHP())), 10);
				g.setColor(Color.white);

				g.drawImage(ImageIO.read(new File(path+"mainPokemon"+state+".png")), 10, 50, dimX, dimY, null);

				if (state.compareTo("Blank") != 0) {

					if (swap != i) {
						int id = team[i].getId();
						g.drawImage(pokemonviolet.data.NIC.pokemonIcons.getSubimage( (int) Math.floor((id-1) % 10) * 40, (int) Math.floor((id-1) / 10) * 40, 40, 40), 10, 37, (int) (40 * 1.5f), (int) (40 * 1.5f), null);
					} else {
						int yDisplace = (int) (((int) Math.pow(animFrame - 6, 2)) * 0.5f);
						g.drawImage(animImg.getSubimage(animFrame * 16, 0, 16, 16), 20, 45 + yDisplace, (int) (16 * RESIZE), (int) (16 * RESIZE), null);
						animFrame = animFrame + 1;
						if (animFrame > 11) {
							animFrame = 0;
						}
					}
					
					if (TMappropriate[i] == 0) {
						g.setColor(Color.orange);
					} else if (TMappropriate[i] == 1) {
						g.setColor(Color.GREEN);
					} else {
						g.setColor(Color.white);
					}
					g.drawString(team[i].getNameNick(), 20, 108);
					g.setColor(Color.white);
					g.drawString("Lv. "+team[i].getLevel(), 45, 139);

					if (team[i].isFainted()) {
						g.setColor(Color.red);
						g.drawString("FNT", 130, 139);
						g.setColor(Color.white);
					}
				}
			} else {
				int dimX = (int) (142 * RESIZE), dimY = (int) (22 * RESIZE);

				if (state.compareTo("Blank") != 0) {
					g.setColor(Color.green);
					if ((float) team[i].getCurHP() / (float) team[i].getStatHP() < 0.5f) {
						g.setColor(Color.orange);
						if ((float) team[i].getCurHP() / (float) team[i].getStatHP() < 0.25f) {
							g.setColor(Color.red);
						}
					}
					g.fillRect(355, 29 + ((i - 1) * (dimY + 10)), (int) ((float) 100 * (float) ((float) team[i].getCurHP() / (float) team[i].getStatHP())), 10);
					g.setColor(Color.white);
				}

				g.drawImage(ImageIO.read(new File(path+"otherPokemon"+state+".png")), 180, 15 + ((i - 1) * (dimY + 10)), dimX, dimY, null);

				if (state.compareTo("Blank") != 0) {

					if (swap != i) {
						int id = team[i].getId();
						g.drawImage(pokemonviolet.data.NIC.pokemonIcons.getSubimage( (int) Math.floor((id-1) % 10) * 40, (int) Math.floor((id-1) / 10) * 40, 40, 40), 180, 2 + ((i - 1) * (dimY + 10)), (int) (40 * 1.5f), (int) (40 * 1.5f), null);
					} else {
						int yDisplace = (int) (((int) Math.pow(animFrame - 6, 2)) * 0.5f);
						g.drawImage(animImg.getSubimage(animFrame * 16, 0, 16, 16), 190, 10 + ((i - 1) * (dimY + 10)) + yDisplace, (int) (16 * RESIZE), (int) (16 * RESIZE), null);
						animFrame = animFrame + 1;
						if (animFrame > 11) {
							animFrame = 0;
						}
					}

					g.drawString(team[i].getNameNick(), 235, 33 + ((i - 1) * (dimY + 10)));
					g.drawString("Lv. "+team[i].getLevel(), 250, 53 + ((i - 1) * (dimY + 10)));

					if (team[i].isFainted()) {
						g.setColor(Color.red);
						g.drawString("FNT", 370, 53 + ((i - 1) * (dimY + 10)));
						g.setColor(Color.white);
					}
				}
			}
		}
		
		String state = "";
		if (selection == 6) {
			state = "Active";
		}
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawImage(ImageIO.read(new File(path+"cancel"+state+".png")), ssX - 116, ssY - 40, (int) (52 * RESIZE), (int) (16 * RESIZE), null);
		g.drawString("CANCEL", ssX - 95, ssY - 20);
		
		return tempStitched;
	}

}
