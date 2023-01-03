public class Company {
    private int sharePrice;
    private int sharePriceOld;
    private int remainingShares;   // In der Anleitung wird nicht explizit erwähnt wie viele Shares es pro Unternehmen gibt. Nur dass es insgesamt 80 gibt.
    private int remainingMarkers = 18;

    public Company(int sharesAmount, int markerAmount){
        remainingShares = sharesAmount;
        remainingMarkers = markerAmount;
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
