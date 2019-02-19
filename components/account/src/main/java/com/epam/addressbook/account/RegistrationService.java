package com.epam.addressbook.account;

import com.epam.addressbook.account.data.AccountDataGateway;
import com.epam.addressbook.person.data.PersonDataGateway;
import com.epam.addressbook.person.data.PersonRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final PersonDataGateway personDataGateway;
    private final AccountDataGateway accountDataGateway;

    public RegistrationService(PersonDataGateway personDataGateway, AccountDataGateway accountDataGateway) {
        this.personDataGateway = personDataGateway;
        this.accountDataGateway = accountDataGateway;
    }

    @Transactional
    public PersonRecord createPersonWithAccount(String loginName, String loginPassword, String firstName, String lastName, String email, String phone) {
        PersonRecord user = personDataGateway.create(firstName, lastName, email, phone);
        accountDataGateway.create(user.getId(), loginName, loginPassword);
        return user;
    }
}