package com.revolut.transfer.controller;

import com.revolut.transfer.controller.dto.AccountDto;
import com.revolut.transfer.controller.dto.TransferDto;
import com.revolut.transfer.exception.AccountException;
import com.revolut.transfer.repository.AccountRepository;
import com.revolut.transfer.service.TransferService;
import io.javalin.http.Handler;

public class AccountController {
    public static final String ACCOUNT_NUMBER_PATH_PARAM = "accountNumber";

    private TransferService transferService;
    private AccountRepository accountRepository;

    public AccountController(TransferService transferService, AccountRepository accountRepository){
        this.transferService = transferService;
        this.accountRepository = accountRepository;
    }

    public Handler transfer = ctx -> {
        TransferDto transferDto = ctx.bodyAsClass(TransferDto.class);

        //todo validate if amount is positive number
        transferService.transfer(transferDto.getSource(), transferDto.getDestination(), transferDto.getAmount());
        ctx.status(204);
    };

    public Handler accountDetails = ctx -> {
        String accountNumber = ctx.pathParam(ACCOUNT_NUMBER_PATH_PARAM);
        AccountDto account = accountRepository.getAccount(accountNumber)
                .map(AccountDto::from)
                .orElseThrow(() -> AccountException.notFound(accountNumber));
        ctx.json(account);
        ctx.status(200);
    };
}
