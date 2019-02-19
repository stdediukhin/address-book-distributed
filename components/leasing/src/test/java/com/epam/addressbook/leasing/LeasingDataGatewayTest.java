package com.epam.addressbook.leasing;

import com.epam.addressbook.leasing.data.LeasingDataGateway;
import com.epam.addressbook.leasing.data.LeasingFields;
import com.epam.addressbook.leasing.data.LeasingRecord;
import com.epam.addressbook.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LeasingDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("address_book_leasing_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private LeasingDataGateway gateway = new LeasingDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setUp() {
        template.execute("DELETE FROM LEASING;");
    }

    @Test
    public void create_whenAllFieldsArePresented_returnsCreatedRecord() {
        LocalDate now = LocalDate.now();
        LeasingFields fields = new LeasingFields(2L, 3L, now, 12);


        LeasingRecord created = gateway.create(fields);


        assertThat(created.getId()).isNotNull();
        assertThat(created.getHousingId()).isEqualTo(2L);
        assertThat(created.getPersonId()).isEqualTo(3L);
        assertThat(created.getAssignDate()).isEqualTo(now);
        assertThat(created.getMonths()).isEqualTo(12);

        Map<String, Object> persisted = template.queryForMap("SELECT * FROM LEASING WHERE ID = ?", created.getId());

        assertThat(persisted.get("HOUSING_ID")).isEqualTo(2L);
        assertThat(persisted.get("PERSON_ID")).isEqualTo(3L);

        Timestamp assignDate = (Timestamp) persisted.get("ASSIGN_DATE");
        LocalDate resultDate = assignDate.toLocalDateTime().toLocalDate();

        assertThat(resultDate).isEqualTo(now);
        assertThat(persisted.get("MONTHS")).isEqualTo(12);
    }

    @Test
    public void findAllByPersonId_whenFoundByPersonId_returnsRecords() {
        LocalDate date = LocalDate.of(2018, 1, 1);

        template.execute("insert into LEASING (ID, HOUSING_ID, PERSON_ID, ASSIGN_DATE, MONTHS) values (1, 2, 3, '2018-01-01', 12)");


        List<LeasingRecord> result = gateway.findAllByPersonId(3L);

        LeasingRecord record = new LeasingRecord();
        record.setId(1L);
        record.setHousingId(2L);
        record.setPersonId(3L);
        record.setAssignDate(date);
        record.setMonths(12);

        assertThat(result).containsExactlyInAnyOrder(record);
    }
}