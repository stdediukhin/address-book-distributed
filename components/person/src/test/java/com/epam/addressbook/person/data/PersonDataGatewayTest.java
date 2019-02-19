package com.epam.addressbook.person.data;

import com.epam.addressbook.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("address_book_registration_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private PersonDataGateway gateway = new PersonDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setUp() {
        template.execute("DELETE FROM HOUSING;");
        template.execute("DELETE FROM ACCOUNT;");
        template.execute("DELETE FROM PERSON;");
    }

    @Test
    public void create_whenAllParamsArePresented_returnsCorrectRecord() {
        PersonRecord createdPerson = gateway.create("firstName", "lastName", "test@example.com", "+12345678900");


        assertThat(createdPerson.getId()).isGreaterThan(0);
        assertThat(createdPerson.getFirstName()).isEqualTo("firstName");
        assertThat(createdPerson.getLastName()).isEqualTo("lastName");
        assertThat(createdPerson.getEmail()).isEqualTo("test@example.com");
        assertThat(createdPerson.getPhone()).isEqualTo("+12345678900");

        Map<String, Object> persistedFields = template.queryForMap("SELECT ID, FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE FROM PERSON WHERE ID = ?", createdPerson.getId());
        assertThat(persistedFields.get("ID")).isEqualTo(createdPerson.getId());
        assertThat(persistedFields.get("FIRST_NAME")).isEqualTo(createdPerson.getFirstName());
        assertThat(persistedFields.get("LAST_NAME")).isEqualTo(createdPerson.getLastName());
        assertThat(persistedFields.get("FULL_NAME")).isEqualTo(createdPerson.getFirstName() + " " + createdPerson.getLastName());
        assertThat(persistedFields.get("EMAIL")).isEqualTo(createdPerson.getEmail());
        assertThat(persistedFields.get("PHONE")).isEqualTo(createdPerson.getPhone());
    }

    @Test
    public void getById_whenIdIsProvidedAndRecordIsInDB_returnsFoundRecord() {
        template.execute("INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE) VALUES " +
                "(100500, 'aFirstName', 'aLastName', 'aFirstName aLastName', 'aa@example.com', null), " +
                "(100501, 'bFirstName', 'bLastName', 'bFirstName bLastName', 'bb@example.com', null), " +
                "(100502, 'cFirstName', 'cLastName', 'cFirstName cLastName', 'cc@example.com', null)");


        PersonRecord bbPerson = gateway.getById(100501L);


        assertThat(bbPerson).isEqualTo(new PersonRecord(100501, "bFirstName", "bLastName", "bFirstName bLastName", "bb@example.com", null));
    }

    @Test
    public void getById_whenIdIsProvidedAndRecordIsAbsentInDB_returnsNull() {
        assertThat(gateway.getById(42347L)).isNull();
    }
}