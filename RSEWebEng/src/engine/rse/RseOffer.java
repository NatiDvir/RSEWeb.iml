package engine.rse;

import java.text.SimpleDateFormat;

public class RseOffer {

    public static final int MKT_CODE = -1;
    public static final int BUY =1;
    public static final int SELL =2;
    public static final int FOK = 3;
    public static final int IOC =4;
    public static final int LMT =5;


    protected String dateOfDeal;

    protected RseUser user;
    protected RseUser secondUser;
    protected Integer amount;
    protected Integer price;
    protected int offerType;
    protected boolean isSell;

    public RseOffer(){}

    public RseOffer( RseUser user, Integer amount, Integer price, int offerType,boolean isSell) {
        this.dateOfDeal = new SimpleDateFormat("HH:mm:ss:SSS").format(System.currentTimeMillis());
        this.user = user;
        this.amount = amount;
        this.price = price;
        this.offerType = offerType;
        this.isSell = isSell;
    }



    public RseOffer(RseUser user,RseUser user2, String dateOfDeal, Integer amount, Integer price, int isMKT, boolean isSell) {
        this.user = user;
        this.secondUser = user2;
        this.dateOfDeal = dateOfDeal;
        this.amount = amount;
        this.price = price;
        this.offerType = isMKT;
    }

    public RseUser getUser() {
        return user;
    }

    public RseUser getSecondUser() {
        return secondUser;
    }

    public String getDateOfDeal() {
        return dateOfDeal;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public int getOfferType() {
        return offerType;
    }
    public static String getOfferTypeString(int oType) {
        if(oType == MKT_CODE)
            return  "MKT";
        if(oType == LMT)
            return  "LMT";
        if(oType == FOK)
          return  "FOK";
        if(oType == IOC)
           return  "IOC";
        return null;
    }
    @Override
    public String toString() {
        return "Date: " + dateOfDeal
                + ", Deal type: " + getOfferTypeString(offerType)
                + ", Amount of Stocks in deal: " + amount
                + ", Price of each Stock: " + price
                + ", Total price of deal: " + price * amount
                + ", Initiating User: "+user.getUserName()
                + ((secondUser != null)? ", Closing User: "+secondUser.getUserName():"");
    }
}
