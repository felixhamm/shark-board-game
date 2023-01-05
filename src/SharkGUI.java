import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SharkGUI extends JFrame {	
	private String[] playerList;
	
	private final String radioList[] = { "Green", "Red", "Blue", "Yellow", "Reset" };
	private final String companyList[] = {	"NYSO (Green)", 
											"Smith & Smith (Red)",
											"Empire (Blue)",
											"Wings (Yellow)"};
	
	private final String title1[] = {"Player", "NYSO", "Smith & Smith", "Empire", "Wings", "Cash"};
	private final String[][] data1 = new String[6][6];
	private final String title2[] = {"", "NYSO", "Smith & Smith", "Empire", "Wings"};
	private final String data2[][] = {	{"Share Price", "0", "0", "0", "0"},
										{"Remaining Shares", "40", "40", "40", "40"},
										{"Remaining Markers", "18", "18", "18", "18"}};
    
	
	final Color labelBackgroundColor = new Color(236, 204, 137);
	
	// World Map
	public CompanyMarker board[][] = new CompanyMarker[10][12];
	
	// Table 1
	private DefaultTableModel tableModel1;
	
	// Bar Chart
	public BarChart shareRanking;
	
	// Table 2
	private DefaultTableModel tableModel2;
	
	// Player Info
	private JLabel playerInfo;
	
	// Tabbed Pane
	public JTabbedPane tabbedPaneSteps;
	/**
	 * <b>Structure:</b><br>
     * nextButtons[0]: Step 1: Buy & Sell <br>
     * nextButtons[1]: Step 2: Roll the Dice <br>
     * nextButtons[2]: Step 3: Set Company Marker <br>
     * nextButtons[3]: Step 4: Buy & Sell <br>
     * nextButtons[4]: Step 5: Forced Sale
     */
	public JButton nextButtons[] = new JButton[5];
	/**
	 * <b>Structure:</b><br>
     * buyButtons[0]: Step 1: Buy & Sell <br>
     * buyButtons[1]: Step 4: Buy & Sell
     */
	public JButton buyButtons[] = new JButton[2];
	/**
	 * <b>Structure:</b><br>
     * sellButtons[0]: Step 1: Buy & Sell <br>
     * sellButtons[1]: Step 4: Buy & Sell <br>
     * sellButtons[1]: Step 5: Forced Sale
     */
	public JButton sellButtons[] = new JButton[3];
	
	// Step 1: Buy & Sell
	private MyBroker panelStep1;
	
	// Step 2: Roll the Dice
	private JLabel regionLabel;
	private JLabel colorInfoLabel;
	private JRadioButton radioButtons[] = new JRadioButton[radioList.length];
	
	public JButton diceBtn;
	
	// Step 3: Set Company Marker
	private JTextArea payoutSummary;
	
	// Step 4: Buy & Sell
	private MyBroker panelStep4;
	
	// Step 5: Forced Sale
	private JLabel forcedSalePlayerLabel;
	private JSpinner spinner_step5;
	private JComboBox combobox_step5;
	private JTextField textfield_step5;
	
	
	// Constructor
	public SharkGUI(String[] playerList)
	{
		this.playerList = playerList;
		
		// Main Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    
	    
		// Image Panel
		ImagePanel worldMapPanel = new ImagePanel(
		        new ImageIcon("images/world_map_small.jpg").getImage());
		
		worldMapPanel.setLayout(new GridLayout(10, 12));
		
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 12; j++){
				board[i][j] = new CompanyMarker();
		        board[i][j].setContentAreaFilled(false);
		        
		        worldMapPanel.add(board[i][j]);
			}
		}
		
		
		// Table Panel 1
		for(int i = 0; i < playerList.length; i++) {
			data1[i][0]= playerList[i];
			for(int j = 1; j < 6; j++) {
				data1[i][j]= "0";
			}
		}
		
		tableModel1 = new DefaultTableModel(data1, title1);
		JTable table1 = new JTable(tableModel1);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment( JLabel.RIGHT );
		for(int i = 0; i < title1.length; i++) {
			table1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		//table1.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		table1.setEnabled(false);
		setColumnWidths(table1,95,50,100,50,50,50);
		JScrollPane tablePanel1 = new JScrollPane(table1);
		tablePanel1.setPreferredSize(new Dimension(395,119));
		
		
		// Bar Chart
		String title = "Share Ranking";
	    int[] values = new int[]{0,0,0,0};
	    String[] bcLabels = new String[]{"NYSO", "Smith & Smith", "Empire", "Wings"};
	    Color[] colors = new Color[]{
	        Color.green,
	        Color.red,
	        Color.blue,
	        Color.yellow
	    };

	    Dimension bcSize = new Dimension(360, 250);
        shareRanking = new BarChart(values, bcLabels, colors, title, bcSize);
        shareRanking.setPreferredSize(bcSize);
        shareRanking.setLayout(new BorderLayout());
		
		// Table Panel 2
        tableModel2 = new DefaultTableModel(data2, title2);
		JTable table2 = new JTable(tableModel2);
		for(int i = 0; i < title2.length; i++) {
			table2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		table2.setEnabled(false);
		setColumnWidths(table2,120,50,100,50,50);
		
		JScrollPane tablePanel2 = new JScrollPane(table2);
		tablePanel2.setPreferredSize(new Dimension(370,71));
		
		// Player Info
		int playerInfoWidth = 240;
		int playerInfoHeight = 100;
		playerInfo = new JLabel("<html><body>Player A's<br>Turn</body></html>", SwingConstants.CENTER);
		playerInfo.setMinimumSize(new Dimension(playerInfoWidth, playerInfoHeight));
		playerInfo.setPreferredSize(new Dimension(playerInfoWidth, playerInfoHeight));
		playerInfo.setMaximumSize(new Dimension(playerInfoWidth, playerInfoHeight));
        playerInfo.setFont(new Font(null,Font.BOLD, 28));
        playerInfo.setOpaque(true);
        playerInfo.setBackground(labelBackgroundColor);
        //Border playerInfoBorder = BorderFactory.createCompoundBorder(new LineBorder(Color.darkGray, 4),new EmptyBorder(0, 60, 0, 60));
        Border playerInfoBorder = BorderFactory.createLineBorder(Color.darkGray, 4);
        playerInfo.setBorder(playerInfoBorder);
        
        // Tapped Pane Steps
        // Step 1: Buy & Sell
        panelStep1 = new MyBroker(companyList);
                
        
        // Step 2: Roll the Dice
        JPanel panelStep2 = new JPanel();
        panelStep2.setLayout(new GridBagLayout());
        
        diceBtn = new JButton(new ImageIcon("images/dice_icon.png"));
        JButton nextBtn_step2 = new JButton(new ImageIcon("images/next_icon.png"));
        
        Font labelFontStep2 = new Font(null,Font.BOLD, 16);
        Border labelBorderStep2 = BorderFactory.createLineBorder(Color.darkGray, 2);
        
        JLabel label1_step2 = new JLabel("Region", SwingConstants.CENTER);
        label1_step2.setFont(labelFontStep2);
        label1_step2.setOpaque(true);
        label1_step2.setBackground(labelBackgroundColor);
        label1_step2.setBorder(labelBorderStep2);
        regionLabel = new JLabel("4", SwingConstants.CENTER);
        regionLabel.setFont(new Font(null,Font.BOLD, 48));
        JLabel label3_step2 = new JLabel("Color", SwingConstants.CENTER);
        label3_step2.setFont(labelFontStep2);
        label3_step2.setOpaque(true);
        label3_step2.setBackground(labelBackgroundColor);
        label3_step2.setBorder(labelBorderStep2);
        colorInfoLabel = new JLabel("You're lucky! Choose a color.", SwingConstants.CENTER);
        
        ButtonGroup radioBtnGroup = new ButtonGroup();
        
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(1,radioList.length));
		
		for (int i = 0; i < radioList.length; i++) {
			radioButtons[i] = new JRadioButton (radioList[i], false);
			radioBtnGroup.add(radioButtons[i]);
			radioPanel.add(radioButtons[i]);
		}
		
		radioButtons[0].setSelected(true);
		
		panelStep2.add(diceBtn,getGBC(0, 0, 1, 3, VERTICAL, 0.0, 1.0, LINE_START, new Insets(5,5,5,10)));
		panelStep2.add(label1_step2,getGBC(1, 0, 1, 1, BOTH, 1.0, 0.0, CENTER, new Insets(5,10,10,10)));
		panelStep2.add(regionLabel,getGBC(1, 1, 1, 2, BOTH, 0.0, 0.0, CENTER, new Insets(5,10,20,10)));
		panelStep2.add(label3_step2,getGBC(2, 0, 1, 1, BOTH, 1.0, 0.0, CENTER, new Insets(5,10,10,10)));
		panelStep2.add(colorInfoLabel,getGBC(2, 1, 1, 1, BOTH, 1.0, 0.0, CENTER, new Insets(5,10,5,10)));
		panelStep2.add(radioPanel,getGBC(2, 2, 1, 1, BOTH, 0.0, 0.0, CENTER, new Insets(5,10,20,10)));
		panelStep2.add(nextBtn_step2,getGBC(3, 0, 1, 3, VERTICAL, 0.0, 1.0, LINE_END, new Insets(5,10,5,5)));
        
		
        // Step 3: Set Company Marker
        JPanel panelStep3 = new JPanel();
        panelStep3.setLayout(new GridBagLayout());
        
        JLabel label1_step3 = new JLabel("Payout Summary:");
        payoutSummary = new JTextArea(6, 50);
        payoutSummary.setText(	"Player 1 set a blue marker in region 4\n" + "Player 1 gets 1000 FT\n" + "Player 2 gets 2000 FT\n" + "...\n");
        payoutSummary.setLineWrap(true);
        payoutSummary.setWrapStyleWord(true);
        payoutSummary.setEditable(false);
        Border payoutSummaryBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
        payoutSummary.setBorder(payoutSummaryBorder);
        JScrollPane scrollpane_step3 = new JScrollPane(payoutSummary);
        JButton nextBtn_step3 = new JButton(new ImageIcon("images/next_icon.png"));
        
        panelStep3.add(label1_step3,getGBC(0, 0, 1, 1, BOTH, 1.0, 0.0, FIRST_LINE_START, new Insets(5,10,10,0)));
        panelStep3.add(scrollpane_step3,getGBC(0, 1, 1, 1, BOTH, 1.0, 0.0, LINE_START, new Insets(0,10,10,10)));
        panelStep3.add(nextBtn_step3,getGBC(1, 0, 1, 2, VERTICAL, 0.0, 1.0, LINE_END, new Insets(5,10,5,5)));
        
        
        // Step 4: Buy & Sell
        panelStep4 = new MyBroker(companyList);
        
        
        // Step 5: Forced Sale
        JPanel panelStep5 = new JPanel();
        panelStep5.setLayout(new GridBagLayout());
	    
        JLabel label1_step5 = new JLabel("Player:");
        forcedSalePlayerLabel = new JLabel("[...]",SwingConstants.CENTER);
	    JLabel label3_step5 = new JLabel("Quantity:");
	    spinner_step5 = new JSpinner(new SpinnerNumberModel(0,0,40,1));
	    JLabel label4_step5 = new JLabel("Company:");
	    combobox_step5 = new JComboBox(companyList);
	    JButton sellBtn_step5 = new JButton("Sell");
	    JLabel label5_step5 = new JLabel("Balance:");
	    textfield_step5 = new JTextField("- 5000 FT");
	    textfield_step5.setEditable(false);
	    textfield_step5.setForeground(Color.red);
	    textfield_step5.setFont(new Font(null,Font.BOLD, 12));
	    textfield_step5.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
	    JTextArea forcedSaleInfo = new JTextArea();
	    forcedSaleInfo.setText(	"Info:\n" + 
	    						"If a player does not have enough cash to pay the debt, then he or she must pay in the form of company shares. " +
	    						"In such a forced sale, the company shares that have to be sold to pay the debt are only worth half. " + 
	    						"If this forced sale results in partial amounts of less than 1000 FT, then these amounts are rounded down.");
	    
	    forcedSaleInfo.setLineWrap(true);
	    forcedSaleInfo.setWrapStyleWord(true);
	    forcedSaleInfo.setEditable(false);
	    forcedSaleInfo.setBackground(labelBackgroundColor);
	    forcedSaleInfo.setFont(new Font(null,Font.BOLD, 12));
	    Border forcedSaleInfoBorder = BorderFactory.createCompoundBorder(new LineBorder(Color.darkGray, 2),new EmptyBorder(5, 10, 5, 10));;
	    forcedSaleInfo.setBorder(forcedSaleInfoBorder);
	    JButton nextBtn_step5 = new JButton(new ImageIcon("images/next_icon.png"));
	    
	    // Grid Bag Layout
	    panelStep5.add(label1_step5,getGBC(0, 0, 1, 1, BOTH, 0.0, 0.0, LINE_START, new Insets(5,10,10,10)));
	    panelStep5.add(forcedSalePlayerLabel,getGBC(1, 0, 2, 1, BOTH, 0.0, 0.0, CENTER, new Insets(5,10,10,10)));
	    panelStep5.add(label3_step5,getGBC(0, 1, 1, 1, BOTH, 0.0, 0.0, LINE_START, new Insets(5,10,5,10)));
	    panelStep5.add(spinner_step5,getGBC(2, 1, 1, 1, BOTH, 0.0, 0.0, LINE_END, new Insets(5,5,5,10),0,5));
	    panelStep5.add(label4_step5,getGBC(0, 2, 1, 1, BOTH, 0.0, 0.0, CENTER, new Insets(5,10,5,10)));
	    panelStep5.add(combobox_step5,getGBC(1, 2, 2, 1, NONE, 0.0, 0.0, LINE_END, new Insets(5,10,5,10)));
	    panelStep5.add(label5_step5,getGBC(0, 3, 1, 1, BOTH, 0.0, 0.0, CENTER, new Insets(5,10,5,10)));
	    panelStep5.add(textfield_step5,getGBC(1, 3, 1, 1, BOTH, 0.5, 0.0, LINE_START, new Insets(5,10,5,5)));
	    panelStep5.add(sellBtn_step5,getGBC(2, 3, 1, 1, NONE, 0.0, 0.0, LINE_END, new Insets(5,5,5,10)));
	    panelStep5.add(forcedSaleInfo,getGBC(3, 0, 1, 4, BOTH, 1.0, 0.0, CENTER, new Insets(5,15,5,10)));
	    panelStep5.add(nextBtn_step5,getGBC(4, 0, 1, 4, VERTICAL, 0.0, 1.0, LINE_END, new Insets(5,10,5,5)));
 
        // Tabbed Pane Button Arrays
	    nextButtons[0] = panelStep1.nextBtn;
	    nextButtons[1] = nextBtn_step2;
	    nextButtons[2] = nextBtn_step3;
	    nextButtons[3] = panelStep4.nextBtn;
	    nextButtons[4] = nextBtn_step5;
	    
	    buyButtons[0] = panelStep1.buyBtn;
	    buyButtons[1] = panelStep4.buyBtn;
	    
	    sellButtons[0] = panelStep1.sellBtn;
	    sellButtons[1] = panelStep4.sellBtn;
	    sellButtons[2] = sellBtn_step5;
        
	    
        tabbedPaneSteps = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
 
        tabbedPaneSteps.addTab("I. Buy & Sell", panelStep1);
        tabbedPaneSteps.addTab("II. Roll the Dice", panelStep2);
        tabbedPaneSteps.addTab("III. Set Marker", panelStep3);
        tabbedPaneSteps.addTab("IV. Buy & Sell", panelStep4);
        tabbedPaneSteps.addTab("Forced Sale [!]", panelStep5);
        
        
        // Main Panel - GridBagLayout
        
        mainPanel.add(worldMapPanel,getGBC(0, 0, 2, 3, NONE, 0.0, 0.0, CENTER, new Insets(5,10,10,10)));
	    mainPanel.add(tablePanel1,getGBC(2, 0, 1, 1, NONE, 0.0, 0.0, PAGE_START, new Insets(5,0,10,10)));
		mainPanel.add(shareRanking,getGBC(2, 1, 1, 1, NONE, 0.0, 0.0, PAGE_START, new Insets(10,0,10,10)));
	    mainPanel.add(tablePanel2,getGBC(2, 2, 1, 1, NONE, 0.0, 0.0, PAGE_START, new Insets(5,0,10,10)));
    	mainPanel.add(playerInfo,getGBC(0, 3, 1, 1, BOTH, 0.0, 0.0, CENTER, new Insets(0,10,0,10)));
        mainPanel.add(tabbedPaneSteps,getGBC(1, 3, 2, 1, BOTH, 1.0, 0.0, CENTER, new Insets(0,0,0,10)));
        
        
        //-------------------------------------
        
        // Frame
        JFrame frame = new JFrame();
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setResizable(false);
		
		frame.setTitle("Shark - The stock-trading game");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(1020,720);
		frame.setVisible(true);
	
		
	}
	
	// GUI Methods
	// World Map Board
	public void disableBoard() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 12; j++) {
		        board[i][j].setEnabled(false);
		    }
		}
	}
	
	// Table 1	
	public void setNumOfShares(int playerIndex, int companyIndex, int value) {
		tableModel1.setValueAt(String.valueOf(value), playerIndex, companyIndex);
	}
	
	public void setCash(int playerIndex, int value) {
		tableModel1.setValueAt(String.valueOf(value), playerIndex, 5);
	}
	
	// Table 2
	public void setSharePrice(int companyIndex, int value) {
		int maxPrice = 15000;
		if((value >= 0) && (value <= maxPrice) && (value%1000 == 0)) {
			tableModel2.setValueAt(value, 0, companyIndex + 1);
			shareRanking.setSpecificValue(companyIndex, (int)(value/1000));
		} else {
			System.out.println("Error: " + value + " is not a valid share price");
		}
	}
	
	public void setRemainingShares(int companyIndex, int value) {
		tableModel2.setValueAt(String.valueOf(value), 1, companyIndex + 1);
	}
	
	public void setRemainingMarkers(int companyIndex, int value) {
		tableModel2.setValueAt(String.valueOf(value), 2, companyIndex + 1);
	}
	
	// Player Info
	public void setPlayerInfo(String name) {
		this.playerInfo.setText("<html><body><center>"+ name +"'s<br>Turn</center></body></html>");
	}
	
	// Step 1: Buy & Sell
	// none
	
	// Step 2: Roll the Dice
	public void resetPanelStep2() {
		regionLabel.setText("?");
		colorInfoLabel.setText("Roll the Dice!");
		radioButtonsSetVisible(false);
	}
	
	public void setRegionLabel(int region) {
		if((region >= 1) && (region <= 6)) {
			regionLabel.setText(String.valueOf(region));
		}
	}
	
	public void setColorBtn(int number) {
		radioButtonsSetVisible(true);
		radioButtonsSetEnabled(true);
		
		if((number >= 1) && (number <= 6)) {
			if(number <= 4) {
				int i = number-1;
				colorInfoLabel.setText("Diced Color: " + radioList[i]);
				radioButtons[i].setSelected(true);
				radioButtonsSetEnabled(false);
			}
			else {
				colorInfoLabel.setText("You're lucky! Choose a color.");
			}
		}
	}
	
	public int getSelectedRadioBtnIndex() {
		int index = -1;
		
		for (int i = 0; i < radioButtons.length; i++) {
			if(radioButtons[i].isSelected()) {
				index = i;
			}
		}
		
		if(index == -1) {
			System.out.println("Error: No Radio Button is selected!"); 
		}
		
		return index;
	}
	
	// Step 3: Set Company Marker
	public void addLineToPayoutSummary(String newText) {
		String text = payoutSummary.getText();
		payoutSummary.setText(text + newText);
	}
	
	public void resetPayoutSummary() {
		payoutSummary.setText("");
	}
	
	// Step 4: Buy & Sell
	// none
	
	// Step 5: Forced Sale
	public void setForcedSalePlayerLabel(String player) {
		forcedSalePlayerLabel.setText(player);
	}
	
	public void setForcedSaleBalance(int value) {
		String text;
		
		if(value%1000 == 0) {
			if(value >= 0) {
				text = String.valueOf(value) + " FT";
				textfield_step5.setForeground(Color.black);
			} else {
				text = "- " + String.valueOf(Math.abs(value)) + " FT";
				textfield_step5.setForeground(Color.red);
			}
			textfield_step5.setText(text);
		} else {
			System.out.println("Error: " + value + " is not a valid balance value");
		}
	}
	
	// Buy & Sell Methods
	public int getQuantity(int tab) {
		int result = -1;
		
		switch(tab){
		case SharkConstants.TAB_1_BUY_AND_SELL:
			result = (int)(panelStep1.spinner.getValue());
			break;
		case SharkConstants.TAB_4_BUY_AND_SELL:
			result = (int)(panelStep4.spinner.getValue());
			break;
		case SharkConstants.TAB_5_FORCED_SALE:
			result = (int)(spinner_step5.getValue());
			break;
		default:
			System.out.println("Invalid tab for getQuantity()");
			break;
		}
		
		return result;
	}
	
	public int getCompanyIndex(int tab) {
		int result = -1;
		
		switch(tab){
		case SharkConstants.TAB_1_BUY_AND_SELL:
			result = panelStep1.combobox.getSelectedIndex();
			break;
		case SharkConstants.TAB_4_BUY_AND_SELL:
			result = panelStep4.combobox.getSelectedIndex();
			break;
		case SharkConstants.TAB_5_FORCED_SALE:
			result = combobox_step5.getSelectedIndex();
			break;
		default:
			System.out.println("Invalid tab for getCompanyIndex()");
			break;
		}
		
		return result;
	}
	
	public void setBrokerInfoText(int tab, String text) {
		switch(tab){
		case SharkConstants.TAB_1_BUY_AND_SELL:
			panelStep1.textfield.setText(text);
			break;
		case SharkConstants.TAB_4_BUY_AND_SELL:
			panelStep4.textfield.setText(text);
			break;
		default:
			System.out.println("Invalid tab for setBrokerInfoText()");
			break;
		}
	}
	
	// Private Methods
	private void radioButtonsSetEnabled (boolean buttonsEnabled) {
		for(int i = 0; i < radioButtons.length; i++) {
			radioButtons[i].setEnabled(buttonsEnabled);
		}
	}
	
	private void radioButtonsSetVisible (boolean buttonsVisible) {
		for(int i = 0; i < radioButtons.length; i++) {
			radioButtons[i].setVisible(buttonsVisible);
		}
	}
	
	private void setColumnWidths(JTable table, int... widths) {
	    TableColumnModel columnModel = table.getColumnModel();
	    for (int i = 0; i < widths.length; i++) {
	        if (i < columnModel.getColumnCount()) {
	            columnModel.getColumn(i).setMaxWidth(widths[i]);
	        }
	        else break;
	    }
	}
	
	// Custom GridBagLayout Methods in order to set the GridBagConstraints
	// Source: https://wiki.byte-welt.net/wiki/GridBagLayout
	
	/*
    private GridBagConstraints getGBC(int gridx, int gridy) {
    	return getGBC(gridx, gridy, 1, 1, NONE, 0.0, 0.0, CENTER, new Insets(0, 0, 0, 0), 0, 0);
    }
    private GridBagConstraints getGBC(int gridx, int gridy, int gridwidth) {
    	return getGBC(gridx, gridy, gridwidth, 1, NONE, 0.0, 0.0, CENTER, new Insets(0, 0, 0, 0), 0, 0);
    }
    private GridBagConstraints getGBC(int gridx, int gridy, int gridwidth, int gridheight) {
    	return getGBC(gridx, gridy, gridwidth, gridheight, NONE, 0.0, 0.0, CENTER, new Insets(0, 0, 0, 0), 0, 0);
    }
    private GridBagConstraints getGBC(int gridx, int gridy, int gridwidth, int gridheight, int fill) {
    	return getGBC(gridx, gridy, gridwidth, gridheight, fill, 0.0, 0.0, CENTER, new Insets(0, 0, 0, 0), 0, 0);
    }
    private GridBagConstraints getGBC(int gridx, int gridy, double weightx, double weighty) {
    	return getGBC(gridx, gridy, 1, 1, NONE, weightx, weighty, CENTER, new Insets(0, 0, 0, 0), 0, 0);
    }
    private GridBagConstraints getGBC(int gridx, int gridy, double weightx, double weighty, int ipadx, int ipady) {
    	return getGBC(gridx, gridy, 1, 1, NONE, weightx, weighty, CENTER, new Insets(0, 0, 0, 0), ipadx, ipady);
    }
    private GridBagConstraints getGBC(int gridx, int gridy, double weightx, double weighty, int anchor) {
    	return getGBC(gridx, gridy, 1, 1, NONE, weightx, weighty, anchor, new Insets(0, 0, 0, 0), 0, 0);
    }
    
	*/
	private GridBagConstraints getGBC(int gridx, int gridy, int gridwidth, int gridheight, int fill, double weightx, double weighty, int anchor, Insets insets) {
    	return getGBC(gridx, gridy, gridwidth, gridheight, fill, weightx, weighty, anchor, insets, 0, 0);
    }
	
    private GridBagConstraints getGBC(int gridx, int gridy, int gridwidth, int gridheight, int fill, double weightx,
    		double weighty, int anchor, Insets insets, int ipadx, int ipady) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = fill;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        constraints.insets = insets;
        constraints.ipadx = ipadx;
        constraints.ipady = ipady;
        return constraints;
    }
    
