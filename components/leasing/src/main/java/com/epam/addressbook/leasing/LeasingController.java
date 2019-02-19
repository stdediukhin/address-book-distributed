package com.epam.addressbook.leasing;

import com.epam.addressbook.leasing.data.LeasingDataGateway;
import com.epam.addressbook.leasing.data.LeasingFields;
import com.epam.addressbook.leasing.data.LeasingRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/leasings")
public class LeasingController {

    private final LeasingDataGateway gateway;
    private final HousingClient client;

    public LeasingController(LeasingDataGateway gateway, HousingClient client) {
        this.gateway = gateway;
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<LeasingInfo> create(@RequestBody LeasingForm form) {
        if (housingIsActive(form.getHousingId())) {
            LeasingRecord record = gateway.create(mapToFields(form));
            return new ResponseEntity<>(present(record), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping
    public List<LeasingInfo> list(@RequestParam long personId) {
        return gateway.findAllByPersonId(personId).stream()
                .map(this::present)
                .collect(toList());
    }

    private LeasingInfo present(LeasingRecord record) {
        return new LeasingInfo(record.getId(), record.getHousingId(), record.getPersonId(), record.getAssignDate().toString(), record.getMonths(), "Leasing info");
    }

    private LeasingFields mapToFields(LeasingForm form) {
        return new LeasingFields(form.getHousingId(), form.getPersonId(), LocalDate.parse(form.getAssignDate()), form.getMonths());
    }

    private boolean housingIsActive(long housingId) {
        HousingInfo housing = client.getHousing(housingId);
        return housing != null && housing.isActive();
    }
}