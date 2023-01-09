import java.util.Random;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;


public class Shark {

	private static SharkGUI gui;

	private static Company NYSO;
	private static Company Wings;
	private static Company Empire;
	private static Company SmithAndSmith;

	private static Player PlayerA;
	private static Player PlayerB;

	static int activePlayer = 0;
	static int activeCompany;
	static ArrayList<int[]> chainList = new ArrayList<int[]>();

	
	public static void main(String[] args) {

		int randNum;
		activePlayer = 0;
		int numOfPlayers = 2;
		activeCompany = -1;
		
		Random rand = new Random();

		NYSO = new Company();
		Wings = new Company();
		Empire = new Company();
		SmithAndSmith = new Company();


		PlayerA = new Player("Felix H");
		PlayerB = new Player("Felix I");


		chainList = new ArrayList<int[]>();
		int[] newElement = {0,0,0};


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

				activeCompany = btnIndex;
				gui.board[row][column].setSet(true);
				gui.board[row][column].setCompanyIndex(activeCompany);
				gui.board[row][column].setChainIndex(chainList.size());
				newElement[0] = chainList.size();
				newElement[1] = 1;
				newElement[2] = activeCompany;
				chainList.add(newElement);
				
				((JButton)e.getSource()).setContentAreaFilled(btnAreaFilled);
				((JButton)e.getSource()).setBackground(colorSelect);
				
				gui.disableBoard();
				chainList = connectChains(chainList);

				int[] listElement = {0,0,0};
				for(int i=0;i<chainList.size();i++){
					listElement = chainList.get(i);
					System.out.println(listElement[0]);
					System.out.println(listElement[1]);
					System.out.println(listElement[2]);
					System.out.println("-");

				}
				System.out.println("-------");

				updateCompanyValues(chainList);
				givePlayerSuccessFee(activePlayer, activeCompany);
				givePlayersDividend();
				updateGui();

				


				switch(btnIndex){
					case 0:
						NYSO.setRemainingMarkers(NYSO.getRemainingMarkers()-1);
						gui.setRemainingMarkers(btnIndex, NYSO.getRemainingMarkers());
						break;
					case 1:
						SmithAndSmith.setRemainingMarkers(SmithAndSmith.getRemainingMarkers()-1);
						gui.setRemainingMarkers(btnIndex, SmithAndSmith.getRemainingMarkers());
						break;
					case 2:
						Empire.setRemainingMarkers(Empire.getRemainingMarkers()-1);
						gui.setRemainingMarkers(btnIndex, Empire.getRemainingMarkers());
						break;
					case 3:
						Wings.setRemainingMarkers(Wings.getRemainingMarkers()-1);
						gui.setRemainingMarkers(btnIndex, Wings.getRemainingMarkers());
						break;
				};

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

				switch(index){
					case 0:
						gui.nextButtons[1].setEnabled(false);
						break;
					case 1:
						gui.nextButtons[2].setEnabled(false);
						break;
					case 2:
						gui.nextButtons[3].setEnabled(false);
						break;
					case 3:
						gui.nextButtons[0].setEnabled(false);
						break;
				};

				if(index == 0){
					gui.tabbedPaneSteps.setEnabledAt(index+1,true);
					gui.tabbedPaneSteps.setSelectedIndex(index+1);
	        		gui.tabbedPaneSteps.setEnabledAt(index,false);

					gui.diceBtn.setEnabled(true);
				}
				else if(index == 1){
					gui.tabbedPaneSteps.setEnabledAt(index+1,true);
					gui.tabbedPaneSteps.setSelectedIndex(index+1);
	        		gui.tabbedPaneSteps.setEnabledAt(index,false);

					gui.nextButtons[2].setEnabled(true);
					gui.resetPayoutSummary();	
				}
				else if(index == 2){
					gui.tabbedPaneSteps.setEnabledAt(index+1,true);
					gui.tabbedPaneSteps.setSelectedIndex(index+1);
	        		gui.tabbedPaneSteps.setEnabledAt(index,false);

					gui.nextButtons[3].setEnabled(true);
				}
				else if(index == 3){
					gui.tabbedPaneSteps.setEnabledAt(0,true);
	        		gui.tabbedPaneSteps.setSelectedIndex(0);
	        		gui.tabbedPaneSteps.setEnabledAt(3,false);

					gui.nextButtons[0].setEnabled(true);
					if(activePlayer < numOfPlayers-1){
						activePlayer++;
					}
					else if(activePlayer == numOfPlayers-1){
						activePlayer = 0;
					}
					setActivePlayerName(activePlayer);
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

	private static ArrayList<int[]> connectChains(ArrayList<int[]> chainlist){

		for(int i=0;i<10;i++){
			for(int j=0;j<12;j++){
				if(gui.board[i][j].isSet()){
					
					if(i != 0 && (gui.board[i-1][j].getCompanyIndex() == gui.board[i][j].getCompanyIndex())){
						
						if(gui.board[i][j].getChainIndex() <= gui.board[i-1][j].getChainIndex()){
							chainlist = changeChainIndex(chainlist, gui.board[i-1][j].getChainIndex(), gui.board[i][j].getChainIndex());
						}
						else{
							chainlist = changeChainIndex(chainlist, gui.board[i][j].getChainIndex(), gui.board[i-1][j].getChainIndex());
						}

						chainlist = updateChain(chainlist, gui.board[i-1][j].getChainIndex());
						chainlist = updateChain(chainlist, gui.board[i][j].getChainIndex());
					}

					if(i != 9 && (gui.board[i+1][j].getCompanyIndex() == gui.board[i][j].getCompanyIndex())){

						if(gui.board[i][j].getChainIndex() <= gui.board[i+1][j].getChainIndex()){
							chainlist = changeChainIndex(chainlist, gui.board[i+1][j].getChainIndex(), gui.board[i][j].getChainIndex());
						}
						else{
							chainlist = changeChainIndex(chainlist, gui.board[i][j].getChainIndex(), gui.board[i+1][j].getChainIndex());
						}

						chainlist = updateChain(chainlist, gui.board[i+1][j].getChainIndex());
						chainlist = updateChain(chainlist, gui.board[i][j].getChainIndex());
					}

					if(j != 0 && (gui.board[i][j-1].getCompanyIndex() == gui.board[i][j].getCompanyIndex())){

						if(gui.board[i][j].getChainIndex() <= gui.board[i][j-1].getChainIndex()){
							chainlist = changeChainIndex(chainlist, gui.board[i][j-1].getChainIndex(), gui.board[i][j].getChainIndex());
						}
						else{
							chainlist = changeChainIndex(chainlist, gui.board[i][j].getChainIndex(), gui.board[i][j-1].getChainIndex());
						}

						chainlist = updateChain(chainlist, gui.board[i][j-1].getChainIndex());
						chainlist = updateChain(chainlist, gui.board[i][j].getChainIndex());
					}

					if(j != 11 && (gui.board[i][j+1].getCompanyIndex() == gui.board[i][j].getCompanyIndex())){

						if(gui.board[i][j].getChainIndex() <= gui.board[i][j+1].getChainIndex()){
							chainlist = changeChainIndex(chainlist, gui.board[i][j+1].getChainIndex(), gui.board[i][j].getChainIndex());
						}
						else{
							chainlist = changeChainIndex(chainlist, gui.board[i][j].getChainIndex(), gui.board[i][j+1].getChainIndex());
						}

						chainlist = updateChain(chainlist, gui.board[i][j+1].getChainIndex());
						chainlist = updateChain(chainlist, gui.board[i][j].getChainIndex());
					}
				}
			}
		}

		return chainlist;
	}

	private static ArrayList<int[]> changeChainIndex(ArrayList<int[]> chainlist, int oldIndex, int newIndex){
		int[] oldElement = {0,0,0};
		for(int i=0;i<10;i++){
			for(int j=0;j<12;j++){

				if(gui.board[i][j].getChainIndex() == oldIndex){
					gui.board[i][j].setChainIndex(newIndex);
					//oldElement = chainList.get(oldIndex);
					//oldElement[1] = 0;

				}

			}
		}
		return chainlist;
	}

	private static ArrayList<int[]> updateChain(ArrayList<int[]> chainlist, int chain){

		int length = 0;
		int[] oldElement = chainlist.get(chain);
		int[] newElement = {oldElement[0],0,oldElement[2]};

		for(int i=0;i<10;i++){
			for(int j=0;j<12;j++){

				if(gui.board[i][j].getChainIndex() == chain){
					length++;
				}

			}
		}

		newElement[1] = length;
		chainlist.set(newElement[0], newElement);


		return chainlist;
	}

	private static void updateCompanyValues(ArrayList<int[]> chainlist){
		int[] newValue = {0,0,0,0};
		int[] currentElement = {0,0,0};
		boolean[] onlyOnes = {false, false, false, false};

		for(int i=0; i < chainlist.size(); i++){
			
			currentElement = chainlist.get(i);
			
			//if(currentElement[1] > 1 || (currentElement[1] == 1 && newValue[currentElement[2]] == 0)){
			//	newValue[currentElement[2]] = newValue[currentElement[2]] + currentElement[1];
			//}
			if(currentElement[1] > 1){
				newValue[currentElement[2]] = newValue[currentElement[2]] + currentElement[1];
			}
			else if(currentElement[1] == 1){
				onlyOnes[currentElement[2]] = true;
			}
		}

		for(int i=0;i<4;i++){
			if(newValue[i] == 0 && onlyOnes[i] == true){
			newValue[i] = 1;
			}
		}

		NYSO.setSharePriceOld(NYSO.getSharePrice());
		Wings.setSharePriceOld(Wings.getSharePrice());
		Empire.setSharePriceOld(Empire.getSharePrice());
		SmithAndSmith.setSharePriceOld(SmithAndSmith.getSharePrice());

		NYSO.setSharePrice(1000*newValue[0]);
		Wings.setSharePrice(1000*newValue[3]);
		Empire.setSharePrice(1000*newValue[2]);
		SmithAndSmith.setSharePrice(1000*newValue[1]);

		for(int i=0;i<4;i++){
			//System.out.println(newValue[i]);
		}

		
		
	}


	private static void givePlayerSuccessFee(int player, int company){
		int successFee = 0;
		switch(company){
			case 0:
				successFee = NYSO.getSharePrice();
				break;
			case 1:
				successFee = SmithAndSmith.getSharePrice();
				break;
			case 2:
				successFee = Empire.getSharePrice();
				break;
			case 3:
				successFee = Wings.getSharePrice();
				break;
		};
	
		switch(player){
			case(0):
				PlayerA.addCash(successFee);
				break;
			case(1):
				PlayerB.addCash(successFee);
				break;
		};
	}


	private static void updateGui(){
		gui.setNumOfShares(0, 1, PlayerA.getShares(0));
		gui.setNumOfShares(0, 4, PlayerA.getShares(3));
		gui.setNumOfShares(0, 3, PlayerA.getShares(2));
		gui.setNumOfShares(0, 2, PlayerA.getShares(1));
		gui.setNumOfShares(1, 1, PlayerB.getShares(0));
		gui.setNumOfShares(1, 4, PlayerB.getShares(3));
		gui.setNumOfShares(1, 3, PlayerB.getShares(2));
		gui.setNumOfShares(1, 2, PlayerB.getShares(1));

		gui.setCash(0, PlayerA.getCash());
		gui.setCash(1, PlayerB.getCash());

		gui.setSharePrice(0, NYSO.getSharePrice());
		gui.setSharePrice(3, Wings.getSharePrice());
		gui.setSharePrice(2, Empire.getSharePrice());
		gui.setSharePrice(1, SmithAndSmith.getSharePrice());

		gui.setRemainingShares(0, NYSO.getRemainingShares());
		gui.setRemainingShares(3, Wings.getRemainingShares());
		gui.setRemainingShares(2, Empire.getRemainingShares());
		gui.setRemainingShares(1, SmithAndSmith.getRemainingShares());

		gui.setRemainingMarkers(0, NYSO.getRemainingMarkers());
		gui.setRemainingMarkers(3, Wings.getRemainingMarkers());
		gui.setRemainingMarkers(2, Empire.getRemainingMarkers());
		gui.setRemainingMarkers(1, SmithAndSmith.getRemainingMarkers());
	}


	private static void givePlayersDividend(){
		PlayerA.addCash((NYSO.getSharePrice() - NYSO.getSharePriceOld()) * PlayerA.getShares(0));
		PlayerA.addCash((Wings.getSharePrice() - Wings.getSharePriceOld()) * PlayerA.getShares(3));
		PlayerA.addCash((Empire.getSharePrice() - Empire.getSharePriceOld()) * PlayerA.getShares(2));
		PlayerA.addCash((SmithAndSmith.getSharePrice() - SmithAndSmith.getSharePriceOld()) * PlayerA.getShares(1));

		PlayerB.addCash((NYSO.getSharePrice() - NYSO.getSharePriceOld()) * PlayerB.getShares(0));
		PlayerB.addCash((Wings.getSharePrice() - Wings.getSharePriceOld()) * PlayerB.getShares(3));
		PlayerB.addCash((Empire.getSharePrice() - Empire.getSharePriceOld()) * PlayerB.getShares(2));
		PlayerB.addCash((SmithAndSmith.getSharePrice() - SmithAndSmith.getSharePriceOld()) * PlayerB.getShares(1));
	}

	private static void setActivePlayerName(int player){
		switch(player){
			case 0:
				gui.setPlayerInfo(PlayerA.getName());
				break;
			case 1:
				gui.setPlayerInfo(PlayerB.getName());
				break;
		};
	}
}




//if((gui.board[i+1][j].getCompanyIndex() != gui.board[i][j].getCompanyIndex()) && (gui.board[i+1][j].getCompanyIndex() != SharkConstants.NONE)){