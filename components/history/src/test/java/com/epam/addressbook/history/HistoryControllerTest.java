package com.epam.addressbook.history;

import com.epam.addressbook.history.data.HistoryDataGateway;
import com.epam.addressbook.history.data.HistoryFields;
import com.epam.addressbook.history.data.HistoryRecord;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HistoryControllerTest {

    private HistoryDataGateway historyDataGateway = mock(HistoryDataGateway.class);
    private HousingClient client = mock(HousingClient.class);
    private HistoryController historyController = new HistoryController(historyDataGateway, client);

    @Test
    public void create_whenHousingIsActive_shouldReturnsCreatedEntity() {
        HistoryRecord record = new HistoryRecord(4L, 3L, "Name", "Description");

        HistoryFields historyFields = new HistoryFields(3L, "historyName", "historyDescription");

        HistoryForm form = new HistoryForm(3L, "historyName", "historyDescription");

        HistoryInfo historyInfo = new HistoryInfo(4L, 3L, "Name", "Description", "History info");

        HousingInfo housingInfo = new HousingInfo(3L, 5L, "hName", "haddr1", "haddr2", "hcity", "hstate", "12345", true, "hInfo");

        when(historyDataGateway.create(historyFields)).thenReturn(record);
        when(client.getHousing(3L)).thenReturn(housingInfo);


        ResponseEntity<HistoryInfo> response = historyController.create(form);


        verify(client).getHousing(3L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(historyInfo);
    }

    @Test
    public void create_whenHousingIsInactive_returnsServiceUnavailable() {
        when(client.getHousing(3L)).thenReturn(null);

        HistoryForm form = new HistoryForm(3L, "historyName", "historyDescription");


        ResponseEntity<HistoryInfo> response = historyController.create(form);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    public void getAll_whenFoundByHousingId_returnsFoundInfos() {
        HistoryRecord firstRecord = new HistoryRecord(1L, 3L, "Name", "Description");

        HistoryRecord secondRecord = new HistoryRecord(2L, 3L, "Name", "Description");

        when(historyDataGateway.findAllByHousingId(3L)).thenReturn(Lists.newArrayList(firstRecord, secondRecord));


        List<HistoryInfo> result = historyController.getAll(3L);


        verify(historyDataGateway).findAllByHousingId(3L);

        HistoryInfo firstInfo = new HistoryInfo(1L, 3L, "Name", "Description", "History info");
        HistoryInfo secondInfo = new HistoryInfo(2L, 3L, "Name", "Description", "History info");

        assertThat(result).containsExactly(firstInfo, secondInfo);
    }
}