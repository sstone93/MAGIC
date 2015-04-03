package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import controller.ClientController;
import model.Player;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class View extends JFrame {
	
	private ClientController control;
	private JPanel contentPane;
	private JScrollPane scrollPanel;
	//private JScrollPane upgradedText;
	private JTextArea textDisplay;
	private JPanel	blankPanel;

	private BoardPanel boardPanel;
	private CharacterInfoPanel characterInfoPanel;
	private ActivitiesPanel playsPanel;
	private CombatPanel combatPanel;
	private CharacterSelectPanel characterSelectPanel;
	private TargetPanel targetPanel;
	private CharacterDetailsPanel characterDetailsPanel;

	public View(final ClientController control) {
		
		this.control = control;
		setResizable(false);
		setSize(new Dimension(1280, 720));
		getContentPane().setLayout(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Causes netowrk shutdown by clicking the close button on the window
		this.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("Window Was Closed: Triggering Shutdown");
                control.network.stop();
                System.exit(0);
            }
        } );

		//Creates the characterinfo panel and adds it to the view
		characterInfoPanel = new CharacterInfoPanel();
		contentPane.add(characterInfoPanel);
		
		//creates the plays/activities panel
		playsPanel = new ActivitiesPanel(control);
		contentPane.add(playsPanel);
		
		//adds the text box
		textDisplay = new JTextArea();
		//upgradedText =  new JScrollPane (textDisplay, 
				  // JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textDisplay.setBounds(0, 500, 750, 192);
		textDisplay.setLineWrap(true);
		//contentPane.add(upgradedText);
		contentPane.add(textDisplay);
		textDisplay.setEditable(false);
		//upgradedText.setVisible(true);
		
		//adds the scroll bars
		scrollPanel = new JScrollPane();
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setBounds(0, 0, 819, 500);
		contentPane.add(scrollPanel);
		
		//adds the board panel
		boardPanel = new BoardPanel(control);
		scrollPanel.setViewportView(boardPanel);
		boardPanel.setLayout(null);
		
		//adds the blank no actions panel
		blankPanel = new JPanel();
		blankPanel.setBounds(750, 500, 524, 192);
		blankPanel.setBorder(new LineBorder(Color.GRAY));
		blankPanel.setLayout(null);
		JLabel lblNewLabel_1 = new JLabel("Waiting on Server or other Players");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 275, 18);
		blankPanel.add(lblNewLabel_1);
		contentPane.add(blankPanel);
		
		//creates the character details panel
		characterDetailsPanel = new CharacterDetailsPanel(control);
		
		//creates the character select panel
		characterSelectPanel = new CharacterSelectPanel(control);
		contentPane.add(characterSelectPanel);
		
		//creates a new combatpanel and adds it
		combatPanel = new CombatPanel(control);
		contentPane.add(combatPanel);
		
		//creates a new targetpanel, exact same functionality as before, but in a seperate class to reduce clutter
		targetPanel = new TargetPanel(control);
		contentPane.add(targetPanel);
	}
	
	public void updateMessageBox(){
		textDisplay.setText(control.model.getMessages());
	}
	
	public void update(){
		boardPanel.update();
		
		//UPDATES THE PLAYER PANEL
		Player p = control.model.getPlayer();
		if (p != null){
			characterInfoPanel.update(p);
			scrollPanel.getVerticalScrollBar().setValue(p.getLocation().y - 250);
			scrollPanel.getHorizontalScrollBar().setValue(p.getLocation().x - 400);
					
		}
		updateNonBoardGUI(p);
	}
	
	public void updateNonBoardGUI(Player p){

		//UPDATES THE TEXTBOX
		textDisplay.setText(control.model.getMessages());

		//UPDATES THE INPUT PANEL BASED ON IT'S TYPE
		switch(control.state){
		case CHOOSE_CHARACTER:
			characterSelectPanel.update();
			makePanelVisible(characterSelectPanel);
			scrollPanel.setViewportView(characterDetailsPanel);//this must be called AFTER makePanelVisible
			break;
		case CHOOSE_PLAYS:
			playsPanel.update();
			makePanelVisible(playsPanel);
			break;
		case CHOOSE_COMBATMOVES:
			makePanelVisible(combatPanel);
			break;
		case CHOOSE_COMBATTARGET:
			if (p != null){
				targetPanel.update(p);
			}
			makePanelVisible(targetPanel);
			break;
		case NULL:
			makePanelVisible(blankPanel);
		default:
			break;
		}
	}
	
	public void makePanelVisible(JPanel newPanel){
		newPanel.setVisible(true);
		if(newPanel != combatPanel)
			combatPanel.setVisible(false);
		if(newPanel != playsPanel)
			playsPanel.setVisible(false);
		if(newPanel != targetPanel)
			targetPanel.setVisible(false);
		if(newPanel != blankPanel)
			blankPanel.setVisible(false);
		if(newPanel != characterSelectPanel)
			characterSelectPanel.setVisible(false);
		scrollPanel.setViewportView(boardPanel);
	}
	
	public CharacterDetailsPanel getCharacterDetailsPanel() {
		return characterDetailsPanel;
	}
}
