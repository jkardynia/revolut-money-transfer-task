package com.revolut.transfer

import com.revolut.Application
import org.junit.AfterClass
import org.junit.BeforeClass
import spock.lang.Shared
import spock.lang.Specification

class AccountMoneyTransferIntegrationTest extends Specification{

    @Shared
    Application app;

    @BeforeClass
    def startApplication(){
        app = new Application(8080)
    }

    @AfterClass
    def stopApplication(){
        if(app != null) {
            app.stop()
        }
    }

    def "hello world test"(){
        given:
        def a = "string 1"

        when:
        def b = "string 1"

        then:
        a == b
    }
}
