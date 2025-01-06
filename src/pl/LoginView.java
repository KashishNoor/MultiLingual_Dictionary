package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import bl.BLFacade;
import bl.CustomDictionaryBO;
import bl.IBLFacade;
import bl.UserEntryBO;
import bl.WebScrappingBO;
import bl.WordBO;
import dal.AbstractDAOFactory;
import dal.DALFacade;
import dal.ICustomDictionaryDAO;
import dal.IDALFacade;
import dal.IUserEntryDAO;
import dal.IWebScrappingDAO;
import dal.IWordDAO;
import dto.User;

public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton registerButton;
	private IBLFacade iblFacade;
	private JProgressBar progressBar;
	private Timer progressTimer;

	public LoginView(IBLFacade iblFacade) {
		this.iblFacade = iblFacade;
		setTitle("Dictionary Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750, 450);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(240, 248, 255));

		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(38, 166, 154));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" + "<h1>Dictionary</h1>"
				+ "<p>If a word in the dictionary is misspelled,</p><p>How would we know?</p>" + "</div></html>");
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 100)));
		leftPanel.add(infoLabel);

		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel titleLabel = new JLabel("Welcome Back!");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		rightPanel.add(titleLabel, gbc);

		JLabel usernameLabel = new JLabel("Username:");
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		rightPanel.add(usernameLabel, gbc);

		usernameField = new JTextField(20);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		rightPanel.add(usernameField, gbc);

		JLabel passwordLabel = new JLabel("Password:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		rightPanel.add(passwordLabel, gbc);

		passwordField = new JPasswordField(20);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		rightPanel.add(passwordField, gbc);

		loginButton = new JButton("Sign In");
		loginButton.setBackground(new Color(0, 150, 136));
		loginButton.setForeground(Color.WHITE);
		loginButton.setFocusPainted(false);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		rightPanel.add(loginButton, gbc);

		registerButton = new JButton("Register Now!");
		registerButton.setBackground(new Color(63, 81, 181));
		registerButton.setForeground(Color.WHITE);
		registerButton.setFocusPainted(false);
		gbc.gridy = 4;
		rightPanel.add(registerButton, gbc);

		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);
		gbc.gridy = 5;
		rightPanel.add(progressBar, gbc);

		loginButton.addActionListener(e -> login());

		registerButton.addActionListener(e -> openSignUpPage());

		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(rightPanel, BorderLayout.CENTER);
		setContentPane(mainPanel);
		setVisible(true);
		setupProgressTimer();
	}

	private void setupProgressTimer() {
		progressTimer = new Timer(100, e -> {
			int progress = progressBar.getValue() + 5;
			progressBar.setValue(progress);
			if (progress >= 100) {
				progressTimer.stop();
				progressBar.setValue(0);
				new MainDashboardView(iblFacade).setVisible(true);
				dispose();
			}
		});
	}

	private void login() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both username and password", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			if (iblFacade.loginUser(new User(username, password)).equals("Successful")) {
				progressBar.setVisible(true);
				progressTimer.start();
			} else {
				JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void openSignUpPage() {
		new SignUpView(iblFacade).setVisible(true);
		dispose();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				IWordDAO wordDAO = AbstractDAOFactory.getInstance().createWordDao();
				IUserEntryDAO userDAO = AbstractDAOFactory.getInstance().createUserDao();
				IWebScrappingDAO webScrappingDAO = AbstractDAOFactory.getInstance().createWebScrappingDao();
				ICustomDictionaryDAO customDictionaryDAO = AbstractDAOFactory.getInstance().createCustomDictionaryDao();

				IDALFacade idalFacade = new DALFacade(wordDAO, userDAO, webScrappingDAO, customDictionaryDAO);
				IBLFacade iblFacade = new BLFacade(new WordBO(idalFacade), new UserEntryBO(idalFacade),
						new WebScrappingBO(idalFacade), new CustomDictionaryBO(idalFacade));

				new LoginView(iblFacade);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "An error occurred during initialization: " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
