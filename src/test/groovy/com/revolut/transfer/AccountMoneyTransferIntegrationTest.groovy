package com.revolut.transfer

import com.jayway.restassured.RestAssured
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.response.Response
import com.jayway.restassured.response.ValidatableResponse
import com.revolut.Application
import com.revolut.transfer.controller.dto.TransferDto
import org.junit.After
import org.junit.Before
import spock.lang.Shared
import spock.lang.Specification

import static com.jayway.restassured.RestAssured.get
import static com.jayway.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.CoreMatchers.equalTo

class AccountMoneyTransferIntegrationTest extends Specification{

    @Shared
    Application app;

    @Before
    def startApplication(){

        def appPort = 8080
        app = new Application(appPort)
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = appPort
    }

    @After
    def stopApplication(){
        if(app != null) {
            app.stop()
        }
        RestAssured.reset()
    }

    def "get account details"(){
        given:
        // I am using existing accounts to make it simpler for the sake of this exercise
        // not to hit DB directly or add account via API

        when:
        Response response = get("/account/123456")

        then:
        response.then().assertThat()
            .body("balance", equalTo(100))
            .body("number", equalTo("123456"))
            .body("owner", equalTo("Ryszard Kowalski"))
            .statusCode(200)
    }

    def "transfer money"(){
        given:
        // I am using existing accounts to make it simpler for the sake of this exercise
        // not to hit DB directly or add account via API

        def from = "12345"
        def to = "123456"
        def amount = 50L

        def request = given()
                .body(new TransferDto(from, to, amount))
                .contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())

        when:
        Response response = request.post("/transfer")

        then:
        response.then().assertThat()
                .statusCode(204)

        assertAccountBalance("123456", 150)
        assertAccountBalance("12345", 50)
    }

    def "should not allow to transfer money when not enough funds"(){
        given:
        // I am using existing accounts to make it simpler for the sake of this exercise
        // not to hit DB directly or add account via API

        def from = "12345"
        def to = "123456"
        def amount = 150L // more than on the account

        def request = given()
                .body(new TransferDto(from, to, amount))
                .contentType(ContentType.JSON)

        when:
        Response response = request.post("/transfer")

        then:
        response.then().assertThat()
                .body("message", containsString("balance"))
                .statusCode(400)

        assertAccountBalance("12345", 100)
        assertAccountBalance("123456", 100)
    }

    private ValidatableResponse assertAccountBalance(String accountNumber, int balance) {
        get("/account/" + accountNumber)
                .then().assertThat()
                .body("balance", equalTo(balance))
    }
}
