import java.util.Random;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;


public class Shark {

	public static SharkGUI gui;
	
	public static void main(String[] args) {

		int randNum;
		int activePlayer = 0;
		int numOfPlayers = 2;
		int activeCompany;
		
		Random rand = new Random();

		Company NYSO = new Company();
		Company Wings = new Company();
		Company Empire = new Company();
		Company SmithAndSmith = new Company();


		Player PlayerA = new Player("Felix H");
		Player PlayerB = new Player("Felix I");


		ArrayList<int[]> chainList = new ArrayList<int[]>();
		//int[] newThing = {1,2,3};
		//chainList.add(0,newThing);
		//int[] newThing2 = chainList.get(0);
		//System.out.println(newThing2[1]);

		String playerList[] = {PlayerA.getName(),PlayerB.getName()};


		gui = new SharkGUI(playerList);
		gui.disableBoard();


		gui.setSharePrice(SharkConstants.NYSO, NYSO.getSharePrice());
		gui.setSharePrice(SharkConstants.WINGS, Wings.getSharePrice());
		gui.setSharePrice(SharkConstants.EMPIRE, Empire.getSharePrice());
		gui.setSharePrice(SharkConstants.SMITH_AND_SMITH, SmithAndSmith.getSharePrice());

		for (int i = 0; i < numOfPlayers; i++){																						//Muss erweitert werden falls mehr als zwei Spieler spielen
			switch(i){
				case 0:
					randNum = rand.nextInt(4);
					PlayerA.setShares(randNum, 1);
					gui.setNumOfShares(i,randNum+1,PlayerA.getShares(randNum));
					break;
				case 1:
					randNum = rand.nextInt(4);
					PlayerB.setShares(randNum, 1);
					gui.setNumOfShares(i,randNum+1,PlayerB.getShares(randNum));
					break;
			}
		}

		gui.setPlayerInfo(PlayerA.getName());
		gui.tabbedPaneSteps.setSelectedIndex(1);

		for(int i=0; i<5; i++){
			gui.nextButtons[i].setEnabled(false);
			gui.tabbedPaneSteps.setEnabledAt(i,false);
		}

		
		
		
		//  Action Listeners for GUI Elements

		// Board Action Listener
		ActionListener boardActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] coordinates = e.getActionCommand().split("-");
				
				int row = Integer.parseInt(coordinates[0]);
				int column = Integer.parseInt(coordinates[1]);
				
				//System.out.println("Button was pressed => Row: "+row+" Column: "+column); // Test
				
				// Set Background Color of Specific Field (Example Code)
				Color colorSelect = Color.green;
				boolean btnAreaFilled = true;
				int btnIndex = gui.getSelectedRadioBtnIndex();
				
			    switch(btnIndex) {
			    case 0:
			    	btnAreaFilled = true;
			    	colorSelect = Color.green;
			    	break;
			    case 1:
			    	btnAreaFilled = true;
			    	colorSelect = Color.red;
			    	break;
			    case 2:
			    	btnAreaFilled = true;
			    	colorSelect = Color.blue;
			    	break;
			    case 3:
			    	btnAreaFilled = true;
			    	colorSelect = Color.yellow;
			    	break;
			    case 4:
			    	btnAreaFilled = false;
			    	break;
			    }
				
				((JButton)e.getSource()).setContentAreaFilled(btnAreaFilled);
				((JButton)e.getSource()).setBackground(colorSelect);
				
