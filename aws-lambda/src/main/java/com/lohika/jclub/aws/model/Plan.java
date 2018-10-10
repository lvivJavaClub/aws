package com.lohika.jclub.aws.model;

public enum Plan {
    MONTHLY(62, 0),
    DAILY(4, 0),
    ONE_TIME(0, 3);

    private int scheduledWithdrawalAmount;

    private int perEntranceWithdrawalAmount;

    Plan(int scheduledWithdrawalAmount, int perEntranceWithdrawalAmount) {
        this.scheduledWithdrawalAmount = scheduledWithdrawalAmount;
        this.perEntranceWithdrawalAmount = perEntranceWithdrawalAmount;
    }

    public int getScheduledWithdrawalAmount() {
        return scheduledWithdrawalAmount;
    }

    public int getPerEntranceWithdrawalAmount() {
        return perEntranceWithdrawalAmount;
    }
}