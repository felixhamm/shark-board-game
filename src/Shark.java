import java.util.Random;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;


public class Shark {
	
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

		
		String playerList[] = {PlayerA.getName(),PlayerB.getName()};

		SharkGUI gui = new SharkGUI(playerList);
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



		
		
		//  Action Listeners for GUI Elements
		
		// Board Action Listener
		ActionListener boardActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
				
				// ...
				
			}
		};
		
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 12; j++){
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
				gui.setColorBtn((int)(1+Math.random()*6));
				
				gui.setRegionLabel(activeRegion);
				
			}
		};
		
		gui.diceBtn.addActionListener(diceButtonActionListener);
		//nextButtonActionListener.actionPerformed(1);

		
		//----------------------------------------
		
		//gui.disableBoard();
		
		// Some GUI methods ...
		//gui.setPlayerInfo(playerList[(int)(Math.random()*playerList.length)]);
		//gui.setForcedSalePlayerLabel(playerList[(int)(Math.random()*playerList.length)]);
		
		//gui.setForcedSaleBalance(-2000);
		
		//gui.disableBoard();


		
		
		
	
	}
}