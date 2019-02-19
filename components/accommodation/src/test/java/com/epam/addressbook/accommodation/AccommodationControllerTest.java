package com.epam.addressbook.accommodation;

import com.epam.addressbook.accommodation.data.AccommodationDataGateway;
import com.epam.addressbook.accommodation.data.AccommodationFields;
import com.epam.addressbook.accommodation.data.AccommodationRecord;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccommodationControllerTest {

    private AccommodationDataGateway accommodationDataGateway = mock(AccommodationDataGateway.class);
    private HousingClient client = mock(HousingClient.class);
    private AccommodationController accommodationController = new AccommodationController(accommodationDataGateway, client);

    @Test
    public void create_whenHousingIsActive_returnsCreatedAccommodation() {
        AccommodationForm form = new AccommodationForm();
        form.setHousingId(2L);
        form.setPersonId(3L);
        form.setSingleOwned(true);
        form.setStartDate("2018-01-01");
        form.setEndDate("2018-12-01");

        HousingInfo housingInfo = new HousingInfo();
        housingInfo.setId(1L);
        housingInfo.setAccountId(2L);
        housingInfo.setName("Name");
        housingInfo.setAddressLine1("AddressLine1");
        housingInfo.setAddressLine2("AddressLine2");
        housingInfo.setCity("City");
        housingInfo.setState("CA");
        housingInfo.setZip5("12345");
        housingInfo.setActive(true);

        AccommodationRecord accommodationRecord = new AccommodationRecord();
        accommodationRecord.setId(1L);
        accommodationRecord.setHousingId(2L);
        accommodationRecord.setPersonId(3L);
        accommodationRecord.setSingleOwned(true);
        accommodationRecord.setStartDate(LocalDate.of(2018, 1, 1));
        accommodationRecord.setEndDate(LocalDate.of(2018, 12, 1));

        when(client.getHousing(2L)).thenReturn(housingInfo);
        when(accommodationDataGateway.create(any())).thenReturn(accommodationRecord);


        ResponseEntity<AccommodationInfo> response = accommodationController.create(form);


        verify(client).getHousing(2L);
        verify(accommodationDataGateway).create(new AccommodationFields(2L, 3L, true,
                LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-01")));

        AccommodationInfo accommodationInfo = new AccommodationInfo();
        accommodationInfo.setId(1L);
        accommodationInfo.setHousingId(2L);
        accommodationInfo.setPersonId(3L);
        accommodationInfo.setSingleOwned(true);
        accommodationInfo.setStartDate("2018-01-01");
        accommodationInfo.setEndDate("2018-12-01");
        accommodationInfo.setInfo("Accommodation info");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(accommodationInfo);
    }

    @Test
    public void create_whenProjectIsNotActive_returnsServiceUnavailable() {
        AccommodationForm form = new AccommodationForm();
        form.setHousingId(2L);
        form.setSingleOwned(false);

        HousingInfo housingInfo = new HousingInfo();
        housingInfo.setId(1L);
        housingInfo.setActive(false);

        when(client.getHousing(2L)).thenReturn(housingInfo);


        ResponseEntity<AccommodationInfo> response = accommodationController.create(form);


        verify(client).getHousing(2L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    public void list_whenHousingIsFound_returnsInfos() {
        AccommodationRecord firstRecord = new AccommodationRecord();
        firstRecord.setId(10L);
        firstRecord.setHousingId(2L);
        firstRecord.setPersonId(5L);
        firstRecord.setSingleOwned(true);
        firstRecord.setStartDate(LocalDate.of(2018, 1, 1));
        firstRecord.setEndDate(LocalDate.of(2018, 10, 1));

        AccommodationRecord secondRecord = new AccommodationRecord();
        secondRecord.setId(15L);
        secondRecord.setHousingId(2L);
        secondRecord.setPersonId(5L);
        secondRecord.setSingleOwned(false);
        secondRecord.setStartDate(LocalDate.of(2018, 10, 1));
        secondRecord.setEndDate(LocalDate.of(2018, 12, 1));

        List<AccommodationRecord> records = asList(firstRecord, secondRecord);
        when(accommodationDataGateway.findAllByHousingId(2L)).thenReturn(records);


        List<AccommodationInfo> result = accommodationController.list(2L);


        verify(accommodationDataGateway).findAllByHousingId(2L);

        AccommodationInfo firstInfo = new AccommodationInfo();
        firstInfo.setId(10L);
        firstInfo.setHousingId(2L);
        firstInfo.setPersonId(5L);
        firstInfo.setSingleOwned(true);
        firstInfo.setStartDate("2018-01-01");
        firstInfo.setEndDate("2018-10-01");
        firstInfo.setInfo("Accommodation info");

        AccommodationInfo secondInfo = new AccommodationInfo();
        secondInfo.setId(15L);
        secondInfo.setHousingId(2L);
        secondInfo.setPersonId(5L);
        secondInfo.setSingleOwned(false);
        secondInfo.setStartDate("2018-10-01");
        secondInfo.setEndDate("2018-12-01");
        secondInfo.setInfo("Accommodation info");

        assertThat(result).containsExactlyInAnyOrder(firstInfo, secondInfo);
    }
}