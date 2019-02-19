package com.epam.addressbook.account;

import com.epam.addressbook.person.PersonInfo;
import com.epam.addressbook.person.data.PersonRecord;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RegistrationControllerTest {

    private RegistrationService registrationService = mock(RegistrationService.class);
    private RegistrationController registrationController = new RegistrationController(registrationService);

    @Test
    public void create_shouldCallsRegistrationServiceAndReturnsInfo() {
        PersonRecord personRecord = new PersonRecord(24L, "Billy", "Milligan", "Billy Milligan", "bm@example.com", "+12345678900");
        doReturn(personRecord).when(registrationService).createPersonWithAccount(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());


        PersonInfo result = registrationController.create(new RegistrationForm("login", "password", "Billy", "Milligan", "bm@example.com", "+12345678900"));


        verify(registrationService).createPersonWithAccount("login", "password", "Billy", "Milligan",
                "bm@example.com", "+12345678900");
        assertThat(result).isEqualTo(new PersonInfo(24L, "Billy", "Milligan", "Billy Milligan",
                "bm@example.com", "+12345678900", "registration info"));
    }
}