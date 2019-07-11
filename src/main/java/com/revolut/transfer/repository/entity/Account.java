package com.revolut.transfer.repository.entity;

import org.javalite.activejdbc.Model;

public class Account extends Model {

    static{
        validateNumericalityOf("balance").greaterThan(0);
    }

    public String getOwner() {
        return getString("owner");
    }

    public void setOwner(String owner) {
        setString("owner", owner);
    }

    public String getNumber() {
        return getString("number");
    }

    public void setNumber(String number) {
        setString("number", number);
    }

    public Long getBalance() {
        return getLong("balance");
    }

    public void setBalance(Long balance) {
        setLong("balance", balance);
    }
}
