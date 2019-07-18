package com.revolut.transfer.controller

import com.revolut.transfer.repository.AccountRepository
import com.revolut.transfer.service.TransferService
import io.javalin.http.Context
import io.javalin.http.util.ContextUtil
import org.junit.Before
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

class AccountControllerTest extends Specification{

    private TransferService transferServiceMock
    private AccountRepository accountRepositoryMock

    @Before
    def setup(){
        transferServiceMock = Mock(TransferService)
        accountRepositoryMock = Mock(AccountRepository)
    }

    // this is example test that uses mocks, it is not meant for full code coverage
    def "should transfer money"(){
        given:
        def accountController = new AccountController(transferServiceMock, accountRepositoryMock)

        def sourceAccount = "12340"
        def destinationAccount = "12341"
        def amount = 10

        def context = getContextForTransfer(sourceAccount, destinationAccount, amount)

        when:
        accountController.transfer.handle(context)

        then:
        1 * transferServiceMock.transfer(sourceAccount, destinationAccount, amount)
        context.status() == 204
    }

    private Context getContextForTransfer(String source, String destination, long amount) {
        MockHttpServletRequest req = new MockHttpServletRequest()
        req.setContent("{\"source\": \"${source}\", \"destination\": \"${destination}\", \"amount\": ${amount} }".getBytes("UTF-8"))

        MockHttpServletResponse res = new MockHttpServletResponse()

        ContextUtil.init(req, res)
    }
}