// ---------------------------------------------------------------------------------------
    
    // Additional Inner Classes

    class ImagePanel extends JPanel {

    	  private Image img;

    	  public ImagePanel(String img) {
    	    this(new ImageIcon(img).getImage());
    	  }

    	  public ImagePanel(Image img) {
    	    this.img = img;
    	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    	    setPreferredSize(size);
    	    setMinimumSize(size);
    	    setMaximumSize(size);
    	    setSize(size);
    	    setLayout(null);
    	  }

    	  public void paintComponent(Graphics g) {
    	    g.drawImage(img, 0, 0, null);
    	  }

    }
    
    class CompanyMarker extends JButton {
    	
    }

    // Source: https://www.javacodex.com/Graphics/Bar-Chart
    class BarChart extends JPanel {
    	private int[] values;
    	private String[] labels;
    	private Color[] colors;
    	private String title;
    	private Dimension size;
    	 
    	public BarChart(int[] values, String[] labels, Color[] colors, String title, Dimension size) {
    		this.labels = labels;
    	    this.values = values;
    	    this.colors = colors;
    	    this.title = title;
    	    this.size = size;
    	}
    	
    	public void setValues(int[] values) {
    		this.values = values;
    		this.repaint();
    	}
    	
    	public void setSpecificValue(int index, int value) {
    		this.values[index] = value;
    		this.repaint();
    	}
    	 
    	public void paintComponent(Graphics g) {
    	    super.paintComponent(g);
    	    if (values == null || values.length == 0) {
    	      return;
    	    }
    	 
    	    int minValue = 0;
    	    int maxValue = 15;
    	    
    	    int xOffset = 20;
    	 
    	    // Set Dimension
    	    setPreferredSize(size);
    	    setMinimumSize(size);
    	    setMaximumSize(size);
    	    setSize(size);
    	    setLayout(null);
    	    int panelWidth = size.width;
    	    int panelHeight = size.height;
    	    int barWidth = (panelWidth - xOffset) / values.length;
    	 
    	    Font titleFont = new Font(null, Font.BOLD, 15);
    	    FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
    	 
    	    Font labelFont = new Font(null, Font.PLAIN, 14);
    	    FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);
    	 
    	    int titleWidth = titleFontMetrics.stringWidth(title);
    	    int stringHeight = titleFontMetrics.getAscent();
    	    int stringWidth = (panelWidth - titleWidth) / 2;
    	    g.setFont(titleFont);
    	    g.drawString(title, stringWidth, stringHeight);
    	    
    	    int top = titleFontMetrics.getHeight();
    	    int bottom = labelFontMetrics.getHeight();
    	    if (maxValue == minValue) {
    	      return;
    	    }
    	    double scale = (panelHeight - top - bottom) / (maxValue - minValue);
    	    stringHeight = panelHeight - labelFontMetrics.getDescent();
    	    g.setFont(labelFont);
    	    
    	    // Draw Grid
    	    int delta = 5;
    	    for (int i = 0; i <= maxValue; i++) {
    	    	int y = maxValue-i;
    	    	String numString;
    	    	
    	    	if(y % delta == 0) {
    	    		if(y < 10) {
    		    		numString = "  " + String.valueOf(y);
    		    	}
    		    	else {
    		    		numString = String.valueOf(y);
    		    	}
    	    		g.setColor(Color.black);
    		    	g.drawString(numString,0,top+(int)(i*scale)+labelFontMetrics.getDescent());
    	    	}
    	    	else {
    	    		g.setColor(Color.lightGray);
    	    	}
    	    	g.drawLine(20,top+(int)(i*scale),panelWidth-1,top+(int)(i*scale));
    	    }
    	    g.drawLine(20,top,20,top+(int)(maxValue*scale));
    	    g.drawLine(panelWidth-1,top,panelWidth-1,top+(int)(maxValue*scale));
    	    
    	    // Draw Bars and Labels
    	    for (int j = 0; j < values.length; j++) {
    	      if (values[j] < 0) {
    	        return;
    	      }
    	      int xSpacing = 10;
    	      int valueP = j * barWidth + xOffset + xSpacing;
    	      int valueQ = top;
    	      int height = (int) (values[j] * scale);
    	      valueQ += (int) ((maxValue - values[j]) * scale);
    	 
    	      g.setColor(colors[j]);
    	      g.fillRect(valueP, valueQ, barWidth - 2*xSpacing, height);
    	      g.setColor(Color.black);
    	      g.drawRect(valueP, valueQ, barWidth - 2*xSpacing, height);
    	 
    	      int labelWidth = labelFontMetrics.stringWidth(labels[j]);
    	      stringWidth = j * barWidth + xOffset + (barWidth - labelWidth) / 2;
    	      g.drawString(labels[j], stringWidth, stringHeight);
    	   }
    	}
    }

    class MyBroker extends JPanel{
    	public JSpinner spinner;
	    public JComboBox combobox; 
	    public JTextField textfield;
	    
	    public JButton buyBtn;
	    public JButton sellBtn;
	    public JButton nextBtn;
    	
    	MyBroker(String[] companyList){
    		this.setLayout(new GridBagLayout());
    	    
    	    JLabel label1 = new JLabel("Quantity:");
    	    spinner = new JSpinner(new SpinnerNumberModel(0,0,40,1));
    	    JLabel label2 = new JLabel("Company:");
    	    combobox = new JComboBox(companyList);        
    	    buyBtn = new JButton("Buy");
    	    sellBtn = new JButton("Sell");
    	    JLabel label3 = new JLabel("Info:");
    	    textfield = new JTextField("You can only buy a maximum of 5 shares!");
    	    textfield.setEditable(false);
    	    textfield.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    	    nextBtn = new JButton(new ImageIcon("images/next_icon.png"));
    	    
    	    // Grid Bag Layout
    	    this.add(label1,getGBC(0, 0, 1, 1, BOTH, 0.0, 0.0, LINE_START, new Insets(5,10,5,10)));
    	    this.add(spinner,getGBC(1, 0, 1, 1, BOTH, 0.0, 0.0, LINE_START, new Insets(5,10,5,10),0,5));
    	    this.add(label2,getGBC(0, 1, 1, 1, BOTH, 0.0, 0.0, LINE_START, new Insets(5,10,5,10)));
    	    this.add(combobox,getGBC(1, 1, 2, 1, NONE, 0.0, 0.0, LINE_START, new Insets(5,10,5,10)));
    	    this.add(buyBtn,getGBC(1, 2, 1, 1, NONE, 0.0, 0.0, LINE_START, new Insets(5,10,5,5)));
    	    this.add(sellBtn,getGBC(2, 2, 1, 1, NONE, 0.0, 0.0, LINE_START, new Insets(5,5,5,10)));
    	    this.add(label3,getGBC(0, 3, 1, 1, BOTH, 0.0, 0.0, LINE_START, new Insets(5,10,5,10)));
    	    this.add(textfield,getGBC(1, 3, 3, 1, BOTH, 0.0, 0.0, LINE_START, new Insets(5,10,5,10)));
    	    this.add(nextBtn,getGBC(4, 0, 1, 4, VERTICAL, 1.0, 1.0, LINE_END, new Insets(5,10,5,5)));
    	}
    }
}