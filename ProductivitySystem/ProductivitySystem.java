/* This program is a study/productivity system
 * @Author: Joyce Wang
 * @Date: May 30 - June 12
 * @Version 1.0
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.io.*;
import javax.swing.JTextArea;

public class WangJFinalSummative {

	// Declares features of the program
	private static JFrame frame;
	private JPanel panelNav;
	private JPanel panelNotes;
	private JPanel panelList;

	private JLabel lblTitle;
	private JLabel lblDate;
	private JLabel lblToDoListTitle;
	private JLabel lblName;
	private JLabel lblImage;

	private JButton btnNewNote;
	private JButton btnSearchNote;
	private JButton btnSaveAndExit;
	private JButton btnAddTask;

	private static JTextPane textPaneNotes;
	private static JTextArea textAreaTip;

	private static JTextField textFieldSearchNote;
	private static JTextField textFieldNoteTitle;
	private static JTextField textFieldTask;

	private static JList<Object> listTasks;
	private static JList<Object> listCheck;
	private static DefaultListModel<Object> tasks;
	private static DefaultListModel<Object> check;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WangJFinalSummative window = new WangJFinalSummative();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	// Declares and initializes the following variables
	int listNum = -1;
	static int searchNoteVerify = 0;
	static int noteNum = 0;
	static boolean noteTitleDisabled = false;
	static String[] noteTitles = new String[100];
	static String[] randomTip = { "Have a designated study space", "Take breaks", "Review, practice, question, repeat",
			"Create a plan and set goals", "Work in progress :)" };

	public WangJFinalSummative() throws IOException {
		initialize();
		readFiles();
		randomTipGenerator();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		// Creates, arranges, and displays the features and functions on screen

		frame = new JFrame();
		frame.setBounds(100, 100, 990, 580);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panelNav = new JPanel();
		panelNav.setBackground(new Color(62, 45, 111));
		panelNav.setBounds(0, 0, 187, 543);
		frame.getContentPane().add(panelNav);
		panelNav.setLayout(null);

		lblImage = new JLabel("");
		lblImage.setBackground(new Color(255, 255, 255));
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setIcon(new ImageIcon(WangJFinalSummative.class.getResource("/imgs/StudyKoala.jpg")));
		lblImage.setBounds(20, 255, 148, 120);
		lblImage.setOpaque(true);
		panelNav.add(lblImage);

		lblTitle = new JLabel("STUDY SYSTEM");
		lblTitle.setBounds(10, 21, 167, 47);
		panelNav.add(lblTitle);
		lblTitle.setForeground(new Color(255, 245, 238));
		lblTitle.setFont(new Font("Tw Cen MT", Font.BOLD, 24));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

		btnNewNote = new JButton(" New Note");
		btnNewNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newNote();
			}
		});
		btnNewNote.setBackground(new Color(255, 250, 250));
		btnNewNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		btnNewNote.setIcon(new ImageIcon(WangJFinalSummative.class.getResource("/imgs/NewNote.png")));
		btnNewNote.setBounds(20, 79, 148, 47);
		btnNewNote.setFocusPainted(false);
		panelNav.add(btnNewNote);

		btnSearchNote = new JButton(" Search Note");
		btnSearchNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					searchNote();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame, "Note does not exist.");
				}
			}
		});
		btnSearchNote.setIcon(new ImageIcon(WangJFinalSummative.class.getResource("/imgs/SearchNote.png")));
		btnSearchNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		btnSearchNote.setFocusPainted(false);
		btnSearchNote.setBackground(new Color(255, 250, 250));
		btnSearchNote.setBounds(20, 145, 148, 47);
		panelNav.add(btnSearchNote);

		btnSaveAndExit = new JButton(" Save & Exit");
		btnSaveAndExit.setForeground(new Color(0, 128, 128));
		btnSaveAndExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAndExit();
			}
		});
		btnSaveAndExit.setIcon(new ImageIcon(WangJFinalSummative.class.getResource("/imgs/Save.png")));
		btnSaveAndExit.setFont(new Font("Trebuchet MS", Font.ITALIC, 14));
		btnSaveAndExit.setFocusPainted(false);
		btnSaveAndExit.setBackground(new Color(254, 254, 235));
		btnSaveAndExit.setBounds(20, 470, 148, 47);
		panelNav.add(btnSaveAndExit);

		panelNotes = new JPanel();
		panelNotes.setBackground(new Color(215, 223, 236));
		panelNotes.setBounds(438, 0, 538, 543);
		frame.getContentPane().add(panelNotes);
		panelNotes.setLayout(null);

		lblDate = new JLabel("New label");
		lblDate.setForeground(SystemColor.inactiveCaption);
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		lblDate.setText("" + java.time.LocalDate.now());
		lblDate.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblDate.setOpaque(true);
		lblDate.setBackground(new Color(255, 255, 255));
		lblDate.setBounds(16, 21, 121, 30);
		panelNotes.add(lblDate);

		textFieldNoteTitle = new JTextField();
		textFieldNoteTitle.setBackground(new Color(255, 255, 255));
		textFieldNoteTitle.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldNoteTitle.setText("Untitled Note");
		textFieldNoteTitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		textFieldNoteTitle.setBounds(136, 21, 387, 30);
		panelNotes.add(textFieldNoteTitle);
		textFieldNoteTitle.setColumns(10);
		textFieldNoteTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		textPaneNotes = new JTextPane();
		textPaneNotes.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		textPaneNotes.setMargin(new Insets(5, 5, 5, 5));
		textPaneNotes.setBounds(16, 50, 507, 469);
		panelNotes.add(textPaneNotes);

		panelList = new JPanel();
		panelList.setBackground(new Color(215, 223, 236));
		panelList.setBounds(185, 0, 258, 543);
		frame.getContentPane().add(panelList);
		panelList.setLayout(null);

		tasks = new DefaultListModel<>();
		check = new DefaultListModel<>();

		btnAddTask = new JButton("");
		btnAddTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTask();
			}
		});
		btnAddTask.setBackground(new Color(255, 255, 255));
		btnAddTask.setIcon(new ImageIcon(WangJFinalSummative.class.getResource("/imgs/AddTask.png")));
		btnAddTask.setBounds(220, 60, 32, 29);
		panelList.add(btnAddTask);
		btnAddTask.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		listCheck = new JList<>(check);
		listCheck.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		listCheck.setLocation(16, 100);
		listCheck.setFixedCellHeight(18);
		panelList.add(listCheck);
		listCheck.setSize(25, 419);
		listCheck.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				doneTask();
			}
		});

		textFieldTask = new JTextField();
		textFieldTask.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldTask.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		textFieldTask.setBounds(16, 60, 206, 29);
		panelList.add(textFieldTask);
		textFieldTask.setColumns(10);
		textFieldTask.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		listTasks = new JList<>(tasks);
		listTasks.setForeground(SystemColor.inactiveCaption);
		listTasks.setToolTipText("");
		listTasks.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		listTasks.setLocation(39, 100);
		listTasks.setFixedCellHeight(18);
		listTasks.setEnabled(false);
		panelList.add(listTasks);
		listTasks.setSize(213, 419);

		textFieldSearchNote = new JTextField();
		textFieldSearchNote.setBounds(20, 209, 148, 25);
		textFieldSearchNote.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		textFieldSearchNote.setVisible(false);
		panelNav.add(textFieldSearchNote);
		textFieldSearchNote.setColumns(10);

		textAreaTip = new JTextArea();
		textAreaTip.setBounds(20, 395, 148, 53);
		panelNav.add(textAreaTip);
		textAreaTip.setEnabled(false);
		textAreaTip.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		textAreaTip.setDisabledTextColor(Color.BLACK);
		textAreaTip.setMargin(new Insets(5, 5, 5, 5));
		textAreaTip.setLineWrap(true);
		textAreaTip.setWrapStyleWord(true);

		lblName = new JLabel("Joyce W - ICS3U1");
		lblName.setFont(new Font("Trebuchet MS", Font.PLAIN, 6));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setForeground(new Color(102, 102, 153));
		lblName.setBounds(20, 522, 148, 14);
		panelNav.add(lblName);

		lblToDoListTitle = new JLabel("To-Do List");
		lblToDoListTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblToDoListTitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblToDoListTitle.setBounds(16, 21, 236, 29);
		panelList.add(lblToDoListTitle);

	}

	// This method will generate a random tip each time it is run
	// @Param: none
	// @Returns: none
	private static void randomTipGenerator() {
		int randomTipNum = (int) (Math.random() * 5);
		if (randomTipNum == 4)
			textAreaTip.setText(randomTip[randomTipNum]);
		else
			textAreaTip.setText("Tip: " + randomTip[randomTipNum]);
	}

	// This method will search for a saved note and display it
	// @Param: none
	// @Returns: none
	private static void searchNote() throws IOException {
		if (searchNoteVerify == 0)
			textFieldSearchNote.setVisible(true);
		else {
			File txtFile = new File(textFieldSearchNote.getText() + ".txt");
			BufferedReader br = new BufferedReader(new FileReader(txtFile));
			String textNotes;
			while ((textNotes = br.readLine()) != null) {
				String[] splitArray = textNotes.split("\n");
				for (int i = 0; i < splitArray.length; i++) {
					try {
						Document doc = textPaneNotes.getDocument();
						doc.insertString(doc.getLength(), "\n" + splitArray[i], null);
					} catch (BadLocationException exc) {
						JOptionPane.showMessageDialog(frame, "Error.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			br.close();
			textFieldNoteTitle.setText(textFieldSearchNote.getText());
			textFieldNoteTitle.setEnabled(false);
			textPaneNotes.setEnabled(false);
			noteTitleDisabled = true;
		}
		searchNoteVerify++;
	}

	// This method will save the files and terminate the program
	// @Param: none
	// @Returns: none
	private static void saveAndExit() {
		if (JOptionPane.showConfirmDialog(null, "Save Changes and Confirm Exit?", "Save & Exit",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			try (PrintWriter writer = new PrintWriter(new FileWriter("To-Do List.txt"))) {
				for (int i = 0; i < listTasks.getModel().getSize(); i++) {
					writer.printf("%s%n", listTasks.getModel().getElementAt(i));
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Error.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			int result = -1;
			int nullIndex = 0;
			for (int i = 0; i < noteTitles.length; i++) {
				if (noteTitles[i] == null)
					nullIndex = i;
				if (textFieldNoteTitle.getText().equals(noteTitles[i]) && noteTitleDisabled != true) {
					JOptionPane.showMessageDialog(frame, "Note name already exists. Rename before saving.");
					result = i;
				} else if (i == noteTitles.length - 1 && result == -1) {
					if (noteTitleDisabled != true) {
						noteTitles[nullIndex] = textFieldNoteTitle.getText();
						try (PrintWriter writer = new PrintWriter(
								new FileWriter(textFieldNoteTitle.getText() + ".txt"))) {
							writer.printf(textPaneNotes.getText());
							textFieldNoteTitle.setEnabled(false);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(frame, "Error.", "Error", JOptionPane.ERROR_MESSAGE);
						}
						try (PrintWriter writer = new PrintWriter(new FileWriter("Note Titles.txt"))) {
							for (int j = 0; j < noteTitles.length; j++)
								if (noteTitles[j] != null) {
									writer.printf("%s%n", noteTitles[j]);
								}
						} catch (IOException e) {
							JOptionPane.showMessageDialog(frame, "Error.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					System.exit(0);
				}
			}
		}
		;
	}

	// This method will create a new note
	// @Param: none
	// @Returns: none
	private static void newNote() {
		noteNum++;
		noteTitleDisabled = false;
		textFieldNoteTitle.setText("New Note " + noteNum);
		textFieldNoteTitle.setEnabled(true);
		textPaneNotes.setEnabled(true);
		textPaneNotes.setText("");
	}

	// This method will add a task to the to-do list
	// @Param: none
	// @Returns: none
	private static void addTask() {
		if (check.size() <= 19) {
			tasks.addElement(" " + textFieldTask.getText());
			check.addElement(new ImageIcon(WangJFinalSummative.class.getResource("/imgs/Done.png")));
			textFieldTask.setText("");
		} else {
			JOptionPane.showMessageDialog(frame, "List is full. Cannot add more tasks.");
		}
	}

	// This method will remove a task from the to-do list
	// @Param: none
	// @Returns: none
	private static void doneTask() {
		int SelectedTask = listCheck.getSelectedIndex();
		if (SelectedTask != -1) {
			check.remove(SelectedTask);
			tasks.remove(SelectedTask);
		}
	}

	// This method will read the necessary txt files
	// @Param: none
	// @Returns: none
	private static void readFiles() throws IOException {
		File txtFileNoteTitles = new File("Note Titles.txt");
		BufferedReader br1 = new BufferedReader(new FileReader(txtFileNoteTitles));
		String textNoteTitles;
		int indexCounter1 = 0;
		while ((textNoteTitles = br1.readLine()) != null) {
			String[] splitArray = textNoteTitles.split("\n");
			for (int i = 0; i < splitArray.length; i++) {
				noteTitles[indexCounter1] = splitArray[i];
				indexCounter1++;
			}
		}
		br1.close();
		File txtFileToDoList = new File("To-Do List.txt");
		BufferedReader br2 = new BufferedReader(new FileReader(txtFileToDoList));
		String textToDoList;
		while ((textToDoList = br2.readLine()) != null) {
			String[] splitArray = textToDoList.split("\n");
			for (int i = 0; i < splitArray.length; i++) {
				tasks.addElement(splitArray[i]);
				check.addElement(new ImageIcon(WangJFinalSummative.class.getResource("/imgs/Done.png")));
			}
		}
		br2.close();
	}
}
