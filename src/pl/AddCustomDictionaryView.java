package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bl.IBLFacade;

public class AddCustomDictionaryView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField dictionaryNameField;
	private String filePath;
	private JButton browseButton, uploadButton;
	private IBLFacade iblFacade;

	public AddCustomDictionaryView(IBLFacade iblFacade) {
		this.iblFacade = iblFacade;
		setTitle("Dictionary Management System - Add Custom Dictionary");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 500);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(240, 248, 255));
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(38, 166, 154));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension(250, getHeight()));

		JLabel descriptionLabel = new JLabel("<html><div style='text-align: center;'>" + "<h1>Add Dictionary</h1>"
				+ "<p>.   Upload Custom Dictionary to Enhance System. </p>" + "</div></html>");
		descriptionLabel.setForeground(Color.WHITE);
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		leftPanel.add(descriptionLabel);
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel dictionaryNameLabel = new JLabel("Dictionary Name:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		rightPanel.add(dictionaryNameLabel, gbc);

		dictionaryNameField = new JTextField(20);
		gbc.gridx = 1;
		rightPanel.add(dictionaryNameField, gbc);

		JLabel filePathLabel = new JLabel("File Path:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		rightPanel.add(filePathLabel, gbc);

		browseButton = new JButton("Browse");
		browseButton.setBackground(new Color(0, 150, 136));
		browseButton.setForeground(Color.WHITE);
		browseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		browseButton.setFocusPainted(false);
		browseButton.setPreferredSize(new Dimension(120, 30));
		gbc.gridx = 1;
		rightPanel.add(browseButton, gbc);

		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(AddCustomDictionaryView.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					filePath = fileChooser.getSelectedFile().getAbsolutePath();
				}
			}
		});
		uploadButton = new JButton("Upload");
		uploadButton.setBackground(new Color(63, 81, 181));
		uploadButton.setForeground(Color.WHITE);
		uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		uploadButton.setFocusPainted(false);
		uploadButton.setPreferredSize(new Dimension(150, 40));
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		rightPanel.add(uploadButton, gbc);
		uploadButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String dictionaryName = dictionaryNameField.getText();
		        if (dictionaryName.isEmpty() || filePath == null || filePath.isEmpty()) {
		            JOptionPane.showMessageDialog(AddCustomDictionaryView.this, "Please fill in all fields!", "Error",
		                    JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        // Show a loading dialog
		        JOptionPane loadingDialog = new JOptionPane("Uploading dictionary, please wait...", JOptionPane.INFORMATION_MESSAGE);
		        JDialog dialog = loadingDialog.createDialog(AddCustomDictionaryView.this, "Processing");
		        dialog.setModal(false); 
		        dialog.setVisible(true);

		     
		        Thread uploadThread = new Thread(() -> {
		            try {
		                
		                boolean result = iblFacade.addCustomDictionary(dictionaryName, filePath);

		              
		                javax.swing.SwingUtilities.invokeLater(() -> {
		                    dialog.dispose(); 
		                    if (result) {
		                        JOptionPane.showMessageDialog(AddCustomDictionaryView.this,
		                                "Custom dictionary added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		                    } else {
		                        JOptionPane.showMessageDialog(AddCustomDictionaryView.this,
		                                "Failed to add custom dictionary. Please try again.", "Error",
		                                JOptionPane.ERROR_MESSAGE);
		                    }
		                });
		            } catch (Exception ex) {
		                ex.printStackTrace();
		                javax.swing.SwingUtilities.invokeLater(() -> {
		                    dialog.dispose(); 
		                    JOptionPane.showMessageDialog(AddCustomDictionaryView.this,
		                            "An error occurred while uploading the dictionary: " + ex.getMessage(), "Error",
		                            JOptionPane.ERROR_MESSAGE);
		                });
		            }
		        });

		        
		        uploadThread.start();
		    }
		});

		JLabel backToDashboardLabel = new JLabel("<html><u>Back to Dashboard</u></html>");
		backToDashboardLabel.setForeground(new Color(0, 120, 215));
		backToDashboardLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gbc.gridy = 4;
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		rightPanel.add(backToDashboardLabel, gbc);

		backToDashboardLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				new MainDashboardView(iblFacade);
			}
		});
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(rightPanel, BorderLayout.CENTER);

		setContentPane(mainPanel);
		setVisible(true);
	}

}
