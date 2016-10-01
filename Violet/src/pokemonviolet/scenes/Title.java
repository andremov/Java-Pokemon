/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
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

/**
 *
 * @author Andres
 */
public class Title extends Scene{
	
	private int blackBarY, logoY, growlitheX, arcanineX, startDisplay;
	private final int finalBlackBarY, finalLogoY, finalArcanineX, finalGrowlitheX;
	private boolean ready;
	
	public Title(Handler main) {
		super(main, "TITLE", true);
		
		ready = false;
		startDisplay = 6;
		
		logoY=-400;
		finalLogoY=00;
		
		blackBarY=ssY+50;
		finalBlackBarY=ssY-50;
		
		growlitheX=ssX+120;
		finalGrowlitheX=ssX-140;
		
		arcanineX=ssX+60;
		finalArcanineX=ssX-200;
		
		logoY=-100;
	}

	@Override
	public BufferedImage getDisplay() {
		BufferedImage display = new BufferedImage( ssX, ssY, BufferedImage.TYPE_INT_RGB);
		Graphics g = display.getGraphics();
		
		try {
			g.drawImage(ImageIO.read(new File("assets/title/background.png")), 0,0,null);
		} catch (IOException ex) {
		}
		
		ready=true;
		
		if (blackBarY!=finalBlackBarY){
			blackBarY=blackBarY-20;
			if (ready){
				ready=false;
			}
		}
		g.setColor(Color.black);
		g.fillRect(0, blackBarY, ssX, ssY);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Andrés Movilla",20, blackBarY+40);
		
		try {
			if (arcanineX!=finalArcanineX){
				arcanineX=arcanineX-20;
				if (ready){
					ready=false;
				}
			}
			g.drawImage(ImageIO.read(new File("assets/pokemon/59ffn.png")), arcanineX, ssY-220, 160,160,null);
		} catch (IOException ex) {
		}
		
		try {
			if (arcanineX==finalArcanineX){
				if (growlitheX!=finalGrowlitheX){
					growlitheX=growlitheX-20;
					if (ready){
						ready=false;
					}
				}
			}
			g.drawImage(ImageIO.read(new File("assets/pokemon/58ffn.png")), growlitheX, ssY-170, 160,160,null);
		} catch (IOException ex) {
		}
		
		try {
			if (logoY!=finalLogoY){
				logoY=logoY+20;
				if (ready){
					ready=false;
				}
			}
			g.drawImage(ImageIO.read(new File("assets/title/violetLogo.png")), (ssX/2)-40-(int)((int)(1363/4)/2), logoY, (int)(1363/4),(int)(786/4),null);
		} catch (IOException ex) {
		}
		
		if (ready){
			startDisplay = startDisplay-1;
			if (startDisplay<2){
				if (startDisplay==0){
					startDisplay=6;
				}
				g.setFont(new Font("Arial", Font.BOLD, 25));
				g.drawString("Press Start", 35, 250);
			}
		}
		
		return display;
	}

	@Override
	public void receiveKeyAction(String action, String state) {
		if (ready){
			if (action.compareTo("START")==0 && state.compareTo("RELEASE")==0){
				this.dispose();
			}
		}
	}

	@Override
	protected void accept() {
	
	}

	@Override
	protected void cancel() {
	
	}

	@Override
	protected void dispose() {
		main.gameState.remove(main.gameState.size()-1);
		main.gameState.add(new GenderChoose(main));
	}
	
}
