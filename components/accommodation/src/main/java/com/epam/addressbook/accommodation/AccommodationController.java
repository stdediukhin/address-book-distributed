package com.epam.addressbook.accommodation;

import com.epam.addressbook.accommodation.data.AccommodationDataGateway;
import com.epam.addressbook.accommodation.data.AccommodationFields;
import com.epam.addressbook.accommodation.data.AccommodationRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/accommodations")
public class AccommodationController {

    private final AccommodationDataGateway gateway;
    private final HousingClient client;

    public AccommodationController(AccommodationDataGateway gateway, HousingClient client) {
        this.gateway = gateway;
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<AccommodationInfo> create(@RequestBody AccommodationForm form) {
        if (housingIsActive(form.getHousingId())) {
            AccommodationRecord record = gateway.create(formToFields(form));
            return new ResponseEntity<>(present(record), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping
    public List<AccommodationInfo> list(@RequestParam long housingId) {
        return gateway.findAllByHousingId(housingId)
                .stream()
                .map(this::present)
                .collect(toList());
    }

    private boolean housingIsActive(long housingId) {
        HousingInfo housing = client.getHousing(housingId);

        return housing != null && housing.isActive();
    }

    private AccommodationFields formToFields(AccommodationForm form) {
        return new AccommodationFields(form.getHousingId(), form.getPersonId(), form.isSingleOwned(),
                LocalDate.parse(form.getStartDate()), LocalDate.parse(form.getEndDate()));
    }

    private AccommodationInfo present(AccommodationRecord record) {
        return new AccommodationInfo(record.getId(), record.getHousingId(), record.getPersonId(), record.isSingleOwned(),
                record.getStartDate().toString(), record.getEndDate().toString(), "Accommodation info");
    }
}