package com.revolut.transfer.controller.dto;

import com.revolut.transfer.repository.entity.Account;
import lombok.Data;

@Data
public class AccountDto {

    private String owner;
    private String number;
    private long balance;

    public static AccountDto from(Account account){
        AccountDto that = new AccountDto();

        that.setOwner(account.getOwner());
        that.setNumber(account.getNumber());
        that.setBalance(account.getBalance());

        return that;
    }
}
