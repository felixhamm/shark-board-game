public class Player {
    private String name;
    private int cash = 0;
    private boolean active = true;
    private int shares[] = {0, 0, 0, 0};

    public Player(String playerName){
        name = playerName;
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