				gui.disableBoard();
				gui.nextButtons[1].setEnabled(true);
				
			}
		};
		
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 12; j++){
				gui.board[i][j].setActionCommand(String.valueOf(i)+"-"+String.valueOf(j));
		        gui.board[i][j].addActionListener(boardActionListener);
		    }
		}
		
		// Next Button Action Listener
		ActionListener nextButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(e.getActionCommand());
				if(index == 3){
					gui.tabbedPaneSteps.setEnabledAt(0,true);
	        		gui.tabbedPaneSteps.setSelectedIndex(0);
	        		gui.tabbedPaneSteps.setEnabledAt(3,false);
				}
				else if(index == 4){
					gui.tabbedPaneSteps.setEnabledAt(2,true);
					gui.tabbedPaneSteps.setSelectedIndex(2);
	        		gui.tabbedPaneSteps.setEnabledAt(4,false);
				}
				else{
					gui.tabbedPaneSteps.setEnabledAt(index+1,true);
					gui.tabbedPaneSteps.setSelectedIndex(index+1);
	        		gui.tabbedPaneSteps.setEnabledAt(index,false);
				}

				switch(index){
					case 0:
						gui.nextButtons[1].setEnabled(false);
					case 1:
						gui.nextButtons[2].setEnabled(false);
					case 2:
						gui.nextButtons[3].setEnabled(false);
					case 3:
						gui.nextButtons[0].setEnabled(false);
				};
			}
		};
		
		for (int i = 0; i < gui.nextButtons.length; i++) {
			gui.nextButtons[i].setActionCommand(String.valueOf(i)); // Index Information for Action Listener
			gui.nextButtons[i].addActionListener(nextButtonActionListener);
		}	
		
		// Buy Button Action Listener
		ActionListener buyButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(e.getActionCommand());
				// Console Message (Example Code)
				System.out.println("Buy Button "+(index+1)+" was pressed");
				
				// ...
			}
		};
		
		for (int i = 0; i < gui.buyButtons.length; i++) {
			gui.buyButtons[i].setActionCommand(String.valueOf(i)); // Index Information for Action Listener
			gui.buyButtons[i].addActionListener(buyButtonActionListener);
		}
				
		// Sell Button Action Listener
		ActionListener sellButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(e.getActionCommand());
				// Console Message (Example Code)
				System.out.println("Sell Button "+(index+1)+" was pressed");
				
				// ...
			}
		};
		
		for (int i = 0; i < gui.sellButtons.length; i++) {
			gui.sellButtons[i].setActionCommand(String.valueOf(i)); // Index Information for Action Listener
			gui.sellButtons[i].addActionListener(sellButtonActionListener);
		}	
		
		// Dice Button Action Listener
		ActionListener diceButtonActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Set the Dice Result Labels (Example Code)
				int activeRegion = (int)(1+Math.random()*6);
				gui.setRegionLabel((activeRegion));
				activateRegion(activeRegion);
				gui.setColorBtn((int)(1+Math.random()*6));
				gui.diceBtn.setEnabled(false);
			}
		};
		
		gui.diceBtn.addActionListener(diceButtonActionListener);
		
	
	}

	private static void activateRegion(int region){
		switch(region){
			case 1:
				for(int i=0;i<5;i++){
					for(int j=0;j<4;j++){
						gui.board[i][j].setEnabled(true);
					}
				}
				break;
			case 2:
				for(int i=0;i<5;i++){
					for(int j=4;j<8;j++){
						gui.board[i][j].setEnabled(true);
					}
				}
				break;
			case 3:
				for(int i=0;i<5;i++){
					for(int j=8;j<12;j++){
						gui.board[i][j].setEnabled(true);
					}
				}
				break;
			case 4:
				for(int i=5;i<10;i++){
					for(int j=0;j<4;j++){
						gui.board[i][j].setEnabled(true);
					}
				}
				break;
			case 5:
				for(int i=5;i<10;i++){
					for(int j=4;j<8;j++){
						gui.board[i][j].setEnabled(true);
					}
				}
				break;
			case 6:
				for(int i=5;i<10;i++){
					for(int j=8;j<12;j++){
						gui.board[i][j].setEnabled(true);
					}
				}
				break;
		};
	}

	private ArrayList<int[]> connectChains(ArrayList<int[]> chainList){

		for(int i=0;i<10;i++){
			for(int j=0;j<12;j++){
				if(gui.board[i][j].isSet()){
					

				}
			}
		}

		return chainList;
	}
}