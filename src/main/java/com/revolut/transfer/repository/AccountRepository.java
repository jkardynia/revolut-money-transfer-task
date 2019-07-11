package com.revolut.transfer.repository;

import com.revolut.transfer.exception.AccountException;
import com.revolut.transfer.repository.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.DB;

import java.util.Optional;

@Slf4j
public class AccountRepository {

    private DB db;

    public AccountRepository(DB db){
        this.db = db;
    }

    public void transferMoney(String from, String to, long amount){
        log.info("Transfering money from : " + from + " to " + to);
        Account fromAccount = getAccount(from).orElseThrow(() -> AccountException.notFound(from));
        Account toAccount = getAccount(to).orElseThrow(() -> AccountException.notFound(to));

        db.withDb(() -> {
            db.openTransaction();

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            fromAccount.saveIt();
            toAccount.saveIt();
            db.commitTransaction();

            return Optional.empty();
        });
    }

    public Optional<Account> getAccount(String accountNumber){

        log.info("Getting account info: " + accountNumber);
        return db.withDb(() -> {
            return Optional.ofNullable(Account.findFirst("number = ?", accountNumber));
        });
    }
}
