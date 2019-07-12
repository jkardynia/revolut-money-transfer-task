package com.revolut.transfer

import com.jayway.restassured.RestAssured
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.response.Response
import com.revolut.Application
import com.revolut.transfer.controller.dto.TransferDto
import org.junit.AfterClass
import org.junit.BeforeClass
import spock.lang.Shared
import spock.lang.Specification

import static com.jayway.restassured.RestAssured.get
import static com.jayway.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.equalTo

class AccountMoneyTransferIntegrationTest extends Specification{

    @Shared
    Application app;

    @BeforeClass
    def startApplication(){

        def appPort = 8080
        app = new Application(appPort)
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = appPort
    }

    @AfterClass
    def stopApplication(){
        if(app != null) {
            app.stop()
        }
        RestAssured.reset()
    }

    def "get account details"(){
        given:
        // I am using existing accounts to make it simpler for sake of this exercise
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
        // I am using existing accounts to make it simpler for sake of this exercise
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

        get("/account/123456")
                .then().assertThat()
                .body("balance", equalTo(150))

        get("/account/12345")
                .then().assertThat()
                .body("balance", equalTo(50))
    }
}
