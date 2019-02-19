package com.epam.addressbook.housing;

import com.epam.addressbook.housing.data.HousingDataGateway;
import com.epam.addressbook.housing.data.HousingFields;
import com.epam.addressbook.housing.data.HousingRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/housings")
public class HousingController {

    private final HousingDataGateway gateway;

    public HousingController(HousingDataGateway gateway) {
        this.gateway = gateway;
    }

    @PostMapping
    public ResponseEntity<HousingInfo> create(@RequestBody HousingForm form) {
        HousingRecord record = gateway.create(formToFields(form));
        return new ResponseEntity<>(present(record), HttpStatus.CREATED);
    }

    @GetMapping
    public List<HousingInfo> list(@RequestParam long accountId) {
        return gateway.findAllByAccountId(accountId)
                .stream()
                .map(this::present)
                .collect(toList());
    }

    @GetMapping("/{housingId}")
    public HousingInfo get(@PathVariable long housingId) {
        HousingRecord record = gateway.findById(housingId);

        if (record == null) {
            return null;
        } else {
            return present(record);
        }
    }

    private HousingFields formToFields(HousingForm form) {
        return new HousingFields(form.getAccountId(), form.getName(), form.getAddressLine1(), form.getAddressLine2(),
                form.getCity(), form.getState(), form.getZip5(), form.isActive());
    }

    private HousingInfo present(HousingRecord record) {
        return new HousingInfo(record.getId(), record.getAccountId(), record.getName(), record.getAddressLine1(),
                record.getAddressLine2(), record.getCity(), record.getState(), record.getZip5(), record.isActive(), "Housing info");
    }
}