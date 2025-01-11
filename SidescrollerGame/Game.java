/* This program is a platformer-style game
 * @Author: Joyce Wang
 * @Date: May 28 - June 11
 * @Version 1.0
 */

// Imports necessary elements
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel {

	// Declares attributes of program
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int screenWidth = (int) screenSize.getWidth();
	static int screenHeight = (int) screenSize.getHeight();
	static int gameWidth = 900;
	static int gameHeight = 360;
	static int score = 0;
	static int screen = 1;
	Sprite sprite = new Sprite(this);
	Terrain terrain = new Terrain(this);
	Background background = new Background(this);
	Obstacle obstacle = new Obstacle(this);
	ObstacleManager obstacleManager = new ObstacleManager();
	Leaderboard leaderboard = new Leaderboard(this);

	// This method is the Game constructor
	// @Param: none
	// @Returns: none
	public Game() {
		// Adds the necessary KeyListeners for user/keyboard input
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				sprite.keyReleased(e);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				sprite.keyPressed(e);
				Game.keyPressed(e);
			}
		});
		setFocusable(true);
	}

	// This method specifies the function performed when the corresponding key is
	// pressed
	// @Param: e
	// @Returns: none
	protected static void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_S && screen == 1) { // Starts the game
			screen = 2;
		} else if (e.getKeyCode() == KeyEvent.VK_C && screen == 2) { // Changes screen to instructions
			screen = 3;
		} else if (e.getKeyCode() == KeyEvent.VK_R && screen == 4) { // Restarts the game
			score = 0;
			screen = 3;
		} else if (e.getKeyCode() == KeyEvent.VK_L && screen == 4) { // Changes screen to leader board
			screen = 5;
		} else if (e.getKeyCode() == KeyEvent.VK_S && screen == 5) { // Search rank function is enabled in pop-up window
			try {
				Leaderboard.searchRanking();
			} catch (Exception error) {
				JOptionPane.showMessageDialog(null, "Error, input is invalid", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_R && screen == 5) { // Changes screen back to game over
			screen = 4;
		}
	}

	// This method directs movement of game elements
	// @Param: none
	// @Returns: none
	private void move() {
		sprite.move();
		terrain.move();
		background.move();
		obstacleManager.update();
	}

	// This method contains the score system
	// @Param: none
	// @Returns: score
	public int score() {
		// Increments score when obstacle is successfully avoided
		if (Sprite.getXLocation() > (Obstacle.getXLocation() + 60) && sprite.isScoreRetrieved() == false) {
			score++;
			Sprite.scoreRetrieved = true;
		}
		return score;
	}

	// This method contains game over functions including restart and leader board
	// @Param: none
	// @Returns: none
	public void gameOver() {
		// Changes to game over screen
		screen = 4;
		// Adds most recent score to the leader board if it has beat another score
		if (score > Leaderboard.lowestHighscore) {
			Leaderboard.lowestHighscore = score;
			leaderboard.addScore(Leaderboard.lowestHighscore);
		}
		obstacle.gameOver();
		obstacleManager.gameOver();
	}

	// This method controls display content and appearance
	// @Param: g
	// @Returns: none
	public void paint(Graphics g) {
		if (screen == 1) { // Displays the opening/cover screen
			super.paintComponent(g);
			ImageIcon cover = new ImageIcon(getClass().getResource("/Resources/cover.png"));
			cover.paintIcon(this, g, 0, 0);
		} else if (screen == 2) { // Displays the instructions screen
			super.paintComponent(g);
			ImageIcon cover = new ImageIcon(getClass().getResource("/Resources/instructions.png"));
			cover.paintIcon(this, g, 0, 0);
			g.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 15));
			g.setColor(Color.WHITE);
			g.drawString("Avoid the spikes using the up arrow key to jump", 220, 160);
			g.drawString("Speed/difficulty will advance as points are gained", 220, 190);
			g.drawString("Scores are saved in a leaderboard", 220, 220);
		} else if (screen == 3) { // Displays game screen
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			background.draw(g2d);
			terrain.draw(g2d);
			obstacleManager.draw(g2d);
			sprite.paint(g2d);
			Color yellowGreen = new Color(246, 255, 212);
			g2d.setColor(yellowGreen);
			g2d.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 10));
			g2d.drawString("Current Score: " + String.valueOf(score()), 22, 27);
		} else if (screen == 4) { // Displays game over screen
			super.paintComponent(g);
			ImageIcon gameOverbg = new ImageIcon(getClass().getResource("/Resources/gameOver.png"));
			gameOverbg.paintIcon(this, g, 0, 0);
			g.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 20));
			g.setColor(Color.WHITE);
			g.drawString("Personal Score: " + String.valueOf(score()), 350, 180);
		} else if (screen == 5) { // Displays leader board screen
			super.paintComponent(g);
			ImageIcon leaderboardbg = new ImageIcon(getClass().getResource("/Resources/leaderboard.png"));
			leaderboardbg.paintIcon(this, g, 0, 0);
			g.setColor(Color.WHITE);
			leaderboard.paint(g);
		}
	}

	// Launches the application
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("Trailblazer - Platformer Game by Joyce Wang");
		Game game = new Game();
		frame.add(game);
		Container c = frame.getContentPane();
		c.setPreferredSize(new Dimension(gameWidth, gameHeight));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Leaderboard.readData();

		// Attempts to play music on loop
		try {
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;
			Clip clip;

			stream = AudioSystem.getAudioInputStream(new File("music.wav"));
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (Exception e) {
			System.out.println("Audio error");
		}

		while (true) {
			if (screen == 3)
				game.move();
			game.repaint();
			Thread.sleep(10);
		}
	}
}