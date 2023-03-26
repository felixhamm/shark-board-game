import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class Shark {
	// Global variables
	private static SharkGUI gui;

	private static final int numOfCompanies = 4;
	private static Company Companies[] = new Company[numOfCompanies];

	private static String playerList[] = {"Alice","Bob","Felix"};
	private static final int numOfPlayers = playerList.length;
	private static Player Players[] = new Player[numOfPlayers]; 

	private static Player activePlayer;
	private static Player forcedSalePlayer;
	private static Company activeCompany;
	private static int activeRegion;
	private static ArrayList<int[]> chainList = new ArrayList<int[]>();
	private static int buyLimit = 0;
	
	private static boolean forcedSaleIndicator = false;
	private static boolean gameOver = false;

	
	public static void main(String[] args) {
		Companies[0] = new Company(SharkConstants.NYSO,"NYSO",Color.green);
		Companies[1] = new Company(SharkConstants.SMITH_AND_SMITH,"Smith & Smith",Color.red);
		Companies[2] = new Company(SharkConstants.EMPIRE,"Empire",Color.blue);
		Companies[3] = new Company(SharkConstants.WINGS,"Wings",Color.yellow);
		
		gui = new SharkGUI(playerList);
		gui.disableBoard();
		resetInfoBoxes();

		for(int i = 0; i < numOfCompanies; i++) {
			gui.setSharePrice(Companies[i].getCompanyIndex(), Companies[i].getSharePrice());
		}
		
		for(int i = 0; i < numOfPlayers; i++) {
			int companyIndex = (int)(Math.random()*4);
			
			activeCompany = Companies[companyIndex];
			
			Players[i] = new Player(i, playerList[i]);
			Players[i].setShares(companyIndex, 1);
			gui.setNumOfShares(i,companyIndex, 1);
			int remainingShares = activeCompany.getRemainingShares() - 1;
			activeCompany.setRemainingShares(remainingShares);
			gui.setRemainingShares(companyIndex, remainingShares);
		}
		
		activePlayer = Players[0];

		gui.setPlayerInfo(activePlayer.getName());
		gui.tabbedPaneSteps.setSelectedIndex(1);

		for(int i=0; i<5; i++){
			gui.nextButtons[i].setEnabled(false);
			if(i != 1) {
				gui.tabbedPaneSteps.setEnabledAt(i,false);
			}
		}
		
		updateGui();
		
		
		//  Action Listeners for GUI Elements

		// Board Action Listener
		ActionListener boardActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] coordinates = e.getActionCommand().split("-");
				
				int row = Integer.parseInt(coordinates[0]);
				int column = Integer.parseInt(coordinates[1]);
				
				int companyIndex = gui.getSelectedRadioBtnIndex();
			    
			    activeCompany = Companies[companyIndex];
			    
			    gui.board[row][column].setSet(true);
				gui.board[row][column].setCompanyIndex(activeCompany.getCompanyIndex());
				gui.board[row][column].setChainIndex(chainList.size());
				
				int[] newElement = {0,0,0};
				newElement[0] = chainList.size(); // Chain Index
				newElement[1] = 1; // Chain Length
				newElement[2] = activeCompany.getCompanyIndex(); // Company Index
				chainList.add(newElement);
				
				// Set Background Color of Specific Field
				((JButton)e.getSource()).setContentAreaFilled(true);
				((JButton)e.getSource()).setBackground(activeCompany.getColor());
				
				int remainingMarkers = activeCompany.getRemainingMarkers()-1;
				activeCompany.setRemainingMarkers(remainingMarkers);
				if(remainingMarkers == 0) {
					gameOver = true;
				}
				
				gui.disableBoard();
				mergeChains(row,column);
				destroyChains(row,column);
				boolean anyLosses = updateCompanyValues();

				// Alternative: Move on to step 3 (automatically) after marker placement
				// In this case the next button of step 2 is not used!
				//gui.nextButtons[1].setEnabled(true);
				
				// Step 3: Summary of Marker Placement
				gui.tabbedPaneSteps.setEnabledAt(2,true);
				gui.tabbedPaneSteps.setSelectedIndex(2);
				gui.tabbedPaneSteps.setEnabledAt(1,false);
				
				// Success Fee
				activePlayerReceivesSuccessFee();
				// Dividends
				playersReceiveDividends();
				// Losses
				if(anyLosses){
					gui.addLineToPayoutSummary("Potential Losses:");
					playersPayForLosses(0);
				}
				else{
					gui.nextButtons[2].setEnabled(true);
				}

				// Game Over?
				if(gameOver) {
					gameOverRoutine();					
					gui.nextButtons[2].setEnabled(false);
				}
				
				updateGui();
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
						gui.tabbedPaneSteps.setEnabledAt(1,true);
						gui.tabbedPaneSteps.setSelectedIndex(1);
		        		gui.tabbedPaneSteps.setEnabledAt(0,false);

						gui.diceBtn.setEnabled(true);
						break;
					case 1:
						gui.tabbedPaneSteps.setEnabledAt(3,true);
						gui.tabbedPaneSteps.setSelectedIndex(3);
		        		gui.tabbedPaneSteps.setEnabledAt(1,false);

		        		gui.nextButtons[1].setEnabled(false);
		        		gui.nextButtons[3].setEnabled(true);
						break;
					case 2:
						if(forcedSaleIndicator){
							gui.tabbedPaneSteps.setEnabledAt(4,true);
							gui.tabbedPaneSteps.setSelectedIndex(4);
							//gui.nextButtons[4].setEnabled(true);
						}
						else{
							gui.tabbedPaneSteps.setEnabledAt(3,true);
							gui.tabbedPaneSteps.setSelectedIndex(3);
							gui.nextButtons[3].setEnabled(true);
						}
						
						gui.tabbedPaneSteps.setEnabledAt(2,false);
						
						break;
					case 3:
						int oldIndex = activePlayer.getPlayerIndex();
						int newIndex = (oldIndex+1)%numOfPlayers;
						
						while(!(Players[newIndex].getActivityStatus())){
							if(newIndex == oldIndex){
								break;
							}
							newIndex = (newIndex+1)%numOfPlayers;
						}
						if(newIndex == oldIndex){
							gameOver = true;
							gameOverRoutine();
							
							gui.tabbedPaneSteps.setEnabledAt(2,true);
			        		gui.tabbedPaneSteps.setSelectedIndex(2);
			        		
			        		gui.nextButtons[2].setEnabled(false);
						}
						else{
							activePlayer = Players[newIndex];
							buyLimit = 0;
							resetInfoBoxes(); // Reset all Info boxes
							setActivePlayerName(); // Update Player Label
							
							gui.tabbedPaneSteps.setEnabledAt(0,true);
			        		gui.tabbedPaneSteps.setSelectedIndex(0);
			        		
			        		gui.nextButtons[0].setEnabled(true);
						}
						gui.tabbedPaneSteps.setEnabledAt(3,false);
						break;
					case 4:
						gui.tabbedPaneSteps.setEnabledAt(2,true);
						gui.tabbedPaneSteps.setSelectedIndex(2);
		        		gui.tabbedPaneSteps.setEnabledAt(4,false);
		        		
		        		playersPayForLosses(forcedSalePlayer.getPlayerIndex()+1);
						break;
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
				int tab = 0;
				// Console Message (Example Code)
				//System.out.println("Buy Button "+(index+1)+" was pressed");

				if(index == 0){
					tab = SharkConstants.TAB_1_BUY_AND_SELL;
				}
				else if(index == 1){
					tab = SharkConstants.TAB_4_BUY_AND_SELL;
				}
				else{
					System.out.println("Error: Invalid Buy Button Index");
					return;
				}
				
				activeCompany = Companies[gui.getCompanyIndex(tab)];
				
				int quantity = gui.getQuantity(tab);
				int sharePrice = activeCompany.getSharePrice();
				int price = sharePrice * quantity;
				int remainingShares = activeCompany.getRemainingShares();
				
				if(sharePrice > 0){
					if(activePlayer.getCash() >= price){
						if(buyLimit + quantity <= 5){
							if(remainingShares - quantity >= 0){
								int newShares = activePlayer.getShares(activeCompany.getCompanyIndex()) + quantity;
								activePlayer.setShares(activeCompany.getCompanyIndex(), newShares);
								activePlayer.setCash(activePlayer.getCash() - price);
								activeCompany.setRemainingShares(remainingShares - quantity);
								buyLimit += quantity;
								gui.setBrokerInfoText(tab,"You've bought "+String.valueOf(quantity)+" "+activeCompany.getName()+" share(s) for "+String.valueOf(price)+" FT");
							}
							else{
								gui.setBrokerInfoText(tab,"There are only "+String.valueOf(remainingShares)+" "+activeCompany.getName()+" share(s) left");
							}
						}
						else{
							gui.setBrokerInfoText(tab,"You can only buy 5 shares per turn. You've already bought "+String.valueOf(buyLimit)+" share(s)");
						}
					}
					else{
						gui.setBrokerInfoText(tab,"You've got not enough money for "+String.valueOf(quantity)+" "+activeCompany.getName()+" share(s)");
					}
				}
				else{
					gui.setBrokerInfoText(tab,"You can only buy shares with a price greater than 0 FT");
				}	

				updateGui();
				
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
				int tab = 0;

				if(index == 0){
					tab = SharkConstants.TAB_1_BUY_AND_SELL;
				}
				else if(index == 1){
					tab = SharkConstants.TAB_4_BUY_AND_SELL;
				}
				else if(index == 2){
					tab = SharkConstants.TAB_5_FORCED_SALE;
				}
				else{
					System.out.println("Error: Invalid Sell Button Index");
					return;
				}
				
				activeCompany = Companies[gui.getCompanyIndex(tab)];
				
				int quantity = gui.getQuantity(tab);
				int sharePrice = activeCompany.getSharePrice();
				int price = sharePrice * quantity;
				
				if(forcedSaleIndicator){
					price = ((int)(price/2000))*1000; // Partial amounts under 1000 FT are rounded down
					int numOfShares = forcedSalePlayer.getShares(activeCompany.getCompanyIndex());
					int debt = forcedSalePlayer.getDebt();
					
					if(sharePrice > 0){
						if(numOfShares >= quantity){
							forcedSalePlayer.setShares(activeCompany.getCompanyIndex(), numOfShares - quantity);
							activeCompany.setRemainingShares(activeCompany.getRemainingShares() + quantity);
							if(debt > price){
								int newDebt = debt - price;
								forcedSalePlayer.setDebt(newDebt);
								gui.setForcedSaleBalance((-1)*newDebt);
							}
							else{
								int newBalance = price - debt;
								forcedSalePlayer.setDebt(0);
								forcedSalePlayer.setCash(newBalance);
								gui.setForcedSaleBalance(newBalance);
								forcedSaleIndicator = false;
								
								gui.sellButtons[2].setEnabled(false);
								gui.nextButtons[4].setEnabled(true);
							}
						}
					}
				}
				else{
					int numOfShares = activePlayer.getShares(activeCompany.getCompanyIndex());
					
					if(sharePrice > 0){
						if(numOfShares >= quantity){
							activePlayer.setShares(activeCompany.getCompanyIndex(), numOfShares - quantity);
							activePlayer.setCash(activePlayer.getCash() + price);
							activeCompany.setRemainingShares(activeCompany.getRemainingShares() + quantity);
							gui.setBrokerInfoText(tab,"You've sold "+String.valueOf(quantity)+" "+activeCompany.getName()+" share(s) for "+String.valueOf(price)+" FT");
						}
						else{
							gui.setBrokerInfoText(tab,"You do not own "+String.valueOf(quantity)+" "+activeCompany.getName()+" share(s)");
						}
					}
					else{
						gui.setBrokerInfoText(tab,"You can only sell shares with a price greater than 0 FT");
					}
				}
				
				updateGui();
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
				activeRegion = (int)(1+Math.random()*6);
				gui.setRegionLabel((activeRegion));
				int dicedColor = (int)(1+Math.random()*6);
				gui.setColorBtn(dicedColor);
				gui.diceBtn.setEnabled(false);
				
				activeCompany = Companies[gui.getSelectedRadioBtnIndex()];
				if(!enableRegion(activeRegion)){
					if(dicedColor < 5) {
						gui.setDiceInfo("Oh dear! All fields are occupied. Unfortunately, you have to move on");
						gui.nextButtons[1].setEnabled(true);
					}
				}
				
			}
		};
		
		gui.diceBtn.addActionListener(diceButtonActionListener);
		
		// Radio Button Action Listener
		ActionListener radioButtonActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				activeCompany = Companies[gui.getSelectedRadioBtnIndex()];
				if(!enableRegion(activeRegion)){
					gui.setDiceInfo("Oops, all fields are occupied => Select a different color");
				}
				else{
					gui.setDiceInfo("You're lucky! Choose a color.");
				}
			}
		};
		
		for (int i = 0; i < gui.radioButtons.length; i++) {
			gui.radioButtons[i].addActionListener(radioButtonActionListener);
		}
	}
	
	// Enables all valid fields in the selected region (set fields and adjacent field are taken into account)
	private static boolean enableRegion(int region){
		boolean validFieldExists = false; // At least one valid field in selected region
		
		gui.disableBoard();
		
		switch(region){
			case 1:
				for(int i=0;i<5;i++){
					for(int j=0;j<4;j++){
						if(gui.board[i][j].isSet() == false){
							if(validField(i,j)){
								gui.board[i][j].setEnabled(true);
								validFieldExists = true;
							} 
						}
					}
				}
				break;
			case 2:
				for(int i=0;i<5;i++){
					for(int j=4;j<8;j++){
						if(gui.board[i][j].isSet() == false){
							if(validField(i,j)){
								gui.board[i][j].setEnabled(true);
								validFieldExists = true;
							}
						}
					}
				}
				break;
			case 3:
				for(int i=0;i<5;i++){
					for(int j=8;j<12;j++){
						if(gui.board[i][j].isSet() == false){
							if(validField(i,j)){
								gui.board[i][j].setEnabled(true);
								validFieldExists = true;
							}
						}
					}
				}
				break;
			case 4:
				for(int i=5;i<10;i++){
					for(int j=0;j<4;j++){
						if(gui.board[i][j].isSet() == false){
							if(validField(i,j)){
								gui.board[i][j].setEnabled(true);
								validFieldExists = true;
							}
						}
					}
				}
				break;
			case 5:
				for(int i=5;i<10;i++){
					for(int j=4;j<8;j++){
						if(gui.board[i][j].isSet() == false){
							if(validField(i,j)){
								gui.board[i][j].setEnabled(true);
								validFieldExists = true;
							}
						}
					}
				}
				break;
			case 6:
				for(int i=5;i<10;i++){
					for(int j=8;j<12;j++){
						if(gui.board[i][j].isSet() == false){
							if(validField(i,j)){
								gui.board[i][j].setEnabled(true);
								validFieldExists = true;
							}
						}
					}
				}
				break;
		};
		
		return validFieldExists;
	}
	
	// Checks if selected field is valid (with regard to the adjacent company chains)
	private static boolean validField(int row, int column){
		int adjacentCompanies[] = {0,0,0,0}; // Ordered by company index (Green,Red,Blue,Yellow)
		int  selectedCompany = activeCompany.getCompanyIndex();
		
		// TOP
		if((row-1) >= 0){
			int chainIndex = gui.board[row-1][column].getChainIndex();
			if(chainIndex >= 0){
				int[] listElement = chainList.get(chainIndex);
				int chainLength = listElement[1];
				int company = listElement[2];
				if(company == selectedCompany){
					adjacentCompanies[company] += chainLength;
				}
				else{
					if(adjacentCompanies[company] < chainLength){
						adjacentCompanies[company] = chainLength;
					}
				}
			}
		}
		// RIGHT
		if((column+1) <= 11){
			int chainIndex = gui.board[row][column+1].getChainIndex();
			if(chainIndex >= 0){
				int[] listElement = chainList.get(chainIndex);
				int chainLength = listElement[1];
				int company = listElement[2];
				if(company == selectedCompany){
					adjacentCompanies[company] += chainLength;
				}
				else{
					if(adjacentCompanies[company] < chainLength){
						adjacentCompanies[company] = chainLength;
					}
				}
			}
		}
		// BOTTOM
		if((row+1) <= 9){
			int chainIndex = gui.board[row+1][column].getChainIndex();
			if(chainIndex >= 0){
				int[] listElement = chainList.get(chainIndex);
				int chainLength = listElement[1];
				int company = listElement[2];
				if(company == selectedCompany){
					adjacentCompanies[company] += chainLength;
				}
				else{
					if(adjacentCompanies[company] < chainLength){
						adjacentCompanies[company] = chainLength;
					}
				}
			}
		}
		// LEFT
		if((column-1) >= 0){
			int chainIndex = gui.board[row][column-1].getChainIndex();
			if(chainIndex >= 0){
				int[] listElement = chainList.get(chainIndex);
				int chainLength = listElement[1];
				int company = listElement[2];
				if(company == selectedCompany){
					adjacentCompanies[company] += chainLength;
				}
				else{
					if(adjacentCompanies[company] < chainLength){
						adjacentCompanies[company] = chainLength;
					}
				}
			}
		}
		
		boolean isSelectedCompanyDominant = true;
		
		for(int i = 0; i <= 3; i++){
			if(i != selectedCompany){
				if(adjacentCompanies[i] > adjacentCompanies[selectedCompany]){
					isSelectedCompanyDominant = false;
					break;
				}
			}
		}
		
		return isSelectedCompanyDominant;
	}

	private static void mergeChains(int row, int column){
		if(gui.board[row][column].isSet()){
			// TOP
			if(row > 0 && (gui.board[row-1][column].getCompanyIndex() == gui.board[row][column].getCompanyIndex())){
				if(gui.board[row][column].getChainIndex() <= gui.board[row-1][column].getChainIndex()){
					updateChainIndex(gui.board[row-1][column].getChainIndex(), gui.board[row][column].getChainIndex());
				}
				else{
					updateChainIndex(gui.board[row][column].getChainIndex(), gui.board[row-1][column].getChainIndex());
				}
			}
			// RIGHT
			if(column < 11 && (gui.board[row][column+1].getCompanyIndex() == gui.board[row][column].getCompanyIndex())){
				if(gui.board[row][column].getChainIndex() <= gui.board[row][column+1].getChainIndex()){
					updateChainIndex(gui.board[row][column+1].getChainIndex(), gui.board[row][column].getChainIndex());
				}
				else{
					updateChainIndex(gui.board[row][column].getChainIndex(), gui.board[row][column+1].getChainIndex());
				}
			}
			// BOTTOM
			if(row < 9 && (gui.board[row+1][column].getCompanyIndex() == gui.board[row][column].getCompanyIndex())){
				if(gui.board[row][column].getChainIndex() <= gui.board[row+1][column].getChainIndex()){
					updateChainIndex(gui.board[row+1][column].getChainIndex(), gui.board[row][column].getChainIndex());
				}
				else{
					updateChainIndex(gui.board[row][column].getChainIndex(), gui.board[row+1][column].getChainIndex());
				}
			}
			// LEFT
			if(column > 0 && (gui.board[row][column-1].getCompanyIndex() == gui.board[row][column].getCompanyIndex())){
				if(gui.board[row][column].getChainIndex() <= gui.board[row][column-1].getChainIndex()){
					updateChainIndex(gui.board[row][column-1].getChainIndex(), gui.board[row][column].getChainIndex());
				}
				else{
					updateChainIndex(gui.board[row][column].getChainIndex(), gui.board[row][column-1].getChainIndex());
				}
			}
		}
	}

	private static void updateChainIndex(int oldIndex, int newIndex){
		int delta = 0;
		
		int[] oldChainListEntry = chainList.get(oldIndex); // Get reference of chainList entry
		oldChainListEntry[1] = 0; // Set chain length to zero
		
		for(int i=0;i<10;i++){
			for(int j=0;j<12;j++){
				if(gui.board[i][j].getChainIndex() == oldIndex){
					gui.board[i][j].setChainIndex(newIndex); // Update chain index of company marker
					delta++;
				}
			}
		}
		
		int[] chainListEntry = chainList.get(newIndex); // Get reference of chainList entry
		chainListEntry[1] += delta; // Update chain length
	}

	// Calculates the new share price of each company
	private static boolean updateCompanyValues(){
		boolean anyLosses = false; // Are there any losses?
		int[] newValue = {0,0,0,0};
		boolean[] isolatedMarkers = {false, false, false, false};

		for(int i=0; i < chainList.size(); i++){
			int[] currentElement = chainList.get(i);
			int chainLength = currentElement[1];
			int companyIndex = currentElement[2];
			
			if(chainLength > 1){
				newValue[companyIndex] = newValue[companyIndex] + chainLength;
			}
			else if(chainLength == 1){
				isolatedMarkers[companyIndex] = true;
			}
		}

		for(int i = 0; i < numOfCompanies; i++){
			if(newValue[i] == 0 && isolatedMarkers[i] == true){
				newValue[i] = 1; // The selected company only holds isolated markers
			}
			if(newValue[i] >= 15) {
				newValue[i] = 15; // 15 is the maximum value
				gameOver = true;
			}
			int oldPrice = Companies[i].getSharePrice();
			int newPrice = 1000*newValue[i];
			
			Companies[i].setSharePriceOld(oldPrice);
			Companies[i].setSharePrice(newPrice);
			
			if(oldPrice > newPrice){
				anyLosses = true;
			}
		}
		
		return anyLosses;
	}
	
	private static void destroyChains(int row, int column){
		int adjacentFields[] = {-1,-1,-1,-1}; // Order: Top, Right, Bottom, Left
		int selectedChainIndex = gui.board[row][column].getChainIndex();
		
		// TOP
		if(row > 0){
			int chainIndex = gui.board[row-1][column].getChainIndex();
			if((chainIndex >= 0) && (chainIndex != selectedChainIndex)){
				adjacentFields[0] = chainIndex;
			}
		}
		// RIGHT
		if(column < 11){
			int chainIndex = gui.board[row][column+1].getChainIndex();
			if(chainIndex >= 0 && (chainIndex != selectedChainIndex)){
				adjacentFields[1] = chainIndex;
			}
		}
		// BOTTOM
		if(row < 9){
			int chainIndex = gui.board[row+1][column].getChainIndex();
			if(chainIndex >= 0 && (chainIndex != selectedChainIndex)){
				adjacentFields[2] = chainIndex;
			}
		}
		// LEFT
		if(column > 0 ){
			int chainIndex = gui.board[row][column-1].getChainIndex();
			if(chainIndex >= 0 && (chainIndex != selectedChainIndex)){
				adjacentFields[3] = chainIndex;
			}
		}
		
		for(int h = 0; h < numOfCompanies; h++) {
			int rivalChainIndex = adjacentFields[h];
			if(rivalChainIndex != -1){
				int[] listElement = chainList.get(rivalChainIndex);
				listElement[1] = 0; // Set chain length to zero
				
				for(int i = 0; i < 10; i++){
					for(int j = 0; j < 12; j++){
						if(gui.board[i][j].getChainIndex() == rivalChainIndex) {
							// Reset Company Marker
							gui.board[i][j].setCompanyIndex(SharkConstants.NONE);
							gui.board[i][j].setChainIndex(SharkConstants.NONE);
							gui.board[i][j].setSet(false);
							gui.board[i][j].setContentAreaFilled(false);
						}
					}
				}
			}
		}
	}
	
	private static void updateGui(){
		// Update Player Info Table
		for(int i = 0; i < numOfPlayers; i++){
			if(Players[i].getActivityStatus()) {
				for(int j = 0; j < numOfCompanies; j++) {
					gui.setNumOfShares(i, j, Players[i].getShares(j));
				}
				
				gui.setCash(i, Players[i].getCash());
			}
		}
		// Update Company Info Table
		for(int i = 0; i < numOfCompanies; i++){
			gui.setSharePrice(Companies[i].getCompanyIndex(), Companies[i].getSharePrice());
			gui.setRemainingShares(Companies[i].getCompanyIndex(), Companies[i].getRemainingShares());
			gui.setRemainingMarkers(Companies[i].getCompanyIndex(), Companies[i].getRemainingMarkers());
		}	
	}
	
	private static void activePlayerReceivesSuccessFee(){
		int successFee = 0;
		if(activeCompany.getSharePrice() > activeCompany.getSharePriceOld()){
			successFee = activeCompany.getSharePrice();
		}
		else{
			successFee = 1000; // Only 1000 FT, if player sets an isolated marker
		}
		
		activePlayer.addCash(successFee);
		gui.addLineToPayoutSummary("Success Fee:");
		gui.addLineToPayoutSummary(activePlayer.getName()+" receives a success fee of "+String.valueOf(successFee)+" FT");
	}


	private static void playersReceiveDividends(){
		boolean titleAlreadyPrinted = false;
		
		for(int i = 0; i < numOfPlayers; i++){
			int dividend = (activeCompany.getSharePrice() - activeCompany.getSharePriceOld()) * Players[i].getShares(activeCompany.getCompanyIndex());
			if(dividend > 0){
				if(!titleAlreadyPrinted){
					gui.addLineToPayoutSummary("Dividends:");
					titleAlreadyPrinted = true;
				}
				Players[i].addCash(dividend);
				gui.addLineToPayoutSummary(Players[i].getName()+" receives a dividend of "+String.valueOf(dividend)+" FT");
			}
		}
	}
	
	
	private static void playersPayForLosses(int startIndex){
		for(int i = startIndex; i < numOfPlayers; i++){
			if(i != activePlayer.getPlayerIndex()){
				int totalDebt = 0;
				boolean anyLosses = false;
				
				// Calculate total debt
				for(int j = 0; j < numOfCompanies; j++) {
					int oldPrice = Companies[j].getSharePriceOld();
					int currentPrice = Companies[j].getSharePrice();
					if(oldPrice > currentPrice){
						int numOfShares = Players[i].getShares(j);
						if(numOfShares > 0){							
							totalDebt += (oldPrice - currentPrice) * numOfShares;
						}
						anyLosses = true;
					}
				}
				
				if(anyLosses){
					// Pay losses (completely or partially)
					if(totalDebt > 0){
						int cash = Players[i].getCash();
						if(cash >= totalDebt){
							cash -= totalDebt;
							Players[i].setCash(cash);
							gui.addLineToPayoutSummary(Players[i].getName()+" paid "+String.valueOf(totalDebt)+" FT for losses.");
							totalDebt = 0;			
						}
						else{
							totalDebt -= cash;
							Players[i].setCash(0);
							gui.addLineToPayoutSummary(Players[i].getName()+" could not pay all the debts -> Forced Sale");
						}
					}
					// Forced Sale
					if(totalDebt > 0){
						if(isPlayerSolvent(Players[i], totalDebt)){
							forcedSaleIndicator = true;
							forcedSalePlayer = Players[i];
							forcedSalePlayer.setDebt(totalDebt);
							gui.setForcedSalePlayerLabel(forcedSalePlayer.getName());
							gui.setForcedSaleBalance((-1)*totalDebt);
							gui.sellButtons[2].setEnabled(true);
							gui.nextButtons[2].setEnabled(true);
							updateGui();
							return;
						}
						else{
							// Player can't pay all the debts
							gui.addLineToPayoutSummary(Players[i].getName()+" is insolvent and is removed from the game!");
							Players[i].disablePlayer();
							sellAllShares(i);
							Players[i].setCash(0);
							gui.removePlayer(i);
						}
					}
				}
			}
		}
		gui.nextButtons[2].setEnabled(true);
		updateGui();
	}
	
	private static boolean isPlayerSolvent(Player player, int debt){
		boolean status = false;
		int sum = 0;
		
		for(int i = 0; i < numOfCompanies; i++) {
			 sum += ((int)((Companies[i].getSharePrice()*player.getShares(i))/2000))*1000;
		}
		
		if(sum >= debt){
			status = true;
		}
		
		return status;
	}

	private static void setActivePlayerName(){
		gui.setPlayerInfo(activePlayer.getName());
	}
	
	private static void resetInfoBoxes(){
		gui.setBrokerInfoText(SharkConstants.TAB_1_BUY_AND_SELL,"...");
		gui.resetPanelStep2();
		gui.resetPayoutSummary();
		gui.setBrokerInfoText(SharkConstants.TAB_4_BUY_AND_SELL,"...");
	}
	
	private static void sellAllShares(int playerIndex) {
		for(int j = 0; j < numOfCompanies; j++) {
			int sharePrice = Companies[j].getSharePrice();
			int numOfShares = Players[playerIndex].getShares(Companies[j].getCompanyIndex());
			int price = sharePrice * numOfShares;
				
			Players[playerIndex].setShares(j, 0);
			Players[playerIndex].setCash(Players[playerIndex].getCash() + price);
			Companies[j].setRemainingShares(Companies[j].getRemainingShares() + numOfShares);
		}
	}
	
	private static void gameOverRoutine(){
		for(int i = 0; i < numOfPlayers; i++) {
			sellAllShares(i);
		}
		int[][] ranking = determineWinner();
		
		gui.addLineToPayoutSummary("--- GAME OVER ---");
		
		updateGui();
		
		JDialog d = new JDialog(gui);
		
		String text = "Summary:";
		
		for(int i = 0; i < numOfPlayers; i++) {
			text = text+"<br>"+String.valueOf(ranking[i][1])+". "+Players[ranking[i][0]].getName()+"   ("+String.valueOf(Players[ranking[i][0]].getCash())+" FT)";
		}
		
		JLabel label = new JLabel("<html><body>"+text+"</body></html>");
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		label.setVerticalAlignment(JLabel.TOP);
		
		d.add(label);
        
		d.setResizable(false);
        d.setTitle("Game Over");
		d.setLocationRelativeTo(null);
		d.setSize(250,200);
		d.setVisible(true);
	}
	
	private static int[][] determineWinner() {
		ArrayList<Integer> cashList = new ArrayList<Integer>();
		
        for (int i = 0; i < numOfPlayers; i++) {
            if (!cashList.contains(Players[i].getCash())) {
                cashList.add(Players[i].getCash());
            }
        }
        
        Collections.sort(cashList,Collections.reverseOrder());
        
        int[][] ranking = new int[numOfPlayers][2];
        
        for(int i = 0; i < numOfPlayers; i++) {
        	ranking[i][0] = i;
        	ranking[i][1] = cashList.indexOf(Players[i].getCash())+1;
        }
        
        Arrays.sort(ranking, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
               if(a[1] > b[1]) return 1;
               else return -1;
            }
        });
        
        return ranking;
	}
	
}
