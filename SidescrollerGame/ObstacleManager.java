/* This class contains the features and functions of more obstacles
 * @Author: Joyce Wang
 * @Date: May 28 - June 11
 * @Version 1.0
 */

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class ObstacleManager {

	// Declares attributes of ObstacleManager
	Game game;
	private ArrayList<Obstacle> moreObstacles;
	static int spikeNum;
	boolean reset = false;
	Random random = new Random();

	// This method is the ObstacleManager constructor
	// @Param: none
	// @Returns: none
	public ObstacleManager() {
		moreObstacles = new ArrayList<Obstacle>();
		Obstacle obstacle = new Obstacle(game);
		moreObstacles.add(obstacle);
	}

	// This method is the newObstacle constructor
	// @Param: none
	// @Returns: obstacle
	private Obstacle newObstacle() {
		Obstacle obstacle;
		obstacle = new Obstacle(game);
		Obstacle.x = 1000;
		Sprite.scoreRetrieved = false;
		return obstacle;
	}

	// This method updates to add more obstacles once off screen
	// @Param: none
	// @Returns: none
	public void update() {
		// Each obstacle has the same movement behaviours
		for (Obstacle o : moreObstacles) {
			o.move();
		}
		// Replaces old off screen obstacle with new obstacle to continue cycle
		if (moreObstacles.get(0).offScreen() || reset) {
			// Randomized obstacle size
			if (Obstacle.speed < 3)
				spikeNum = (int) (Math.random() * 2);
			else if (Obstacle.speed >= 3 && Obstacle.speed < 6)
				spikeNum = random.nextInt(2) + 2;
			else if (Obstacle.speed > 6 && Obstacle.speed < 7)
				spikeNum = random.nextInt(3) + 3;
			else
				spikeNum = random.nextInt(4) + 4;
			moreObstacles.remove(0);
			moreObstacles.add(newObstacle());
			reset = false;
		}
	}

	// This method resets spike when game is over
	// @Param: none
	// @Returns: none
	public void gameOver() {
		reset = true;
		update();
	}

	// This method displays more obstacles
	// @Param: g
	// @Returns: none
	public void draw(Graphics2D g) {
		for (Obstacle o : moreObstacles) {
			o.draw(g);
		}
	}

}
