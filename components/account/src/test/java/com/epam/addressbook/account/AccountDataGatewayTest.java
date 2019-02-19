package com.epam.addressbook.account;

import com.epam.addressbook.account.data.AccountDataGateway;
import com.epam.addressbook.account.data.AccountRecord;
import com.epam.addressbook.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("address_book_registration_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private AccountDataGateway gateway = new AccountDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setup() {
        template.execute("DELETE FROM HOUSING;");
        template.execute("DELETE FROM ACCOUNT;");
        template.execute("DELETE FROM PERSON;");
    }

    @Test
    public void create_whenAllFieldsArePresented_returnsCreatedRecord() {
        template.execute("INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE) " +
                "VALUES (25, 'JOHN', 'JOHNSON', 'JOHN JOHNSON', 'example@test.com', '+11111111111')");


        AccountRecord created = gateway.create(25L, "loginName", "loginPassword");


        assertThat(created.getId()).isNotNull();
        assertThat(created.getPersonId()).isEqualTo(25);
        assertThat(created.getLoginName()).isEqualTo("loginName");
        assertThat(created.getLoginPassword()).isEqualTo("loginPassword");

        Map<String, Object> persisted = template.queryForMap("SELECT * FROM ACCOUNT WHERE ID = ?", created.getId());
        assertThat(persisted.get("PERSON_ID")).isEqualTo(25L);
        assertThat(persisted.get("LOGIN_NAME")).isEqualTo("loginName");
        assertThat(persisted.get("LOGIN_PASSWORD")).isEqualTo("loginPassword");
    }

    @Test
    public void findAllByPersonId_whenRecordsForPersonIdAreAvailable_returnsSortedRecords() {
        template.execute("INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE) " +
                "VALUES (25, 'JOHN', 'JOHNSON', 'JOHN JOHNSON', 'example@test.com', '+11111111111')");
        template.execute("INSERT INTO ACCOUNT (ID, PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD) VALUES (1, 25, 'BBB', 'bbbPass')");
        template.execute("INSERT INTO ACCOUNT (ID, PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD) VALUES (2, 25, 'AAA', 'aaaPass')");
        template.execute("INSERT INTO ACCOUNT (ID, PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD) VALUES (3, 25, 'CCC', 'cccPass')");


        List<AccountRecord> result = gateway.findAllByPersonId(25L);

        AccountRecord firstRecord = new AccountRecord(2L, 25L, "AAA", "aaaPass");
        AccountRecord secondRecord = new AccountRecord(1L, 25L, "BBB", "bbbPass");
        AccountRecord thirdRecord = new AccountRecord(3L, 25L, "CCC", "cccPass");

        assertThat(result).containsExactly(firstRecord, secondRecord, thirdRecord);
    }
}