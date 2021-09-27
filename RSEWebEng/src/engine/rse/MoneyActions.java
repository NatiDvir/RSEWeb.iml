package engine.rse;

import java.util.Date;

public class MoneyActions {
    final static int LOAD_ACTION = 1;
    final static int BUY_ACTION = 2;
    final static int SELL_ACTION = 3;

    private int actionType;
    private String actionDate;
    private int amount;
    private int balanceBefore;
    private int balanceAfter;

    public MoneyActions() {
    }

    public MoneyActions(int actionType, String date, int amount, int balanceBefore) {
        this.actionType = actionType;
        this.actionDate = date;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceBefore + amount;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(int balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public int getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
}
