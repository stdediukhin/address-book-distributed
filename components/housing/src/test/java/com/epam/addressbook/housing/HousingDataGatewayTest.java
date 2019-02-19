package com.epam.addressbook.housing;

import com.epam.addressbook.housing.data.HousingDataGateway;
import com.epam.addressbook.housing.data.HousingFields;
import com.epam.addressbook.housing.data.HousingRecord;
import com.epam.addressbook.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HousingDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("address_book_registration_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private HousingDataGateway gateway = new HousingDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setUp() {
        template.execute("DELETE FROM HOUSING;");
        template.execute("DELETE FROM ACCOUNT;");
        template.execute("DELETE FROM PERSON;");
    }

    @Test
    public void create_whenAllFieldsArePresented_returnsCreatedRecord() {
        template.execute("INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE) " +
                "VALUES (25, 'JOHN', 'JOHNSON', 'JOHN JOHNSON', 'example@test.com', '+11111111111')");
        template.execute("INSERT INTO ACCOUNT (ID, PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD) VALUES (1, 25, 'loginName', 'loginPassword')");

        HousingFields fields = new HousingFields(1L, "aHouse", "addressLine1", "addressLine2", "city", "ST", "zip5", true);


        HousingRecord created = gateway.create(fields);


        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("aHouse");
        assertThat(created.getAccountId()).isEqualTo(1L);
        assertThat(created.getAddressLine1()).isEqualTo("addressLine1");
        assertThat(created.getAddressLine2()).isEqualTo("addressLine2");
        assertThat(created.getCity()).isEqualTo("city");
        assertThat(created.getState()).isEqualTo("ST");
        assertThat(created.getZip5()).isEqualTo("zip5");
        assertThat(created.isActive()).isEqualTo(true);

        Map<String, Object> persisted = template.queryForMap("SELECT * FROM HOUSING WHERE ID = ?", created.getId());

        assertThat(persisted.get("NAME")).isEqualTo("aHouse");
        assertThat(persisted.get("ACCOUNT_ID")).isEqualTo(1L);
        assertThat(persisted.get("ADDRESS_LINE_1")).isEqualTo("addressLine1");
        assertThat(persisted.get("ADDRESS_LINE_2")).isEqualTo("addressLine2");
        assertThat(persisted.get("CITY")).isEqualTo("city");
        assertThat(persisted.get("STATE")).isEqualTo("ST");
        assertThat(persisted.get("ZIP5")).isEqualTo("zip5");
        assertThat(persisted.get("ACTIVE")).isEqualTo(true);
    }

    @Test
    public void findAllByAccountId_whenRecordsForAccountIdAreAvailable_returnsRecords() {
        template.execute("INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE) " +
                "VALUES (25, 'John', 'Johnson', 'John Johnson', 'example@test.com', '+11111111111')");
        template.execute("INSERT INTO ACCOUNT (ID, PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD) VALUES (1, 25, 'loginName', 'loginPassword')");
        template.execute("INSERT INTO HOUSING (ID, ACCOUNT_ID, NAME, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, STATE, ZIP5) " +
                "VALUES (20, 1, 'aHouse', 'AddressLine1', 'AddressLine2', 'City', 'ST', '12345')");


        List<HousingRecord> result = gateway.findAllByAccountId(1L);


        HousingRecord record = new HousingRecord(20L, 1L, "aHouse", "AddressLine1", "AddressLine2", "City", "ST", "12345", true);
        assertThat(result).containsExactly(record);
    }

    @Test
    public void findById_whenFoundById_returnsRecord() {
        template.execute("INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME, FULL_NAME, EMAIL, PHONE) " +
                "VALUES (25, 'JOHN', 'JOHNSON', 'JOHN JOHNSON', 'example@test.com', '+11111111111')");
        template.execute("INSERT INTO ACCOUNT (ID, PERSON_ID, LOGIN_NAME, LOGIN_PASSWORD) VALUES (1, 25, 'loginName', 'loginPassword')");
        template.execute("INSERT INTO HOUSING (ID, ACCOUNT_ID, NAME, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, STATE, ZIP5) " +
                "VALUES (20, 1, 'aHouse', 'AddressLine1', 'AddressLine2', 'City', 'ST', '12345')");


        HousingRecord foundRecord = gateway.findById(20L);


        HousingRecord record = new HousingRecord(20L, 1L, "aHouse", "AddressLine1", "AddressLine2", "City", "ST", "12345", true);
        assertThat(foundRecord).isEqualTo(record);
    }
}