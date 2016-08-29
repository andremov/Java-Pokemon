/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemonviolet;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 *
 * @author Andres
 */
public class Movement extends KeyAdapter{
	
	public Movement() {
	}
	
    public void keyReleased(KeyEvent key) {
		switch (key.getKeyCode()) {
			// UP
			case KeyEvent.VK_UP:
				if (Game.player.getDirection().compareTo("UP")==0){
					Game.player.setDirection("");
				}
			break;
			case KeyEvent.VK_W:
				if (Game.player.getDirection().compareTo("UP")==0){
					Game.player.setDirection("");
				}
			break;
			// DOWN
			case KeyEvent.VK_DOWN:
				if (Game.player.getDirection().compareTo("DOWN")==0){
					Game.player.setDirection("");
				}
			break;
			case KeyEvent.VK_S:
				if (Game.player.getDirection().compareTo("DOWN")==0){
					Game.player.setDirection("");
				}
			break;
			// LEFT
			case KeyEvent.VK_LEFT:
				if (Game.player.getDirection().compareTo("LEFT")==0){
					Game.player.setDirection("");
				}
			break;
			case KeyEvent.VK_A:
				if (Game.player.getDirection().compareTo("LEFT")==0){
					Game.player.setDirection("");
				}
			break;
			// RIGHT
			case KeyEvent.VK_RIGHT:
				if (Game.player.getDirection().compareTo("RIGHT")==0){
					Game.player.setDirection("");
				}
			break;
			case KeyEvent.VK_D:
				if (Game.player.getDirection().compareTo("RIGHT")==0){
					Game.player.setDirection("");
				}
			break;
			// INTERACT
			case KeyEvent.VK_J:
				Game.player.setIsRunning(false);
			break;
		}
    }
  
    public void keyPressed(KeyEvent key) {
		switch (key.getKeyCode()) {
			// UP
			case KeyEvent.VK_UP:
				Game.player.setDirection("UP");
			break;
			case KeyEvent.VK_W:
				Game.player.setDirection("UP");
			break;
			// DOWN
			case KeyEvent.VK_DOWN:
				Game.player.setDirection("DOWN");
			break;
			case KeyEvent.VK_S:
				Game.player.setDirection("DOWN");
			break;
			// LEFT
			case KeyEvent.VK_LEFT:
				Game.player.setDirection("LEFT");
			break;
			case KeyEvent.VK_A:
				Game.player.setDirection("LEFT");
			break;
			// RIGHT
			case KeyEvent.VK_RIGHT:
				Game.player.setDirection("RIGHT");
			break;
			case KeyEvent.VK_D:
				Game.player.setDirection("RIGHT");
			break;
			// INTERACT
			case KeyEvent.VK_J:
				Game.player.setIsRunning(true);
			break;
		}
    }
}