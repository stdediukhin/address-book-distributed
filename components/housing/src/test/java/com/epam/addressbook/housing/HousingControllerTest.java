package com.epam.addressbook.housing;

import com.epam.addressbook.housing.data.HousingDataGateway;
import com.epam.addressbook.housing.data.HousingFields;
import com.epam.addressbook.housing.data.HousingRecord;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HousingControllerTest {

    private HousingDataGateway gateway = mock(HousingDataGateway.class);
    private HousingController controller = new HousingController(gateway);

    @Test
    public void create_returnsInfo() {
        HousingRecord housingRecord = new HousingRecord(1L, 2L, "name", "addr1", "addr2", "city", "ST", "12345", true);

        HousingFields housingFields = new HousingFields(2L, "name", "addr1", "addr2", "city", "ST", "12345", true);

        HousingForm housingForm = new HousingForm(2L, "name", "addr1", "addr2", "city", "ST", "12345", true);

        HousingInfo housingInfo = new HousingInfo(1L, 2L, "name", "addr1", "addr2", "city", "ST", "12345", true, "Housing info");

        when(gateway.create(any())).thenReturn(housingRecord);


        ResponseEntity<HousingInfo> result = controller.create(housingForm);


        verify(gateway).create(housingFields);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(housingInfo);
    }

    @Test
    public void list_whenFoundByAccountId_returnsFoundInfos() {
        HousingRecord firstRecord = new HousingRecord(1L, 2L, "name", "addr1", "addr2", "city", "ST", "12345", true);

        HousingRecord secondRecord = new HousingRecord(2L, 2L, "name", "addr1", "addr2", "city", "ST", "12345", true);

        List<HousingRecord> records = asList(firstRecord, secondRecord);

        when(gateway.findAllByAccountId(2L)).thenReturn(records);


        List<HousingInfo> result = controller.list(2L);


        HousingInfo firstInfo = new HousingInfo(1L, 2L, "name", "addr1", "addr2", "city", "ST", "12345", true, "Housing info");

        verify(gateway).findAllByAccountId(2L);
        HousingInfo secondInfo = new HousingInfo(2L, 2L, "name", "addr1", "addr2", "city", "ST", "12345", true, "Housing info");
        assertThat(result).containsExactly(firstInfo, secondInfo);
    }

    @Test
    public void get_whenFoundRecord_returnsInfo() {
        HousingRecord record = new HousingRecord(1L, 2L, "name", "addr1", "addr2", "city", "ST", "12345", true);
        when(gateway.findById(1L)).thenReturn(record);


        HousingInfo result = controller.get(1L);


        verify(gateway).findById(1L);
        HousingInfo info = new HousingInfo(1L, 2L, "name", "addr1", "addr2", "city", "ST", "12345", true, "Housing info");
        assertThat(result).isEqualTo(info);
    }

    @Test
    public void get_whenRecordIsAbsent_returnsNull() {
        when(gateway.findById(66L)).thenReturn(null);

        HousingInfo result = controller.get(66L);

        verify(gateway).findById(66L);
        assertThat(result).isNull();
    }
}