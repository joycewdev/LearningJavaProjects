/* This class contains the features and functions of background
 * @Author: Joyce Wang
 * @Date: May 28 - June 11
 * @Version 1.0
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Background {

	// Declares attributes of background
	Game game;
	int bgWidth;
	static int bgHeight;
	static int x = 0;
	static int y;
	BufferedImage bg;

	// This method is the Background constructor
	// @Param: game
	// @Returns: none
	public Background(Game game) {
		this.game = game;
		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/Resources/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method implements a scrolling background
	// @Param: none
	// @Returns: none
	public void move() {
		for (int i = 0; i < Obstacle.speed; i++)
			x--;
	}

	// This method displays the background
	// @Param: g
	// @Returns: none
	public void draw(Graphics2D g) {
		bgWidth = bg.getTileWidth();
		bgHeight = bg.getTileHeight();
		y = Game.gameHeight - bgHeight;
		for (int i = 0; i < Game.gameWidth - x; i += bgWidth)
			g.drawImage(bg, x + i, y, bgWidth, bgHeight, null);
	}

}
