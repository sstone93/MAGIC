package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import controller.ClientController;


@SuppressWarnings("serial")
public class View extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public View(ClientController control) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 739, 581);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane BoardPanel = new JScrollPane();
		BoardPanel.setBounds(10, 11, 471, 304);
		contentPane.add(BoardPanel);
		
		JPanel CharacterInfoPanel = new JPanel();
		CharacterInfoPanel.setBounds(481, 11, 232, 303);
		contentPane.add(CharacterInfoPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 316, 470, 216);
		contentPane.add(scrollPane);
		
		JTextArea TextDisplay = new JTextArea();
		scrollPane.setViewportView(TextDisplay);
		TextDisplay.setEditable(false);
		
		JPanel ButtonPanel1 = new JPanel();
		ButtonPanel1.setBounds(480, 314, 233, 218);
		contentPane.add(ButtonPanel1);
	}
}
