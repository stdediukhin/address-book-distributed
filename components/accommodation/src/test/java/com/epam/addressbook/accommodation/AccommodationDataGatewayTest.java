package com.epam.addressbook.accommodation;

import com.epam.addressbook.accommodation.data.AccommodationDataGateway;
import com.epam.addressbook.accommodation.data.AccommodationFields;
import com.epam.addressbook.accommodation.data.AccommodationRecord;
import com.epam.addressbook.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AccommodationDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("address_book_accommodation_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private AccommodationDataGateway gateway = new AccommodationDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setup() {
        template.execute("DELETE FROM ACCOMMODATION;");
    }

    @Test
    public void create_whenAllFieldsArePresented_returnsCreatedRecord() {
        AccommodationFields fields = new AccommodationFields(2L, 3L, true, LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 1));


        AccommodationRecord created = gateway.create(fields);


        assertThat(created.getId()).isNotNull();
        assertThat(created.getHousingId()).isEqualTo(2L);
        assertThat(created.getPersonId()).isEqualTo(3L);
        assertThat(created.isSingleOwned()).isEqualTo(true);
        assertThat(created.getStartDate()).isEqualTo(LocalDate.parse("2018-01-01"));
        assertThat(created.getEndDate()).isEqualTo(LocalDate.parse("2018-12-01"));

        Map<String, Object> persisted = template.queryForMap("SELECT * FROM ACCOMMODATION WHERE ID = ?", created.getId());

        assertThat(persisted.get("HOUSING_ID")).isEqualTo(2L);
        assertThat(persisted.get("PERSON_ID")).isEqualTo(3L);
        assertThat(persisted.get("SINGLE_OWNED")).isEqualTo(true);
        assertThat(persisted.get("START_DATE")).isEqualTo(Timestamp.valueOf("2018-01-01 00:00:00"));
        assertThat(persisted.get("END_DATE")).isEqualTo(Timestamp.valueOf("2018-12-01 00:00:00"));
    }

    @Test
    public void findAllByHousingId_whenRecordsForHousingIdAreAvailable_returnsSortedRecords() {
        template.execute("INSERT INTO ACCOMMODATION (ID, HOUSING_ID, PERSON_ID, SINGLE_OWNED, START_DATE, END_DATE) VALUES (100500, 2, 3, true, '2018-01-01', '2018-10-01')");
        template.execute("INSERT INTO ACCOMMODATION (ID, HOUSING_ID, PERSON_ID, SINGLE_OWNED, START_DATE, END_DATE) VALUES (100600, 2, 4, false, '2018-10-01', '2018-12-01')");
        template.execute("INSERT INTO ACCOMMODATION (ID, HOUSING_ID, PERSON_ID, SINGLE_OWNED, START_DATE, END_DATE) VALUES (100700, 2, 5, false, '2018-10-01', '2018-12-01')");


        List<AccommodationRecord> result = gateway.findAllByHousingId(2L);

        AccommodationRecord firstRecord = new AccommodationRecord();
        firstRecord.setId(100500L);
        firstRecord.setHousingId(2L);
        firstRecord.setPersonId(3L);
        firstRecord.setSingleOwned(true);
        firstRecord.setStartDate(LocalDate.of(2018, 1, 1));
        firstRecord.setEndDate(LocalDate.of(2018, 10, 1));

        AccommodationRecord secondRecord = new AccommodationRecord();
        secondRecord.setId(100600L);
        secondRecord.setHousingId(2L);
        secondRecord.setPersonId(4L);
        secondRecord.setSingleOwned(false);
        secondRecord.setStartDate(LocalDate.of(2018, 10, 1));
        secondRecord.setEndDate(LocalDate.of(2018, 12, 1));

        AccommodationRecord thirdRecord = new AccommodationRecord();
        thirdRecord.setId(100700L);
        thirdRecord.setHousingId(2L);
        thirdRecord.setPersonId(5L);
        thirdRecord.setSingleOwned(false);
        thirdRecord.setStartDate(LocalDate.of(2018, 10, 1));
        thirdRecord.setEndDate(LocalDate.of(2018, 12, 1));

        assertThat(result).containsExactly(firstRecord, secondRecord, thirdRecord);
    }
}