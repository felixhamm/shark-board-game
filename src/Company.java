import java.awt.Color;

public class Company {
	private int companyIndex;	// Read Only
	private String companyName;	// Read Only
	private Color color;		// Read Only
	private int sharePrice;
    private int sharePriceOld;
    private int remainingShares;
    private int remainingMarkers;

    public Company(int companyIndex, String companyName, Color color){
       	remainingShares = 40;
        remainingMarkers = 18;
        sharePrice = 0;
        sharePriceOld = 0;
        
        this.companyIndex = companyIndex;
        this.companyName = companyName;
        this.color = color;
    }
    
    public int getCompanyIndex(){
    	return companyIndex;
    }
    
    public String getName(){
    	return companyName;
    }
    
    public Color getColor(){
    	return color;
    }

    public int getSharePrice(){
        return sharePrice;
    }

    public int getSharePriceOld(){
        return sharePriceOld;
    }

    public void setSharePrice(int newSharePrice){
        sharePrice = newSharePrice;
    }

    public void setSharePriceOld(int newSharePriceOld){
        sharePriceOld = newSharePriceOld;
    }

    public int getRemainingShares(){
        return remainingShares;
    }

    public void setRemainingShares(int shares){
        remainingShares = shares;
    }

    public void setRemainingMarkers(int markers){
        remainingMarkers = markers;
    }

    public int getRemainingMarkers(){
        return remainingMarkers;
    }
}
