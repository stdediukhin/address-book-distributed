package com.epam.addressbook.leasing;

import com.epam.addressbook.leasing.data.LeasingDataGateway;
import com.epam.addressbook.leasing.data.LeasingFields;
import com.epam.addressbook.leasing.data.LeasingRecord;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LeasingControllerTest {

    private LeasingDataGateway gateway = mock(LeasingDataGateway.class);
    private HousingClient client = mock(HousingClient.class);
    private LeasingController controller = new LeasingController(gateway, client);


    @Test
    public void create_whenHousingIsActive_returnsCreatedRecord() {
        LocalDate now = LocalDate.now();

        LeasingRecord record = new LeasingRecord();
        record.setId(1L);
        record.setHousingId(2L);
        record.setPersonId(3L);
        record.setAssignDate(now);
        record.setMonths(12);

        LeasingFields fields = new LeasingFields(2L, 3L, now, 12);
        LeasingForm form = new LeasingForm(2L, 3L, now.toString(), 12);

        HousingInfo housingInfo = new HousingInfo(2L, 5L, "hName", "haddr1",
                "haddr2", "hcity", "hstate", "12345", true, "hInfo");

        when(gateway.create(fields)).thenReturn(record);
        when(client.getHousing(2L)).thenReturn(housingInfo);


        ResponseEntity<LeasingInfo> result = controller.create(form);


        verify(client).getHousing(2L);
        LeasingInfo leasingInfo = new LeasingInfo(1L, 2L, 3L, now.toString(), 12, "Leasing info");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(leasingInfo);
    }

    @Test
    public void create_whenHousingIsInactive_returnsServiceUnavailable() {
        LeasingForm form = new LeasingForm(2L, 3L, LocalDate.now().toString(), 12);

        HousingInfo housingInfo = new HousingInfo(2L, 5L, "hName", "haddr1",
                "haddr2", "hcity", "hstate", "12345", false, "hInfo");

        when(client.getHousing(2L)).thenReturn(housingInfo);


        ResponseEntity<LeasingInfo> result = controller.create(form);


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    public void list_whenFoundByPersonId_returnsInfos() {
        LocalDate now = LocalDate.now();

        LeasingRecord firstRecord = new LeasingRecord();
        firstRecord.setId(1L);
        firstRecord.setHousingId(2L);
        firstRecord.setPersonId(3L);
        firstRecord.setAssignDate(now);
        firstRecord.setMonths(12);

        LeasingRecord secondRecord = new LeasingRecord();
        secondRecord.setId(2L);
        secondRecord.setHousingId(2L);
        secondRecord.setPersonId(3L);
        secondRecord.setAssignDate(now);
        secondRecord.setMonths(12);

        LeasingRecord thirdRecord = new LeasingRecord();
        thirdRecord.setId(3L);
        thirdRecord.setHousingId(2L);
        thirdRecord.setPersonId(3L);
        thirdRecord.setAssignDate(now);
        thirdRecord.setMonths(12);

        List<LeasingRecord> records = Lists.newArrayList(firstRecord, secondRecord, thirdRecord);
        when(gateway.findAllByPersonId(3L)).thenReturn(records);
        int personId = 3;

        List<LeasingInfo> result = controller.list(personId);


        verify(gateway).findAllByPersonId(personId);

        LeasingInfo firstInfo = new LeasingInfo(1L, 2L, 3L, now.toString(), 12, "Leasing info");
        LeasingInfo secondInfo = new LeasingInfo(2L, 2L, 3L, now.toString(), 12, "Leasing info");
        LeasingInfo thirdInfo = new LeasingInfo(3L, 2L, 3L, now.toString(), 12, "Leasing info");

        assertThat(result).containsExactlyInAnyOrder(firstInfo, secondInfo, thirdInfo);
    }
}