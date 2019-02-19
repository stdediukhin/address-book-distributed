package com.epam.addressbook.history;

import com.epam.addressbook.history.data.HistoryDataGateway;
import com.epam.addressbook.history.data.HistoryFields;
import com.epam.addressbook.history.data.HistoryRecord;
import com.epam.addressbook.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HistoryDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("address_book_history_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private HistoryDataGateway gateway = new HistoryDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setUp() {
        template.execute("DELETE FROM HISTORY;");
    }

    @Test
    public void create_whenAllFieldsArePresented_returnsCreatedRecord() {
        HistoryFields fields = new HistoryFields(5L, "aHistoryName", "aHistoryDescription");


        HistoryRecord created = gateway.create(fields);


        assertThat(created.getId()).isNotNull();
        assertThat(created.getHousingId()).isEqualTo(5L);
        assertThat(created.getName()).isEqualTo("aHistoryName");
        assertThat(created.getDescription()).isEqualTo("aHistoryDescription");

        Map<String, Object> persisted = template.queryForMap("SELECT * FROM HISTORY WHERE ID = ?", created.getId());

        assertThat(persisted.get("HOUSING_ID")).isEqualTo(5L);
        assertThat(persisted.get("NAME")).isEqualTo("aHistoryName");
        assertThat(persisted.get("DESCRIPTION")).isEqualTo("aHistoryDescription");
    }

    @Test
    public void findAllByHousingId_whenRecordsForHousingIdAreAvailable_returnsRecords() {
        template.execute("INSERT INTO HISTORY (ID, HOUSING_ID, NAME, DESCRIPTION) VALUES (1000, 25, 'aHistoryName', 'aHistoryDescription')");


        List<HistoryRecord> result = gateway.findAllByHousingId(25L);


        assertThat(result).containsExactly(new HistoryRecord(1000L, 25L, "aHistoryName", "aHistoryDescription"));
    }
}