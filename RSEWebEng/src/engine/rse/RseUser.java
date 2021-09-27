package engine.rse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RseUser {
    private String userName;
    private boolean isAdmin;
    private Integer moneyAmount = 0;
    protected RseHoldings rseHoldings;
    private List<MoneyActions> actionsList;

    public RseUser() {
        actionsList = new ArrayList<>();
        this.rseHoldings = new RseHoldings();

    }

    public RseUser(String name, boolean isAdmin) {
        this.userName = name;
        this.isAdmin = isAdmin;
        actionsList = new ArrayList<>();
        this.rseHoldings = new RseHoldings();
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<MoneyActions> getActionsList() {
        return actionsList;
    }

    /**
     * Gets the value of the rseHoldings property.
     *
     * @return
     *     possible object is
     *     {@link RseHoldings }
     *
     */
    public RseHoldings getRseHoldings() {
        return rseHoldings;
    }

    /**
     * Sets the value of the rseHoldings property.
     *
     * @param value
     *     allowed object is
     *     {@link RseHoldings }
     *
     */
    public void setRseHoldings(RseHoldings value) {
        this.rseHoldings = value;
    }

    public boolean checkSale(Integer amount, String sym) {
        RseItem item = rseHoldings.findItem(sym);
        return (item.getQuantity() - item.getAwaitingSale()) >= amount;
    }

    public void loadMoney(int amount){

        actionsList.add(new MoneyActions(MoneyActions.LOAD_ACTION,
                new SimpleDateFormat("HH:mm:ss:SSS").format(System.currentTimeMillis()),
                amount,
                moneyAmount));
        moneyAmount += amount;
    }

    public synchronized void offerAccepted(String symbol,int amount,int dtype,int price,String date) {
        int actionType = 0;
        RseItem item = rseHoldings.findItem(symbol);
        if (item == null) {
            item = new RseItem(symbol, 0);
            rseHoldings.getRseItem().add(item);
        }

        if (dtype == RseOffer.BUY) {
            item.addToAwaitingSale(amount);
            amount *= -1;
            actionType = MoneyActions.BUY_ACTION;
        }
        if (dtype == RseOffer.SELL) {
            price *= -1;
            actionType = MoneyActions.SELL_ACTION;
        }

        item.setQuantity(item.getQuantity() + amount);
        amount *= amount > 0 ? 1:-1;
        if (item.getQuantity() == 0)
            rseHoldings.getRseItem().remove(item);

        actionsList.add(new MoneyActions(actionType,
                date,
                amount * price,
                moneyAmount));

        updateMoney(price * amount);
    }

    private synchronized void updateMoney(int amount){
        moneyAmount += amount;
    }
}
