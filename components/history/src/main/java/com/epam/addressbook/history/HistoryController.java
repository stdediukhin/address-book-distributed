package com.epam.addressbook.history;

import com.epam.addressbook.history.data.HistoryDataGateway;
import com.epam.addressbook.history.data.HistoryFields;
import com.epam.addressbook.history.data.HistoryRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/histories")
public class HistoryController {
    private final HistoryDataGateway gateway;
    private final HousingClient client;

    public HistoryController(HistoryDataGateway gateway, HousingClient client) {
        this.gateway = gateway;
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<HistoryInfo> create(@RequestBody HistoryForm form) {
        if (housingIsActive(form.getHousingId())) {
            HistoryRecord record = gateway.create(mapToFields(form));
            return new ResponseEntity<>(present(record), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping
    public List<HistoryInfo> getAll(@RequestParam long housingId) {
        return gateway.findAllByHousingId(housingId).stream()
                .map(this::present)
                .collect(toList());
    }

    private boolean housingIsActive(long housingId) {
        HousingInfo project = client.getHousing(housingId);
        return project != null && project.isActive();
    }

    private HistoryFields mapToFields(HistoryForm form) {
        return new HistoryFields(form.getHousingId(), form.getName(), form.getDescription());
    }

    private HistoryInfo present(HistoryRecord record) {
        return new HistoryInfo(record.getId(), record.getHousingId(), record.getName(), record.getDescription(), "History info");
    }
}