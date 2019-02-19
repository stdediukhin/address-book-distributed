package com.epam.addressbook.account;

import com.epam.addressbook.account.data.AccountDataGateway;
import com.epam.addressbook.person.data.PersonDataGateway;
import com.epam.addressbook.person.data.PersonRecord;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {
    private PersonDataGateway personDataGateway = mock(PersonDataGateway.class);
    private AccountDataGateway accountDataGateway = mock(AccountDataGateway.class);
    private RegistrationService service = new RegistrationService(personDataGateway, accountDataGateway);

    @Test
    public void createPersonWithAccount_shouldCallPersonAndAccountDataGatewaysAndReturnsPersonRecord() {
        PersonRecord createdUser = new PersonRecord(25L, "first", "last", "first last", "f@l.com", "+123");
        doReturn(createdUser).when(personDataGateway).create("first", "last", "f@l.com", "+123");


        PersonRecord result = service.createPersonWithAccount("login", "password", "first", "last", "f@l.com", "+123");


        verify(personDataGateway).create("first", "last", "f@l.com", "+123");
        verify(accountDataGateway).create(25L, "login", "password");

        PersonRecord expectedResult = new PersonRecord(25L, "first", "last", "first last", "f@l.com", "+123");
        assertThat(result).isEqualTo(expectedResult);
    }
}