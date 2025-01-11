/* This program is a weather application
 * @Author: Joyce W
 * @Date: May 3, 2024
 * @Version 1.0
 */

// Imports necessary items
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import org.json.simple.JSONObject;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WangJGUITaskWeatherApp {

	// Declares features of program
	private JFrame frame;
	private JTextField textFieldSearch;
	private JButton btnSearch;
	private JLabel lblTitle;
	private JLabel lblWeatherIcon;
	private JLabel lblTemperature;
	private JLabel lblHumidityIcon;
	private JLabel lblHumidityTitle;
	private JLabel lblWindTitle;
	private JLabel lblWeather;
	private JLabel lblHumidity;
	private JLabel lblWindspeed;
	private JLabel lblWindspeedIcon;
	private JLabel lblBackground;
	private JSONObject weatherData;

	// Launch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WangJGUITaskWeatherApp window = new WangJGUITaskWeatherApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the application
	public WangJGUITaskWeatherApp() {
		initialize();
	}

	// Initialize the contents of the frame
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 355, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textFieldSearch = new JTextField();
		textFieldSearch.setEditable(false);
		textFieldSearch.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		textFieldSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textFieldSearch.setEditable(true);
				textFieldSearch.setText("");
			}
		});
		textFieldSearch.setBackground(SystemColor.inactiveCaptionBorder);
		textFieldSearch.setText("Enter Location");
		textFieldSearch.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 16));
		textFieldSearch.setBounds(28, 70, 231, 31);
		frame.getContentPane().add(textFieldSearch);
		textFieldSearch.setColumns(10);

		lblHumidityIcon = new JLabel("");
		lblHumidityIcon.setIcon(new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/humid.png")));
		lblHumidityIcon.setBounds(44, 399, 35, 44);
		frame.getContentPane().add(lblHumidityIcon);

		lblWindspeedIcon = new JLabel("");
		lblWindspeedIcon.setIcon(new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/wind.png")));
		lblWindspeedIcon.setBounds(170, 399, 35, 44);
		frame.getContentPane().add(lblWindspeedIcon);

		lblWindspeed = new JLabel("___ km/h");
		lblWindspeed.setBounds(197, 412, 109, 31);
		frame.getContentPane().add(lblWindspeed);
		lblWindspeed.setForeground(new Color(255, 255, 255));
		lblWindspeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblWindspeed.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 18));

		lblWindTitle = new JLabel("Windspeed");
		lblWindTitle.setBounds(202, 384, 103, 31);
		frame.getContentPane().add(lblWindTitle);
		lblWindTitle.setForeground(new Color(255, 255, 255));
		lblWindTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblWindTitle.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));

		lblHumidity = new JLabel("___%");
		lblHumidity.setBounds(53, 412, 109, 31);
		frame.getContentPane().add(lblHumidity);
		lblHumidity.setForeground(new Color(255, 255, 255));
		lblHumidity.setHorizontalAlignment(SwingConstants.CENTER);
		lblHumidity.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));

		lblHumidityTitle = new JLabel("Humidity");
		lblHumidityTitle.setBounds(63, 384, 87, 29);
		frame.getContentPane().add(lblHumidityTitle);
		lblHumidityTitle.setForeground(new Color(255, 255, 255));
		lblHumidityTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblHumidityTitle.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		lblTitle = new JLabel("Hourly Weather " + timeStamp);
		lblTitle.setForeground(new Color(255, 255, 255));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 22));
		lblTitle.setBounds(10, 11, 321, 44);
		frame.getContentPane().add(lblTitle);

		lblWeatherIcon = new JLabel("");
		lblWeatherIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeatherIcon.setBounds(49, 130, 241, 146);
		frame.getContentPane().add(lblWeatherIcon);

		lblTemperature = new JLabel("___ °C");
		lblTemperature.setForeground(new Color(255, 255, 255));
		lblTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		lblTemperature.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 40));
		lblTemperature.setBounds(51, 283, 239, 44);
		frame.getContentPane().add(lblTemperature);

		lblWeather = new JLabel("Sunny");
		lblWeather.setForeground(new Color(255, 255, 255));
		lblWeather.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeather.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		lblWeather.setBounds(49, 328, 241, 25);
		frame.getContentPane().add(lblWeather);

		btnSearch = new JButton("");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Gets user input and validates it
				String userInput = textFieldSearch.getText();
				if (WangJWeatherAppAPICode.getWeatherData(userInput) == null) {
					JOptionPane.showMessageDialog(frame, "Input is invalid");
				} else {
					// Calls class with the API functions
					weatherData = WangJWeatherAppAPICode.getWeatherData(userInput);
					// Displays the corresponding information based on the return value
					String weatherCondition = (String) weatherData.get("weather_condition");
					if (weatherCondition == "Sunny")
						lblWeatherIcon
								.setIcon(new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/sun.png")));
					else if (weatherCondition == "Partly Cloudy")
						lblWeatherIcon.setIcon(
								new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/clouds-and-sun.png")));
					else if (weatherCondition == "Cloudy")
						lblWeatherIcon
								.setIcon(new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/clouds.png")));
					else if (weatherCondition == "Raining")
						lblWeatherIcon
								.setIcon(new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/rain.png")));
					else if (weatherCondition == "Snowing")
						lblWeatherIcon
								.setIcon(new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/snow.png")));
					else if (weatherCondition == "Thunderstorm")
						lblWeatherIcon.setIcon(
								new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/thunderstorm.png")));

					lblWeather.setText(weatherCondition);

					double temperature = (double) weatherData.get("temperature");
					lblTemperature.setText(temperature + " °C");

					long humidity = (long) weatherData.get("humidity");
					lblHumidity.setText(humidity + "%");

					double windspeed = (double) weatherData.get("windspeed");
					lblWindspeed.setText(windspeed + " km/h");
				}
			}
		});
		btnSearch.setIcon(new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/search.png")));
		btnSearch.setBounds(269, 70, 53, 31);
		btnSearch.setFocusPainted(false);
		btnSearch.setBackground(new Color(255, 250, 250));
		frame.getContentPane().add(btnSearch);

		lblBackground = new JLabel("New label");
		lblBackground.setVerticalAlignment(SwingConstants.TOP);
		lblBackground.setIcon(new ImageIcon(WangJGUITaskWeatherApp.class.getResource("/images/background.png")));
		lblBackground.setBounds(0, 0, 341, 475);
		frame.getContentPane().add(lblBackground);
	}

}
