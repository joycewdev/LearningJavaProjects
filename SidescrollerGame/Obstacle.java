/* This class contains the features and functions of obstacle
 * @Author: Joyce Wang
 * @Date: May 28 - June 11
 * @Version 1.0
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Obstacle {

	// Declares attributes of obstacle
	Game game;
	static int x = 800;
	static int y;
	static int width = 15;
	int height = 18;
	int spikeHeight;
	int spikeWidth;
	static int speed;
	BufferedImage spikes;

	// This method is the Obstacle constructor
	// @Param: game
	// @Returns: none
	public Obstacle(Game game) {
		this.game = game;
		try {
			spikes = ImageIO.read(getClass().getResourceAsStream("/Resources/spikes.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method controls obstacle movement across screen
	// @Param: none
	// @Returns: none
	public void move() {
		// Sets speed according to game progress
		if (Game.score <= 0 && Game.score <= 4)
			speed = 2;
		else if (Game.score % 4 == 0 && speed <= 8 && Sprite.x == x)
			speed++;
		for (int i = 0; i < speed; i++)
			x--;
	}

	// This method is the obstacle's hit box
	// @Param: none
	// @Returns: Rectangle(x, y, width + width * ObstacleManager.spikeNum, height)
	public Rectangle getBounds() {
		return new Rectangle(x, y, width + width * ObstacleManager.spikeNum, height);
	}

	// This method determines whether obstacle is off screen
	// @Param: none
	// @Returns: (x + width < 0)
	public static boolean offScreen() {
		return (x + width < 0);
	}

	// This method gets the obstacle's x coordinate location
	// @Param: none
	// @Returns: x
	public static int getXLocation() {
		return x;
	}

	// This method sets game over for obstacle, resets location
	// @Param: none
	// @Returns: none
	public void gameOver() {
		x = 800;
		speed = 2;
	}

	// This method displays the obstacle
	// @Param: g
	// @Returns: none
	public void draw(Graphics2D g) {
		spikeWidth = spikes.getTileWidth() * ObstacleManager.spikeNum;
		spikeHeight = spikes.getTileHeight();
		y = 322;
		for (int i = 0; i <= ObstacleManager.spikeNum; i++)
			g.drawImage(spikes, x + i * width, y, width, height, null);
	}

}
