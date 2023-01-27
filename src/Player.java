public class Player {
    private String name;
    private int cash = 0;
    private boolean active = true;
    private int shares[] = {0, 0, 0, 0};
    private int debt = 0;
    
    private int playerIndex;

    public Player(int playerIndex, String playerName){
        this.playerIndex = playerIndex;
    	name = playerName;
    }
    
    public int getPlayerIndex(){
    	return playerIndex;
    }

    public String getName(){
        return name;
    }

    public int getCash(){
        return cash;
    }

    public void addCash(int amount){
        cash = cash + amount;
    }

    public void setCash(int newCash){
        cash = newCash;
    }

    public int getShares(int company){
        return shares[company];
    }

    public void setShares(int company, int newShares){
        shares[company] = newShares;
    }
    
    public void setDebt(int debt){
    	this.debt = debt;
    }
    
    public int getDebt(){
    	return debt;
    }

    public void disablePlayer(){
        active = false;
    }

    public boolean getActivityStatus(){
        return active;
    }

    public int getMaxLoss(int nysoSharePrice, int wingsSharePrice, int empireSharePrice, int smithAndSmithSharePrice){
        double maxLoss = cash + ( 0.5 * (nysoSharePrice + wingsSharePrice + empireSharePrice + smithAndSmithSharePrice));
        return (int)maxLoss; 
    }
}
