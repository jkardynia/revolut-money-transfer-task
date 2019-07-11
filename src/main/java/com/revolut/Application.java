package com.revolut;

import com.revolut.transfer.controller.AccountController;
import com.revolut.transfer.controller.ErrorResponse;
import com.revolut.transfer.exception.AccountException;
import com.revolut.transfer.repository.entity.Account;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.validation.ValidationException;

import java.util.Optional;

import static com.revolut.Configuration.accountController;
import static com.revolut.Configuration.db;

@Slf4j
public class Application {

    private final Javalin app;

    public static void main(String[] args) {
        new Application(8080);
    }

    public Application(int port){
        Javalin app = Javalin.create();
        configureEndpoints(app);
        addExceptionHandlers(app);
        initDb();
        app.start(port);

        this.app = app;
    }

    public void stop(){
        app.stop();
    }

    private static void configureEndpoints(Javalin app){

        app.get("/account/:" + AccountController.ACCOUNT_NUMBER_PATH_PARAM,
                accountController().accountDetails);
        app.post("/transfer",
                accountController().transfer);
    }

    private static void addExceptionHandlers(Javalin app){
        app.exception(AccountException.class, (e, ctx) -> {
            log.error(e.getMessage(), e);
            ctx.status(400);
            ctx.json(ErrorResponse.of(e.getMessage()));
        });

        app.exception(ValidationException.class, (e, ctx) -> {
            log.error(e.getMessage(), e);
            ctx.status(400);
            ctx.json(ErrorResponse.of(e.getMessage()));
        });

        app.exception(Exception.class, (e, ctx) -> {
            log.error(e.getMessage(), e);
            ctx.status(500);
            ctx.json(ErrorResponse.of("Internal error"));
        });
    }

    private static void initDb(){
        db().withDb(() -> {
            Account.deleteAll();
            Account acc1 = new Account();
            acc1.setBalance(100L);
            acc1.setId(1);
            acc1.setOwner("John Doe");
            acc1.setNumber("12345");
            acc1.insert();

            Account acc2 = new Account();
            acc2.setBalance(100L);
            acc2.setId(2);
            acc2.setOwner("Ryszard Kowalski");
            acc2.setNumber("123456");
            acc2.insert();

            return Optional.empty();
        });

    }
}
