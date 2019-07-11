package com.revolut;

import com.revolut.transfer.controller.AccountController;
import com.revolut.transfer.repository.AccountRepository;
import com.revolut.transfer.service.TransferService;
import org.javalite.activejdbc.DB;


public class Configuration {
    private static DB db = new DB();
    private static final AccountRepository accountRepository = new AccountRepository(db);
    private static final TransferService transferService = new TransferService(accountRepository);
    private static final AccountController accountController = new AccountController(transferService, accountRepository);

    public static TransferService transferService(){
        return transferService;
    }

    public static AccountRepository accountRepository(){
        return accountRepository;
    }

    public static AccountController accountController(){
        return accountController;
    }

    public static DB db(){
        return db;
    }


}
