package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import model.Armour;
import model.Path;
import model.Phase;
import model.Treasure;
import model.Weapon;
import controller.ClientController;
import utils.Utility.Actions;
import utils.Utility.PhaseType;
import utils.Utility.SearchTables;

@SuppressWarnings({ "rawtypes", "unchecked", "serial"})
public class ActivitiesPanel extends JPanel{
	
	private JComboBox extraInfo;
	private JComboBox phase;
	private JComboBox option;
	private ClientController control;
	public boolean state = false;
	
	public ActivitiesPanel(ClientController c){
		
		this.control = c;
		
		setBounds(750, 500, 524, 192);
		setBorder(new LineBorder(Color.GRAY));
		setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Actions");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 11, 72, 14);
		add(lblNewLabel_1);

		extraInfo = new JComboBox();
		extraInfo.setBounds(289, 40, 213, 20);
		add(extraInfo);
		
		phase = new JComboBox();
		phase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//somehow get the actions they can perform in that phase.
				
				Actions[] arr;
				
				switch((PhaseType)phase.getSelectedItem()){
					case BASIC:
						arr = Actions.values();
						break;
					case SPECIAL:
						arr = control.model.getPlayer().getPhases().get(phase.getSelectedIndex()).getAction();
						break;
					case SUNLIGHT:
						arr = Actions.values();
						break;
					case TREASURE:
						ArrayList<Actions> actions = control.model.getPlayer().getTreasureActions();
						arr = new Actions[actions.size() + 1];
						for(int i = 0; i < actions.size(); i++){
							arr[i] = actions.get(i);
						}
						arr[actions.size()] = Actions.PASS;
						break;
					default:
						arr = new Actions[0];
						break;
				}
				
				option.setModel(new DefaultComboBoxModel(arr));
				if(arr.length > 0){
					option.setSelectedIndex(0);
				}
			}
		});
		phase.setBounds(10, 40, 125, 20);
		add(phase);
		
		option = new JComboBox();
		option.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] arr = {};
				
				switch((Actions)option.getSelectedItem()){
					case MOVE:
						ArrayList<Path> connections = control.model.getPlayer().getLocation().getConnections();
						ArrayList<Path> finalConnections = new ArrayList<Path>();
						
						for(Path p : connections){
							if(control.model.getBoard().canUsePath(control.model.getPlayer(), p)){
								finalConnections.add(p);
							}
						}
						
						arr = new String[finalConnections.size()];
						
						for(int i = 0; i < finalConnections.size(); i++){
							//if(player)
							arr[i] = finalConnections.get(i).getDestination(control.model.getPlayer().getLocation()).parent.getName().toString() +" "+ 
									finalConnections.get(i).getDestination(control.model.getPlayer().getLocation()).getClearingNumber();
						}
						extraInfo.setVisible(true);
						
						break;
					case ALERT:
						ArrayList<Weapon> weapons = control.model.getPlayer().getWeapons();
						arr = new String[weapons.size()];
						for(int i = 0; i < weapons.size(); i++){
							arr[i] = weapons.get(i).getType().toString() + " " + weapons.get(i).isActive();
						}
						extraInfo.setVisible(true);
						break;
					case SEARCH:
						arr = SearchTables.values();
						extraInfo.setVisible(true);
						break;
					case TRADE:
						if (control.model.getPlayer().getLocation().getDwelling() != null) {
							ArrayList<Treasure> treasuresToBuy  = control.model.getPlayer().getLocation().getDwelling().getTreasures();
							ArrayList<Treasure> treasuresToSell = control.model.getPlayer().getTreasures();
							ArrayList<Armour>   armourToBuy     = control.model.getPlayer().getLocation().getDwelling().getArmour();
							ArrayList<Armour>   armourToSell    = control.model.getPlayer().getArmour();
							ArrayList<Weapon>   weaponsToBuy    = control.model.getPlayer().getLocation().getDwelling().getWeapons();
							ArrayList<Weapon>   weaponsToSell   = control.model.getPlayer().getWeapons();
									
							
							System.out.println(treasuresToBuy);
							arr = null;
							int size = 0; 
							if (weaponsToBuy != null) {
								size += weaponsToBuy.size();
							}
							if (armourToBuy != null) {
								size += armourToBuy.size();
							}
							if (treasuresToBuy != null) {
								size += treasuresToBuy.size();
							}
							size += treasuresToSell.size() + armourToSell.size() + weaponsToSell.size();
							arr = new String[size];
							int i = 0;
							if (treasuresToBuy != null) {
								for(Treasure t: treasuresToBuy){
									arr[i] = "BUY " + t.getName();
									i++;
								}
							}
							if (armourToBuy != null) {
								for(Armour a: armourToBuy){
									arr[i] = "BUY " + a.getType();
									i++;
								}
							}
							if (weaponsToBuy != null) {
								for(Weapon w: weaponsToBuy){
									arr[i] = "BUY " + w.getType();
									i++;
								}
							}
							for(Treasure t: treasuresToSell){
								arr[i] = "SELL " + t.getName();
								i++;
							}
							for(Armour a: armourToSell){
								arr[i] = "SELL " + a.getType();
								i++;
							}
							for(Weapon w: weaponsToSell){
								arr[i] = "SELL " + w.getType();
								i++;
							}
							extraInfo.setVisible(true);
						}
						break;
					case REST:
					case HIDE:
					case PASS:
						extraInfo.setVisible(false);
						break;
					default:
						break;
				}
				extraInfo.setModel(new DefaultComboBoxModel(arr));
				if(arr.length > 0){
					extraInfo.setSelectedIndex(0);
				}
			}
		});
		
		option.setBounds(163, 40, 93, 20);
		add(option);
		
		JButton btnRecord = new JButton("Send");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.handlePlaySubmit((PhaseType)phase.getSelectedItem(), new Actions[] {(Actions) option.getSelectedItem()}, extraInfo.getSelectedItem());
			}
		});
		
		btnRecord.setBounds(220, 145, 89, 23);
		add(btnRecord);
	}

	public void update(){
		PhaseType p = (PhaseType)phase.getSelectedItem();
		Actions a = (Actions)option.getSelectedItem();
		Object e = null;
		
		if(a != Actions.REST && a != Actions.PASS && a != Actions.HIDE){
			e = extraInfo.getSelectedItem();
		}
		
		boolean valid = true;//flag for if previous selections are still valid
		
		ArrayList<Phase> phases = this.control.model.getPlayer().getPhases();
		PhaseType[] pArr = new PhaseType[phases.size()];
		for(int i=0; i<phases.size(); i++){
			pArr[i] = (phases.get(i).getType());
		}
		phase.setModel(new DefaultComboBoxModel(pArr));
		
		//trying to reselect the previous selection
		if(((DefaultComboBoxModel)phase.getModel()).getIndexOf(p) != -1){
			phase.setSelectedItem(p);
		}
		else{
			valid = false;
		}
		
		if(valid && ((DefaultComboBoxModel)option.getModel()).getIndexOf(a) != -1){
			option.setSelectedItem(a);
		}
		else{
			valid = false;
		}
		
		if(e == null){
			//Do nothing, the third combobox was not being used before.
		}
		else if(valid && ((DefaultComboBoxModel)extraInfo.getModel()).getIndexOf(e) != -1){
			extraInfo.setSelectedItem(e);
		}
		else{
			valid = false;
		}
		
		if(!valid && phases.size() > 0){
			phase.setSelectedIndex(0);
		}
			
	}
}
