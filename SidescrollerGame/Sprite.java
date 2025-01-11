/* This class contains the features and functions of sprite
 * @Author: Joyce Wang
 * @Date: May 28 - June 11
 * @Version 1.0
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Sprite {

	// Declares attributes of sprite
	Game game;
	static int x = 100;
	int y = 290;
	int width = 36;
	int height = 45;
	double xSpeed = 0;
	double ySpeed = 0;
	boolean up;
	boolean inAir;
	static boolean scoreRetrieved;
	BufferedImage sprite;
	BufferedImage[] runAnimation;
	int animationTime, animationIndex, animationSpeed = 8;

	// This method is the Sprite constructor
	// @Param: game
	// @Returns: none
	public Sprite(Game game) {
		this.game = game;
		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/Resources/spriteRun.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method controls jump movement when key is pressed
	// @Param: e
	// @Returns: none
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = true;
	}

	// This method controls jump movement when key is released
	// @Param: e
	// @Returns: none
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = false;
	}

	// This method controls sprite movement
	// @Param: none
	// @Returns: none
	public void move() {
		if (collision()) // Game is over when sprite has hit an obstacle
			game.gameOver();
		if (!contactWithGround()) // Gravity is in effect when sprite has jumped
			ySpeed += 0.3;
		else { // Disables gravity and double jumps
			ySpeed = 0;
			inAir = false;
			if (up && inAir == false) {
				ySpeed = -6;
				inAir = true;
			}
		}
		// Boundaries for movement
		if (y + ySpeed > 0 && y + ySpeed < game.getHeight())
			y += ySpeed;
	}

	// This method gets the sprite's x coordinate location
	// @Param: none
	// @Returns: x
	public static int getXLocation() {
		return x;
	}

	// This method determines whether sprite is on the ground
	// @Param: none
	// @Returns: game.terrain.getBounds().intersects(getBounds())
	private boolean contactWithGround() {
		return game.terrain.getBounds().intersects(getBounds());
	}

	// This method determines whether sprite has hit an obstacle
	// @Param: none
	// @Returns: game.obstacle.getBounds().intersects(getBounds())
	public boolean collision() {
		return game.obstacle.getBounds().intersects(getBounds());
	}

	// This method contains the sprite's hit box
	// @Param: none
	// @Returns: new Rectangle(x, y, width, height)
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	// This method determines whether score has already been retrieved
	// @Param: none
	// @Returns: scoreRetrieved
	public boolean isScoreRetrieved() {
		if (collision())
			scoreRetrieved = false;
		return scoreRetrieved;
	}

	// This method is the structure for sprite's animation
	// @Param: none
	// @Returns: none
	private void animation() {
		// Loops through a selection of images
		runAnimation = new BufferedImage[8];
		for (int i = 0; i < runAnimation.length; i++)
			runAnimation[i] = sprite.getSubimage(i * 12, 0, 12, 15);
	}

	// This method updates the sprite's animation
	// @Param: none
	// @Returns: none
	private void updateAnimation() {
		// Increments animation conditions required for the loop
		animationTime++;
		if (animationTime >= animationSpeed) {
			animationTime = 0;
			animationIndex++;
			if (animationIndex >= runAnimation.length)
				animationIndex = 0;
		}
	}

	// This method displays the sprite
	// @Param: g
	// @Returns: none
	public void paint(Graphics2D g) {
		animation();
		updateAnimation();
		g.drawImage(runAnimation[animationIndex], x, y, width, height, null);
	}

}
