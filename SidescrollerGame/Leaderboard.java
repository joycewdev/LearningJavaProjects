/* This class contains the features and functions of leader board
 * @Author: Joyce Wang
 * @Date: May 28 - June 11
 * @Version 1.0
 */

import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class Leaderboard {

	// Declares attributes of leader board
	Game game;
	static int lowestHighscore = 0;
	static int[] scores = new int[10];
	static int key;
	static String string;

	// This method is the Leader board constructor
	// @Param: game
	// @Returns: none
	public Leaderboard(Game game) {
		this.game = game;
	}

	// This method adds a new score to the leader board
	// @Param: score
	// @Returns: none
	public void addScore(int score) {
		// If no duplicates are found, the new score will be added to the leader board
		if (findDuplicates())
			scores[9] = score;
		sort();
		saveData();
	}

	// This method allows the user to search for a ranking that matches a score
	// @Param: none
	// @Returns: none
	public static void searchRanking() {
		// Pop-up window containing search feature, prompts for user input
		String find = JOptionPane.showInputDialog("Find a rank by entering a score:");
		Leaderboard.key = Integer.valueOf(find);
		if (Leaderboard.searchRank(scores, scores.length - 1, key) == -1)
			JOptionPane.showMessageDialog(null, "Not recorded on leaderboard, unranked.", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "Rank " + Leaderboard.searchRank(scores, scores.length, key), "Message",
					JOptionPane.INFORMATION_MESSAGE);
	}

	// This method sorts the dataset containing the scores
	// @Param: none
	// @Returns: none
	public void sort() {
		// Bubble sorts data by comparing adjacent values in array
		for (int i = 0; i < scores.length; i++) {
			for (int position = 0; position < scores.length - 1; position++) {
				if (scores[position] < (scores[position + 1])) {
					int temp1 = scores[position];
					scores[position] = scores[position + 1];
					scores[position + 1] = temp1;
				}
			}
			// Sets the minimum score needed to be on the leader board
			lowestHighscore = scores[9];
		}
	}

	// This method saves the score data to a text file
	// @Param: none
	// @Returns: none
	public void saveData() {
		try (PrintWriter writer = new PrintWriter(new FileWriter("gameData.txt"))) {
			for (int i = 0; i < scores.length; i++) {
				writer.printf("%s" + " ", scores[i]);
			}
		} catch (IOException e) {
			System.out.println("\nFailed to save data to file.");
		}
	}

	// This method reads the score data to compose the leader board
	// @Param: none
	// @Returns: none
	public static void readData() {
		String[] data = null;
		try (BufferedReader br = new BufferedReader(new FileReader("gameData.txt"))) {
			String text;
			while ((text = br.readLine()) != null) {
				data = text.split(" ");
				for (int i = 0; i < data.length; i++) {
					scores[i] = Integer.parseInt(data[i]);
				}
			}
			br.close();
		} catch (IOException error) {
			System.out.println("\nFailed to read data from file.");
		}
		lowestHighscore = scores[9];
	}

	// This method is recursively traverses score array to match search
	// @Param: scores, size, key
	// @Returns: searchRank(scores, size - 1, key)
	public static int searchRank(int[] scores, int size, int key) {
		if (size == 0) {
			return -1;
		} else if (scores[size - 1] == key) {
			return (size - 1) + 1;
		}
		return searchRank(scores, size - 1, key);
	}

	// This method traverses the score array to find duplicates
	// Ensures that duplicate values are not added to the leader board
	// @Param: none
	// @Returns: true or false
	public static boolean findDuplicates() {
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] == Game.score)
				return false;
		}
		return true;
	}

	// This method displays the leader board
	// @Param: g
	// @Returns: none
	public void paint(Graphics g) {
		g.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 12));
		g.drawString("A record of 10 scores", 350, 130);
		g.drawString("Only displays the top 3 scores", 350, 145);
		g.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 15));
		g.drawString("Highscore: " + scores[0], 365, 172);
		g.drawString("Silver: " + scores[1], 365, 194);
		g.drawString("Bronze: " + scores[2], 365, 216);
	}

}
