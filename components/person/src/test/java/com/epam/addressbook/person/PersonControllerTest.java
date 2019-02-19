package com.epam.addressbook.person;

import com.epam.addressbook.person.data.PersonDataGateway;
import com.epam.addressbook.person.data.PersonRecord;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PersonControllerTest {

    private PersonDataGateway gateway = mock(PersonDataGateway.class);
    private PersonController controller = new PersonController(gateway);

    @Test
    public void getById_returnsInfo() {
        PersonRecord record = new PersonRecord(1L, "first", "last", "first last", "fl@example.com", "+1111111111");

        when(gateway.getById(1L)).thenReturn(record);

        PersonInfo result = controller.getById(1L);

        verify(gateway).getById(1L);
        PersonInfo info = new PersonInfo(1L, "first", "last", "first last", "fl@example.com", "+1111111111", "Person info");

        assertThat(result).isEqualTo(info);
    }
}