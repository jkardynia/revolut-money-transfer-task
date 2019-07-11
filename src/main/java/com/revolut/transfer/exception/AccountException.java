package com.revolut.transfer.exception;

public class AccountException extends RuntimeException {
    private AccountException(String msg){
        super(msg);
    }

    public static AccountException notFound(String accountNumber) {
        return new AccountException("Account " + accountNumber + " not found");
    }
}
