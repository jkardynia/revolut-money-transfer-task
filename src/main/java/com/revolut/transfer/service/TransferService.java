package com.revolut.transfer.service;


import com.revolut.transfer.repository.AccountRepository;

import javax.inject.Singleton;

@Singleton
public class TransferService {

    private AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public void transfer(String from, String to, long amount){
        accountRepository.transferMoney(from, to, amount);
    }
}
