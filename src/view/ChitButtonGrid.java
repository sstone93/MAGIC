package view;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import controller.ServerController;

@SuppressWarnings("serial")
public class ChitButtonGrid  extends JPanel{
	
	ServerController control;
	JButton submit;
	
	JLabel[] chitNameLabels = {new JLabel("HOWL_4"),new JLabel("HOWL_5"),new JLabel("FLUTTER_1"),new JLabel("FLUTTER_2"),new JLabel("PATTER_2"),
			new JLabel("PATTER_5"),new JLabel("ROAR_4"),new JLabel("ROAR_6"),new JLabel("SLITHER_3"),new JLabel("SLITHER_6"),new JLabel("HOARD"),
			new JLabel("LAIR"),new JLabel("ALTAR"),new JLabel("SHRINE"),new JLabel("POOL"),new JLabel("VAULT"),new JLabel("CAIRNS"),
			new JLabel("STATUE"),new JLabel("LOST_CITY"),new JLabel("LOST_CASTLE")};
			
	JLabel[] locationNameLabels = {new JLabel("AWFULVALLEY"),new JLabel("BADVALLEY"),new JLabel("BORDERLAND"),new JLabel("CAVERN"),new JLabel("CAVES"),
			new JLabel("CLIFF"),new JLabel("CRAG"),new JLabel("CURSTVALLEY"),new JLabel("DARKVALLEY"),new JLabel("DEEPWOODS"),new JLabel("EVILVALLEY"),
			new JLabel("HIGHPASS"),new JLabel("LEDGES"),new JLabel("LINDENWOODS"),new JLabel("MAPLEWOODS"),new JLabel("MOUNTAIN"),new JLabel("NUTWOODS"),
			new JLabel("OAKWOODS"),new JLabel("PINESWOODS"),new JLabel("RUINS"),new JLabel("LOSTCITY"),new JLabel("LOSTCASTLE")};

	ButtonGroup[] chitGroups = new ButtonGroup[20];
	
	JRadioButton[][] allButtons = new JRadioButton[20][22];
	
	public ChitButtonGrid(ServerController c){
		this.control = c;
		
		setSize(700,400);
		setLayout(new GridLayout(22,20,0,0));
		
		//adds all the buttons to the right button group
		for(int i=0; i < allButtons.length ; i++){
			for( int j=0; j < allButtons[i].length; j++){
				allButtons[i][j] = new JRadioButton();
				allButtons[i][j].setSize(20, 20);
			}	
		}
		
		for( int j=0; j < chitGroups.length; j++){
			chitGroups[j] = new ButtonGroup();
		}	

		//adds all the buttons to the right button group
		for(int i=0; i < allButtons[0].length ; i++){
			//add(locationNameLabels[i]);
			for( int j=0; j < allButtons.length; j++){
				//System.out.println(i+" "+j);
				//System.out.println(allButtons[i][j]);
				chitGroups[j].add(allButtons[j][i]);	
				//allButtons[i][j].setSize();
				add(allButtons[j][i]);
				//allButtons[i][j].setVisible(true);
				//allButtons[i][j].setEnabled(true);
			}	
		}		
	}
	
	public void update(){
		
	}
}