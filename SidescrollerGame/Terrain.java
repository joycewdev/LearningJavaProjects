/* This class contains the features and functions of terrain
 * @Author: Joyce Wang
 * @Date: May 28 - June 11
 * @Version 1.0
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Terrain {

	// Declares attributes of terrain
	Game game;
	int groundWidth;
	static int groundHeight;
	static int x = 0;
	static int y;
	BufferedImage ground;

	// This method is the Terrain constructor
	// @Param: game
	// @Returns: none
	public Terrain(Game game) {
		this.game = game;
		try {
			ground = ImageIO.read(getClass().getResourceAsStream("/Resources/groundTile.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method displays the terrain
	// @Param: g
	// @Returns: none
	public void draw(Graphics2D g) {
		groundWidth = ground.getTileWidth() * 3;
		groundHeight = ground.getTileHeight() * 4;
		y = Game.gameHeight - groundHeight;
		for (int i = 0; i < Game.gameWidth - x; i += groundWidth)
			g.drawImage(ground, x + i, y, groundWidth, groundHeight, null);
	}

	// This method is the terrain's hit box
	// @Param: none
	// @Returns: Rectangle(x, y + 14, Game.gameWidth - x, groundHeight - 14)
	public Rectangle getBounds() {
		return new Rectangle(x, y + 14, Game.gameWidth - x, groundHeight - 14);
	}

	// This method implements scrolling terrain
	// @Param: none
	// @Returns: none
	public void move() {
		for (int i = 0; i < Obstacle.speed; i++)
			x--;
	}

}
