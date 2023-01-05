public class Company {
    private int sharePrice;
    private int sharePriceOld;
    private int remainingShares;   // In der Anleitung wird nicht explizit erwähnt wie viele Shares es pro Unternehmen gibt. Nur dass es insgesamt 80 gibt.
    private int remainingMarkers;

    public Company(){
        remainingShares = 40;
        remainingMarkers = 18;
        sharePrice = 0;
        sharePriceOld = 0;
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

    public void updateRemainingShares(){

    }

    public void updateRemainingMarkers(){

    }
}
