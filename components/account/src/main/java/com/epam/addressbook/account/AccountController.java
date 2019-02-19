package com.epam.addressbook.account;

import com.epam.addressbook.account.data.AccountDataGateway;
import com.epam.addressbook.account.data.AccountRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {

    private final AccountDataGateway gateway;

    public AccountController(AccountDataGateway gateway) {
        this.gateway = gateway;
    }

    @GetMapping("/accounts")
    public List<AccountInfo> getAccounts(@RequestParam long personId) {
        return gateway.findAllByPersonId(personId)
                .stream()
                .map(this::recordToInfo)
                .collect(toList());
    }

    private AccountInfo recordToInfo(AccountRecord record) {
        return new AccountInfo(record.getId(), record.getPersonId(), record.getLoginName(), record.getLoginPassword(), "account info");
    }
}